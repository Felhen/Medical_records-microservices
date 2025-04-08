package se.kth.felixhr.medical_records.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import se.kth.felixhr.medical_records.model.*;
import se.kth.felixhr.medical_records.repository.ObservationRepository;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ObservationControllerTest {

    @InjectMocks
    ObservationController controller;

    @Mock
    ObservationRepository observationRepository;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller.patientServiceUrl = "http://mock-patient-service";
    }

    @Test
    void addObservation_shouldSucceed_whenPatientExists() {
        Map<String, Object> data = Map.of(
                "date", "2024-01-01",
                "description", "Temperature normal",
                "doctorId", 2
        );

        when(restTemplate.getForEntity(eq("http://mock-patient-service/patient/1"), eq(PatientDTO.class)))
                .thenReturn(new ResponseEntity<>(new PatientDTO(1L, "Test"), HttpStatus.OK));

        ResponseEntity<String> response = controller.addObservation(1L, data);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Observation added successfully", response.getBody());
        verify(observationRepository).save(any(Observation.class));
    }

    @Test
    void addObservation_shouldFail_whenPatientNotFound() {
        Map<String, Object> data = Map.of(
                "date", "2024-01-01",
                "description", "Pulse low",
                "doctorId", 2
        );

        when(restTemplate.getForEntity(anyString(), eq(PatientDTO.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        ResponseEntity<String> response = controller.addObservation(1L, data);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
    }

    @Test
    void addObservation_shouldReturn400_onInvalidDate() {
        Map<String, Object> data = Map.of(
                "date", "bad-date",
                "description", "BP check",
                "doctorId", 2
        );

        ResponseEntity<String> response = controller.addObservation(1L, data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid date"));
    }

    @Test
    void addObservation_shouldReturn400_onInvalidDoctorId() {
        Map<String, Object> data = Map.of(
                "date", "2024-01-01",
                "description", "Checkup",
                "doctorId", "invalid"
        );

        ResponseEntity<String> response = controller.addObservation(1L, data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid doctorId"));
    }

    @Test
    void addObservation_shouldReturn400_onMissingFields() {
        Map<String, Object> data = Map.of(
                "description", "Missing stuff"
        );

        ResponseEntity<String> response = controller.addObservation(1L, data);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Missing required"));
    }

    @Test
    void getObservationsByPatientId_shouldReturnList() {
        List<Observation> mockList = List.of(
                new Observation(Date.valueOf("2024-01-01"), "All good", 1L, 2L)
        );

        when(observationRepository.findByPatientId(1L)).thenReturn(mockList);

        ResponseEntity<List<Observation>> response = controller.getObservationsByPatientId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("All good", response.getBody().get(0).getObservation_info());
    }
}
