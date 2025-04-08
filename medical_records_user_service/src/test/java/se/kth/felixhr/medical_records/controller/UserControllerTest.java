package se.kth.felixhr.medical_records.controller;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import se.kth.felixhr.medical_records.model.*;
import se.kth.felixhr.medical_records.repository.UserRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserRepository userRepository;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateUser() {
        User user = new User("felix", "felix@kth.se", Role.DOCTOR, "kc-123");
        when(userRepository.save(any())).thenReturn(user);

        User saved = userController.newUser(user);
        assertEquals("felix", saved.getUsername());
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(List.of(new User(), new User()));
        assertEquals(2, userController.getAllUsers().size());
    }

    @Test
    void testGetUserByKeycloakId_FoundWithPatient() {
        User user = new User("patientUser", "email", Role.PATIENT, "kc-999");
        Patient patient = new Patient();
        patient.setId(7L);
        user.setPatient(patient);

        when(userRepository.findByKeycloakId("kc-999")).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = userController.getUserByKeycloakId("kc-999");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("PATIENT", response.getBody().get("role"));
        assertEquals(7L, response.getBody().get("patientId"));
    }

    @Test
    void testGetUserByKeycloakId_FoundWithoutPatient() {
        User user = new User("docUser", "doc@kth.se", Role.DOCTOR, "kc-doc");
        user.setPatient(null);

        when(userRepository.findByKeycloakId("kc-doc")).thenReturn(user);

        ResponseEntity<Map<String, Object>> response = userController.getUserByKeycloakId("kc-doc");
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("DOCTOR", response.getBody().get("role"));
        assertNull(response.getBody().get("patientId"));
    }

    @Test
    void testGetUserByKeycloakId_NullInput_Returns404() {
        ResponseEntity<Map<String, Object>> response = userController.getUserByKeycloakId(null);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    void testGetUserByKeycloakId_NotFound() {
        when(userRepository.findByKeycloakId("unknown")).thenReturn(null);
        ResponseEntity<Map<String, Object>> response = userController.getUserByKeycloakId("unknown");
        assertEquals(404, response.getStatusCodeValue());
    }
}
