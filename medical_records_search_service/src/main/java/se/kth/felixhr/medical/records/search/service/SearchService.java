package se.kth.felixhr.medical.records.search.service;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import se.kth.felixhr.medical.records.search.model.ConditionDTO;
import se.kth.felixhr.medical.records.search.model.EncounterDTO;
import se.kth.felixhr.medical.records.search.model.PatientDTO;
import se.kth.felixhr.medical.records.search.repository.ConditionRepository;
import se.kth.felixhr.medical.records.search.repository.EncounterRepository;
import se.kth.felixhr.medical.records.search.repository.PatientRepository;

import java.sql.Date;
import java.util.List;

@ApplicationScoped
public class SearchService {

    @Inject
    PatientRepository patientRepository;

    @Inject
    ConditionRepository conditionRepository;

    @Inject
    EncounterRepository encounterRepository;


    // Search conditions by name
    public Uni<List<ConditionDTO>> searchConditionsByName(String conditionName) {
        return conditionRepository.getConditionsByName(conditionName);
    }

    // Search patients by name
    public Uni<List<PatientDTO>> searchPatientsByName(String firstName, String lastName) {
        return patientRepository.getPatientsByName(firstName, lastName);
    }

    // Search patients assigned to a doctor
    public Uni<List<PatientDTO>> searchPatientsByDoctor(Long doctorId) {
        return patientRepository.getPatientsByDoctorEncounters(doctorId);
    }

    public Uni<List<EncounterDTO>> searchEncountersByDoctor(Long doctorId) {
        return encounterRepository.getEncountersByDoctor(doctorId);
    }

    public Uni<List<EncounterDTO>> searchEncountersByDoctorAndDate(Long doctorId, Date date) {
        return encounterRepository.getEncountersByDoctorAndDate(doctorId, date);
    }

}


