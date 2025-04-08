package se.kth.felixhr.medical_records.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import se.kth.felixhr.medical_records.model.*;
import se.kth.felixhr.medical_records.repository.EncounterRepository;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EncounterControllerTest {

    @InjectMocks
    EncounterController controller;

    @Mock
    EncounterRepository encounterRepository;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller.patientServiceUrl = "http://mock-patient-service";
    }

    @Test
    void addEncounter_shouldSucceed_whenPatientExists() {
        Map<String, Object> data = Map.of(
                "description", "Routine visit",
                "date", "2024-01-01",
                "doctorId", 2
        );

        when(restTemplate.getForEntity(eq("http://mock-patient-service/patient/1"), eq(PatientDTO.class)))
                .thenReturn(new ResponseEntity<>(new PatientDTO(1L, "Test"), HttpStatus.OK));

        ResponseEntity<String> response = controller.addEncounter(1L, data);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Encounter added successfully", response.getBody());
        verify(encounterRepository).save(any(Encounter.class));
    }

    @Test
    void addEncounter_shouldFail_whenPatientNotFound() {
        Map<String, Object> data = Map.of(
                "description", "Routine visit",
                "date", "2024-01-01",
                "doctorId", 2
        );

        when(restTemplate.getForEntity(anyString(), eq(PatientDTO.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        ResponseEntity<String> response = controller.addEncounter(1L, data);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
    }

    @Test
    void addEncounter_shouldReturn400_onInvalidDate() {
        Map<String, Object> data = Map.of(
                "description", "Checkup",
                "date", "not-a-date",
                "doctorId", 2
        );

        ResponseEntity<String> response = controller.addEncounter(1L, data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid date"));
    }

    @Test
    void addEncounter_shouldReturn400_onInvalidDoctorId() {
        Map<String, Object> data = Map.of(
                "description", "Checkup",
                "date", "2024-01-01",
                "doctorId", "bad-id"
        );

        ResponseEntity<String> response = controller.addEncounter(1L, data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid doctorId"));
    }

    @Test
    void addEncounter_shouldReturn400_onMissingFields() {
        Map<String, Object> data = Map.of(
                "description", "No date or doc"
        );

        ResponseEntity<String> response = controller.addEncounter(1L, data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Missing required"));
    }

    @Test
    void getEncountersByPatientId_shouldReturnList() {
        List<Encounter> mockEncounters = List.of(
                new Encounter(Date.valueOf("2024-01-01"), "Checkup", 1L, 2L)
        );

        when(encounterRepository.findByPatientId(1L)).thenReturn(mockEncounters);

        ResponseEntity<List<Encounter>> response = controller.getEncountersByPatientId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }
}
