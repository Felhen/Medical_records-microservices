package se.kth.felixhr.medical_records.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class KeycloakService {

    @Value("${keycloak.auth-server-url}")
    private String keycloakUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}") // Client ID
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    public String createUserInKeycloak(String username, String email, String password, String role) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // 1. Get admin token
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
            body.add("client_id", clientId);
            body.add("client_secret", clientSecret);
            body.add("grant_type", "client_credentials");

            HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity<>(body, headers);
            ResponseEntity<Map> tokenResponse = restTemplate.postForEntity(
                    keycloakUrl + "/realms/" + realm + "/protocol/openid-connect/token",
                    tokenRequest,
                    Map.class
            );

            String accessToken = (String) tokenResponse.getBody().get("access_token");

            // 2. Create user
            HttpHeaders userHeaders = new HttpHeaders();
            userHeaders.setBearerAuth(accessToken);
            userHeaders.setContentType(MediaType.APPLICATION_JSON);

            Map<String, Object> userPayload = new HashMap<>();
            userPayload.put("username", username);
            userPayload.put("email", email);
            userPayload.put("enabled", true);

            Map<String, Object> credential = new HashMap<>();
            credential.put("type", "password");
            credential.put("value", password);
            credential.put("temporary", false);
            userPayload.put("credentials", List.of(credential));


            HttpEntity<Map<String, Object>> createUserRequest = new HttpEntity<>(userPayload, userHeaders);
            ResponseEntity<Void> createUserResponse = restTemplate.postForEntity(
                    keycloakUrl + "/admin/realms/" + realm + "/users",
                    createUserRequest,
                    Void.class
            );

            if (createUserResponse.getStatusCode() != HttpStatus.CREATED) {
                return null;
            }

            // 3. Get the new user's UUID
            Thread.sleep(300); // allow Keycloak to index user
            UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(
                    keycloakUrl + "/admin/realms/" + realm + "/users"
            ).queryParam("username", username);

            HttpEntity<Void> getUserRequest = new HttpEntity<>(userHeaders);
            ResponseEntity<User[]> getUserResponse = restTemplate.exchange(
                    builder.toUriString(),
                    HttpMethod.GET,
                    getUserRequest,
                    User[].class
            );

            if (getUserResponse.getBody() != null && getUserResponse.getBody().length > 0) {
                String userId = getUserResponse.getBody()[0].getId(); // extract UUID

                // assign the realm role
                assignRealmRole(userId, role.toUpperCase(), accessToken);

                return userId;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // Temporary nested class to parse UUID result
    static class User {
        private String id;
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
    }

    public void assignRealmRole(String userId, String roleName, String accessToken) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        try {
            ResponseEntity<Map> roleResponse = restTemplate.exchange(
                    keycloakUrl + "/admin/realms/" + realm + "/roles/" + roleName,
                    HttpMethod.GET,
                    new HttpEntity<>(headers),
                    Map.class
            );

            if (roleResponse.getStatusCode().is2xxSuccessful() && roleResponse.getBody() != null) {
                List<Map<String, Object>> roleList = List.of(roleResponse.getBody());

                restTemplate.postForEntity(
                        keycloakUrl + "/admin/realms/" + realm + "/users/" + userId + "/role-mappings/realm",
                        new HttpEntity<>(roleList, headers),
                        Void.class
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

