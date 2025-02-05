package se.kth.felixhr.medical_records.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import se.kth.felixhr.medical_records.model.Encounter;
import se.kth.felixhr.medical_records.model.PatientDTO;
import se.kth.felixhr.medical_records.repository.EncounterRepository;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class EncounterController {

    @Autowired
    private EncounterRepository encounterRepository;

    @Autowired
    private RestTemplate restTemplate; // for making HTTP calls to patient service

    @Value("${patient.service.url}")
    private String patientServiceUrl;

   @GetMapping("/patient/{patientId}/encounters")
    ResponseEntity<List<Encounter>> getEncountersByPatientId(@PathVariable Long patientId){
        List<Encounter> encounters = encounterRepository.findByPatientId(patientId);
        return ResponseEntity.ok(encounters);
    }

    @Transactional
    @PostMapping("/{patientId}/add_encounter")
    public ResponseEntity<String> addEncounter(@PathVariable Long patientId, @RequestBody Map<String, Object> encounterData) {
        String encounterDateStr = (String) encounterData.get("date");
        String encounterInfo = (String) encounterData.get("description");

        // Parse encounter_date string into a Date object
        Date encounterDate = Date.valueOf(encounterDateStr);

        // Fetch the patient by ID
        String url = patientServiceUrl + "/patient/" + patientId;
        ResponseEntity<PatientDTO> response = restTemplate.getForEntity(url, PatientDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            PatientDTO patientDTO = response.getBody();

            // Create and save the Encounter object
            Encounter encounter = new Encounter(encounterDate, encounterInfo, patientId);

            // Save the Encounter object to the database
            encounterRepository.save(encounter);

            return ResponseEntity.status(HttpStatus.CREATED).body("Encounter added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
    }
}
