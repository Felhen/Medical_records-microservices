package se.kth.felixhr.medical_records.controller;


import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import se.kth.felixhr.medical_records.model.Observation;
import se.kth.felixhr.medical_records.model.PatientDTO;
import se.kth.felixhr.medical_records.repository.ObservationRepository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("https://felixhr-frontend.cloud.cbh.kth.se")
public class ObservationController {

    @Autowired
    private ObservationRepository observationRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${patient.service.url}")
    public String patientServiceUrl;

    @GetMapping("/patient/{patientId}/observations")
    ResponseEntity<List<Observation>> getObservationsByPatientId(@PathVariable Long patientId){
        List<Observation> observations = observationRepository.findByPatientId(patientId);
        return ResponseEntity.ok(observations);
    }

    @Transactional
    @PostMapping("/{patientId}/add_observation")
    public ResponseEntity<String> addObservation(@PathVariable Long patientId, @RequestBody Map<String, Object> observationData) {
        if (observationData.get("description") == null ||
                observationData.get("date") == null ||
                observationData.get("doctorId") == null) {
            return ResponseEntity.badRequest().body("Missing required field(s)");
        }
        String observationDateStr = (String) observationData.get("date");
        String observationInfo = (String) observationData.get("description");

        Long doctorId;
        try {
            doctorId = Long.valueOf(observationData.get("doctorId").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid doctorId");
        }

        Date observationDate;
        try {
            observationDate = Date.valueOf(observationDateStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }

        String url = patientServiceUrl + "/patient/" + patientId;
        ResponseEntity<PatientDTO> response = restTemplate.getForEntity(url, PatientDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Observation observation = new Observation(observationDate, observationInfo, patientId, doctorId);
            observationRepository.save(observation);
            return ResponseEntity.status(HttpStatus.CREATED).body("Observation added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
    }

}
