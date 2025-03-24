package se.kth.felixhr.medical.records.search.controller;

import io.quarkus.security.Authenticated;
import io.smallrye.mutiny.Uni;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import se.kth.felixhr.medical.records.search.model.ConditionDTO;
import se.kth.felixhr.medical.records.search.model.EncounterDTO;
import se.kth.felixhr.medical.records.search.model.PatientDTO;
import se.kth.felixhr.medical.records.search.repository.EncounterRepository;
import se.kth.felixhr.medical.records.search.repository.PatientRepository;
import se.kth.felixhr.medical.records.search.repository.ConditionRepository;

import java.sql.Date;
import java.util.List;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SearchController {

    @Inject
    PatientRepository patientRepository;

    @Inject
    ConditionRepository conditionRepository;

    @Inject
    EncounterRepository encounterRepository;

    // Search patients by name
    @GET
    @Path("/patients")
    @RolesAllowed({"DOCTOR", "STAFF"})
    public Uni<List<PatientDTO>> searchPatientsByName(@QueryParam("name") String name) {
        return patientRepository.getPatientsByName(name);
    }

    // Search patients by doctor
    @GET
    @Path("/patients/by-doctor")
    @RolesAllowed({"DOCTOR", "STAFF"})
    public Uni<List<PatientDTO>> searchPatientsByDoctor(@QueryParam("doctorName") String doctorName) {
        return patientRepository.getPatientsByDoctorName(doctorName);
    }

    // Search conditions by name
    @GET
    @Path("/conditions")
    @RolesAllowed({"DOCTOR", "STAFF"})
    public Uni<List<ConditionDTO>> searchConditions(@QueryParam("conditionName") String conditionName) {
        return conditionRepository.getPatientsByCondition(conditionName);
    }

    // Search encounters by DoctorId and date
    @GET
    @Path("/encounters/by-date")
    @RolesAllowed({"DOCTOR", "STAFF"})
    public Uni<List<EncounterDTO>> searchEncountersByDoctorAndDate(@QueryParam("doctorName") String doctorName, @QueryParam("date") String date) {
        return encounterRepository.getEncountersByDoctorAndDate(doctorName, Date.valueOf(date));
    }
}


