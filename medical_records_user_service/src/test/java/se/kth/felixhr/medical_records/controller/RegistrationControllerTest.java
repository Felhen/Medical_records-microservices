package se.kth.felixhr.medical_records.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import se.kth.felixhr.medical_records.model.*;
import se.kth.felixhr.medical_records.repository.*;
import se.kth.felixhr.medical_records.security.KeycloakService;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegistrationControllerTest {

    @InjectMocks
    RegistrationController registrationController;

    @Mock
    KeycloakService keycloakService;

    @Mock
    UserRepository userRepository;

    @Mock
    PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // ============ POSITIVE CASES ============

    @Test
    void testRegisterDoctor_Success() {
        Map<String, Object> data = Map.of(
                "username", "doc",
                "email", "doc@kth.se",
                "password", "pass",
                "role", "DOCTOR"
        );

        when(keycloakService.createUserInKeycloak("doc", "doc@kth.se", "pass", "DOCTOR"))
                .thenReturn("kc-123");

        ResponseEntity<String> response = registrationController.registerUser(data);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User created successfully", response.getBody());
        verify(userRepository).save(any(User.class));
        verify(patientRepository, never()).save(any());
    }

    @Test
    void testRegisterPatient_Success() {
        Map<String, Object> data = Map.of(
                "username", "pat",
                "email", "pat@kth.se",
                "password", "pass",
                "role", "PATIENT",
                "firstName", "Felix",
                "lastName", "HR",
                "birthdate", "1999-01-01",
                "personNumber", "6789"
        );

        when(keycloakService.createUserInKeycloak(any(), any(), any(), any())).thenReturn("kc-pat");

        ResponseEntity<String> response = registrationController.registerUser(data);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(userRepository).save(any(User.class));
        verify(patientRepository).save(any(Patient.class));
    }

    // ============ NEGATIVE CASES ============

    @Test
    void testMissingRequiredFields_ReturnsBadRequest() {
        Map<String, Object> data = Map.of(
                "username", "someone"
                // missing email, password, role
        );

        ResponseEntity<String> response = registrationController.registerUser(data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Missing required field(s)"));
    }

    @Test
    void testUnknownRole_ReturnsBadRequest() {
        Map<String, Object> data = Map.of(
                "username", "bob",
                "email", "bob@kth.se",
                "password", "1234",
                "role", "ALIEN" // invalid role
        );

        when(keycloakService.createUserInKeycloak(any(), any(), any(), eq("ALIEN"))).thenReturn("kc-x");

        ResponseEntity<String> response = registrationController.registerUser(data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid role"));
    }

    // ============ EDGE CASES ============

    @Test
    void testBirthdateInFuture_ReturnsBadRequest() {
        Map<String, Object> data = Map.of(
                "username", "futurekid",
                "email", "future@kth.se",
                "password", "1234",
                "role", "PATIENT",
                "firstName", "Test",
                "lastName", "Person",
                "birthdate", LocalDate.now().plusYears(1).toString(),
                "personNumber", "0001"
        );

        when(keycloakService.createUserInKeycloak(any(), any(), any(), any())).thenReturn("kc-future");

        ResponseEntity<String> response = registrationController.registerUser(data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("birthdate cannot be in the future"));
    }

    @Test
    void testInvalidPersonNumber_ReturnsBadRequest() {
        Map<String, Object> data = Map.of(
                "username", "noPNR",
                "email", "test@kth.se",
                "password", "pass",
                "role", "PATIENT",
                "firstName", "X",
                "lastName", "Y",
                "birthdate", "2000-01-01",
                "personNumber", "not-a-number"
        );

        when(keycloakService.createUserInKeycloak(any(), any(), any(), any())).thenReturn("kc-wrong");

        ResponseEntity<String> response = registrationController.registerUser(data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid personNumber"));
    }

    // ============ DEFENSIVE CASES ============

    @Test
    void testKeycloakCreationFails_Returns500() {
        Map<String, Object> data = Map.of(
                "username", "failuser",
                "email", "fail@kth.se",
                "password", "1234",
                "role", "DOCTOR"
        );

        when(keycloakService.createUserInKeycloak(any(), any(), any(), any())).thenReturn(null);

        ResponseEntity<String> response = registrationController.registerUser(data);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Keycloak user creation failed"));
    }

    @Test
    void testKeycloakThrowsException_Returns500() {
        Map<String, Object> data = Map.of(
                "username", "kaboom",
                "email", "boom@kth.se",
                "password", "pass",
                "role", "DOCTOR"
        );

        when(keycloakService.createUserInKeycloak(any(), any(), any(), any()))
                .thenThrow(new RuntimeException("Connection to Keycloak failed"));

        ResponseEntity<String> response = registrationController.registerUser(data);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertTrue(response.getBody().contains("Keycloak error"));
    }
}
