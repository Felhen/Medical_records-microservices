package se.kth.felixhr.medical_records.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import se.kth.felixhr.medical_records.model.Condition;
import se.kth.felixhr.medical_records.model.PatientDTO;
import se.kth.felixhr.medical_records.repository.ConditionRepository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ConditionControllerTest {

    @InjectMocks
    ConditionController controller;

    @Mock
    ConditionRepository conditionRepository;

    @Mock
    RestTemplate restTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        controller.patientServiceUrl = "http://mock-patient-service";
    }

    @Test
    void getConditionsByPatientId_shouldReturnList() {
        List<Condition> mockConditions = List.of(new Condition("Asthma", "Chronic", Date.valueOf("2023-01-01"), 1L, 2L));
        when(conditionRepository.findByPatientId(1L)).thenReturn(mockConditions);

        ResponseEntity<List<Condition>> response = controller.getConditionsByPatientId(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void addCondition_shouldSucceed_whenPatientExists() {
        Map<String, Object> request = Map.of(
                "name", "Asthma",
                "description", "Chronic case",
                "date", "2024-03-01",
                "doctorId", 10
        );

        when(restTemplate.getForEntity(eq("http://mock-patient-service/patient/1"), eq(PatientDTO.class)))
                .thenReturn(new ResponseEntity<>(new PatientDTO(1L, "John Doe"), HttpStatus.OK));

        ResponseEntity<String> response = controller.addCondition(1L, request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Condition added successfully", response.getBody());
        verify(conditionRepository).save(any(Condition.class));
    }

    @Test
    void addCondition_shouldFail_whenPatientNotFound() {
        Map<String, Object> request = Map.of(
                "name", "Asthma",
                "description", "Chronic case",
                "date", "2024-03-01",
                "doctorId", 10
        );

        when(restTemplate.getForEntity(eq("http://mock-patient-service/patient/1"), eq(PatientDTO.class)))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.NOT_FOUND));

        ResponseEntity<String> response = controller.addCondition(1L, request);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Patient not found", response.getBody());
    }

    @Test
    void addCondition_shouldReturn400_onInvalidDateFormat() {
        Map<String, Object> request = Map.of(
                "name", "Asthma",
                "description", "Chronic case",
                "date", "invalid-date",
                "doctorId", 10
        );

        ResponseEntity<String> response = controller.addCondition(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid date"));
    }

    @Test
    void addCondition_shouldReturn400_onMissingField() {
        Map<String, Object> request = Map.of(
                "description", "Chronic case",
                "date", "2024-03-01",
                "doctorId", 10
        ); // Missing "name"

        ResponseEntity<String> response = controller.addCondition(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Missing"));
    }

    @Test
    void addCondition_shouldReturn400_onInvalidDoctorId() {
        Map<String, Object> request = Map.of(
                "name", "Asthma",
                "description", "Chronic case",
                "date", "2024-03-01",
                "doctorId", "not-a-number"
        );

        ResponseEntity<String> response = controller.addCondition(1L, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid doctorId"));
    }
}
