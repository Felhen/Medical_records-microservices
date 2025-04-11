package se.kth.felixhr.medical_records.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.felixhr.medical_records.model.Patient;
import se.kth.felixhr.medical_records.model.Role;
import se.kth.felixhr.medical_records.model.User;
import se.kth.felixhr.medical_records.repository.PatientRepository;
import se.kth.felixhr.medical_records.repository.UserRepository;
import se.kth.felixhr.medical_records.security.KeycloakService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Map;

@RestController
@CrossOrigin("https://felixhr-front.app.cloud.cbh.kth.se")
public class RegistrationController {

    @Autowired private KeycloakService keycloakService;
    @Autowired private UserRepository userRepository;
    @Autowired private PatientRepository patientRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, Object> userData) {
        if (userData.get("username") == null || userData.get("email") == null ||
                userData.get("password") == null || userData.get("role") == null) {
            return ResponseEntity.badRequest().body("Missing required field(s)");
        }
        String username = (String) userData.get("username");
        String email = (String) userData.get("email");
        String password = (String) userData.get("password");
        String role = (String) userData.get("role");

        if (!Arrays.asList("PATIENT", "DOCTOR", "STAFF").contains(role)) {
            return ResponseEntity.badRequest().body("Invalid role");
        }

        String keycloakId;
        try {
            keycloakId = keycloakService.createUserInKeycloak(username, email, password, role);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Keycloak error: " + e.getMessage());
        }
        System.out.println(keycloakId); //DEBUG
        if (keycloakId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Keycloak user creation failed");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setKeycloakId(keycloakId);
        user.setRole(Role.valueOf(role.toUpperCase()));
        userRepository.save(user);

        if (role.equalsIgnoreCase("PATIENT")) {
            if (userData.get("firstName") == null || userData.get("lastName") == null ||
                    userData.get("birthdate") == null || userData.get("personNumber") == null) {
                return ResponseEntity.badRequest().body("Missing required field(s)");
            }

            String firstName = (String) userData.get("firstName");
            String lastName = (String) userData.get("lastName");
            String birthdateStr = (String) userData.get("birthdate");

            Date birthdate;
            try {
                birthdate = Date.valueOf(birthdateStr);
                if (birthdate.toLocalDate().isAfter(LocalDate.now())) {
                    return ResponseEntity.badRequest().body("birthdate cannot be in the future");
                }
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid birthdate format");
            }

            int personNumber;
            try {
                personNumber = Integer.parseInt((String) userData.get("personNumber"));
            } catch (Exception e) {
                return ResponseEntity.badRequest().body("Invalid personNumber");
            }

            Patient patient = new Patient(firstName, lastName, birthdate, personNumber, user);
            patientRepository.save(patient);
        }

        return ResponseEntity.ok("User created successfully");
    }
}


