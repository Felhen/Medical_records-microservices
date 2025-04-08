package se.kth.felixhr.medical_records.controller;

import org.junit.jupiter.api.*;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import se.kth.felixhr.medical_records.model.Patient;
import se.kth.felixhr.medical_records.repository.PatientRepository;

import java.sql.Date;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PatientControllerTest {

    @InjectMocks
    PatientController patientController;

    @Mock
    PatientRepository patientRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreatePatient() {
        Patient patient = new Patient("John", "Doe", Date.valueOf("1990-01-01"), 12345, null);
        when(patientRepository.save(any())).thenReturn(patient);

        Patient result = patientController.newPatient(patient);
        assertEquals("John", result.getFirst_name());
    }

    @Test
    void testGetAllPatients() {
        List<Patient> mockList = List.of(new Patient(), new Patient());
        when(patientRepository.findAll()).thenReturn(mockList);

        List<Patient> result = patientController.getAllPatients();
        assertEquals(2, result.size());
    }

    @Test
    void testGetPatientById_Found() {
        Patient patient = new Patient();
        patient.setId(1L);
        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        ResponseEntity<Patient> response = patientController.getPatientById(1L);
        assertEquals(200, response.getStatusCodeValue());
    }

    @Test
    void testGetPatientById_NotFound() {
        when(patientRepository.findById(99L)).thenReturn(Optional.empty());
        ResponseEntity<Patient> response = patientController.getPatientById(99L);
        assertEquals(404, response.getStatusCodeValue());
    }
}
