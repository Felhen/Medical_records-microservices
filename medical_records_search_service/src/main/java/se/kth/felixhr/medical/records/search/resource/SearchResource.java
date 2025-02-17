package se.kth.felixhr.medical.records.search.resource;

import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.inject.Inject;
import se.kth.felixhr.medical.records.search.model.ConditionDTO;
import se.kth.felixhr.medical.records.search.model.EncounterDTO;
import se.kth.felixhr.medical.records.search.model.PatientDTO;
import se.kth.felixhr.medical.records.search.repository.PatientRepository;
import se.kth.felixhr.medical.records.search.repository.ConditionRepository;
import se.kth.felixhr.medical.records.search.repository.EncounterRepository;
import se.kth.felixhr.medical.records.search.service.SearchService;

import java.sql.Date;
import java.util.List;

@Path("/search")
@Produces(MediaType.APPLICATION_JSON)
@ApplicationScoped
public class SearchResource {

    @Inject
    PatientRepository patientRepository;

    @Inject
    ConditionRepository conditionRepository;

    @Inject
    SearchService searchService;

    // Search patients by name
    @GET
    @Path("/patients")
    public Uni<List<PatientDTO>> searchPatientsByName(@QueryParam("firstName") String firstName, @QueryParam("lastName") String lastName) {
        return searchService.searchPatientsByName(firstName, lastName);
    }

    // Search patients by doctor
    @GET
    @Path("/patients/by-doctor")
    public Uni<List<PatientDTO>> searchPatientsByDoctor(@QueryParam("doctorId") Long doctorId) {
        return searchService.searchPatientsByDoctor(doctorId);
    }

    // Search conditions by name
    @GET
    @Path("/conditions")
    public Uni<List<ConditionDTO>> searchConditionsByName(@QueryParam("conditionName") String conditionName) {
        return searchService.searchConditionsByName(conditionName);
    }

    // Search encounters by doctorID
    @GET
    @Path("/encounters")
    public Uni<List<EncounterDTO>> searchEncounters(@QueryParam("doctorId") Long doctorId) {
        return searchService.searchEncountersByDoctor(doctorId);
    }

    // Search encounters by DoctorId and date
    @GET
    @Path("/encounters/by-date")
    public Uni<List<EncounterDTO>> searchEncountersByDoctorAndDate(@QueryParam("doctorId") Long doctorId, @QueryParam("date") String date) {
        return searchService.searchEncountersByDoctorAndDate(doctorId, Date.valueOf(date));
    }
}


