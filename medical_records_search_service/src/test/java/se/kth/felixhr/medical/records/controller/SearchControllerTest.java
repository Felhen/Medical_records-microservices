package se.kth.felixhr.medical.records.controller;

import io.smallrye.mutiny.Uni;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import se.kth.felixhr.medical.records.search.controller.SearchController;
import se.kth.felixhr.medical.records.search.model.ConditionDTO;
import se.kth.felixhr.medical.records.search.model.EncounterDTO;
import se.kth.felixhr.medical.records.search.model.PatientDTO;
import se.kth.felixhr.medical.records.search.repository.ConditionRepository;
import se.kth.felixhr.medical.records.search.repository.EncounterRepository;
import se.kth.felixhr.medical.records.search.repository.PatientRepository;

import java.sql.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SearchControllerTest {

    @InjectMocks
    SearchController searchController;

    @Mock
    PatientRepository patientRepository;

    @Mock
    ConditionRepository conditionRepository;

    @Mock
    EncounterRepository encounterRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    // PATIENT BY NAME ---------------------
    @Test
    void testSearchPatientsByName_ReturnsList() {
        List<PatientDTO> mockPatients = List.of(
                new PatientDTO(1L, "John", "Doe", Date.valueOf("2000-01-01"), 3456)
        );
        when(patientRepository.getPatientsByName("John")).thenReturn(Uni.createFrom().item(mockPatients));

        List<PatientDTO> result = searchController.searchPatientsByName("John").await().indefinitely();

        assertEquals(1, result.size());
        assertEquals("John", result.get(0).firstName);
    }

    @Test
    void testSearchPatientsByName_EmptyList() {
        when(patientRepository.getPatientsByName("Nobody")).thenReturn(Uni.createFrom().item(List.of()));

        List<PatientDTO> result = searchController.searchPatientsByName("Nobody").await().indefinitely();

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchPatientsByName_RepositoryFails() {
        when(patientRepository.getPatientsByName("Fail")).thenReturn(Uni.createFrom().failure(new RuntimeException("DB Error")));

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                searchController.searchPatientsByName("Fail").await().indefinitely()
        );

        assertEquals("DB Error", e.getMessage());
    }

    // PATIENT BY DOCTOR ---------------------
    @Test
    void testSearchPatientsByDoctor_ReturnsList() {
        List<PatientDTO> mockPatients = List.of(
                new PatientDTO(2L, "Alice", "Svensson", Date.valueOf("1998-12-12"), 6543)
        );
        when(patientRepository.getPatientsByDoctorName("Dr. Bob")).thenReturn(Uni.createFrom().item(mockPatients));

        List<PatientDTO> result = searchController.searchPatientsByDoctor("Dr. Bob").await().indefinitely();

        assertEquals(1, result.size());
        assertEquals("Alice", result.get(0).firstName);
    }

    @Test
    void testSearchPatientsByDoctor_NullInput_ReturnsEmpty() {
        when(patientRepository.getPatientsByDoctorName(null)).thenReturn(Uni.createFrom().item(List.of()));

        List<PatientDTO> result = searchController.searchPatientsByDoctor(null).await().indefinitely();

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchPatientsByDoctor_Failure() {
        when(patientRepository.getPatientsByDoctorName("Error")).thenReturn(Uni.createFrom().failure(new RuntimeException("Doctor fetch failed")));

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                searchController.searchPatientsByDoctor("Error").await().indefinitely()
        );

        assertEquals("Doctor fetch failed", e.getMessage());
    }

    // CONDITIONS ---------------------
    @Test
    void testSearchConditions_ReturnsList() {
        List<ConditionDTO> mockConditions = List.of(
                new ConditionDTO(1L, 1L, "asthma", "chronic", Date.valueOf("2023-05-01"), "Jane", "Doe")
        );

        when(conditionRepository.getPatientsByCondition("asthma")).thenReturn(Uni.createFrom().item(mockConditions));

        List<ConditionDTO> result = searchController.searchConditions("asthma").await().indefinitely();

        assertEquals(1, result.size());
        assertEquals("asthma", result.get(0).conditionName);
    }

    @Test
    void testSearchConditions_EmptyQuery_ReturnsEmptyList() {
        when(conditionRepository.getPatientsByCondition("")).thenReturn(Uni.createFrom().item(List.of()));

        List<ConditionDTO> result = searchController.searchConditions("").await().indefinitely();

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchConditions_RepositoryThrows() {
        when(conditionRepository.getPatientsByCondition("bad"))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("DB down")));

        RuntimeException thrown = assertThrows(RuntimeException.class, () ->
                searchController.searchConditions("bad").await().indefinitely()
        );

        assertEquals("DB down", thrown.getMessage());
    }

    // ENCOUNTERS BY DATE ---------------------
    @Test
    void testSearchEncountersByDoctorAndDate_ReturnsList() {
        Date date = Date.valueOf("2025-04-08");
        List<EncounterDTO> mockEncounters = List.of(
                new EncounterDTO(10L, 1L, 2L, date, "Routine check", "Alice Svensson")
        );

        when(encounterRepository.getEncountersByDoctorAndDate("Dr. Carl", date))
                .thenReturn(Uni.createFrom().item(mockEncounters));

        List<EncounterDTO> result = searchController.searchEncountersByDoctorAndDate("Dr. Carl", "2025-04-08").await().indefinitely();

        assertEquals(1, result.size());
        assertEquals("Routine check", result.get(0).encounterInfo);
    }

    @Test
    void testSearchEncountersByDoctorAndDate_InvalidDate_Throws() {
        assertThrows(IllegalArgumentException.class, () ->
                searchController.searchEncountersByDoctorAndDate("Dr. Carl", "not-a-date").await().indefinitely()
        );
    }

    @Test
    void testSearchEncountersByDoctorAndDate_RepositoryFails() {
        Date date = Date.valueOf("2025-04-08");
        when(encounterRepository.getEncountersByDoctorAndDate("Dr. Carl", date))
                .thenReturn(Uni.createFrom().failure(new RuntimeException("Encounter fetch failed")));

        RuntimeException e = assertThrows(RuntimeException.class, () ->
                searchController.searchEncountersByDoctorAndDate("Dr. Carl", "2025-04-08").await().indefinitely()
        );

        assertEquals("Encounter fetch failed", e.getMessage());
    }
}
