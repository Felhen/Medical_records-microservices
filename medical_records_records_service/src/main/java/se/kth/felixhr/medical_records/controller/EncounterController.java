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
@CrossOrigin("https://felixhr-frontend.cloud.cbh.kth.se")
public class EncounterController {

    @Autowired
    private EncounterRepository encounterRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${patient.service.url}")
    public String patientServiceUrl;

   @GetMapping("/patient/{patientId}/encounters")
    ResponseEntity<List<Encounter>> getEncountersByPatientId(@PathVariable Long patientId){
        List<Encounter> encounters = encounterRepository.findByPatientId(patientId);
        return ResponseEntity.ok(encounters);
    }

    @Transactional
    @PostMapping("/{patientId}/add_encounter")
    public ResponseEntity<String> addEncounter(@PathVariable Long patientId, @RequestBody Map<String, Object> encounterData) {
        if (encounterData.get("description") == null ||
                encounterData.get("date") == null ||
                encounterData.get("doctorId") == null) {
            return ResponseEntity.badRequest().body("Missing required field(s)");
        }
        String encounterDateStr = (String) encounterData.get("date");
        String encounterInfo = (String) encounterData.get("description");

        Long doctorId;
        try {
            doctorId = Long.valueOf(encounterData.get("doctorId").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid doctorId");
        }

        Date encounterDate;
        try {
            encounterDate = Date.valueOf(encounterDateStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }

        String url = patientServiceUrl + "/patient/" + patientId;
        ResponseEntity<PatientDTO> response = restTemplate.getForEntity(url, PatientDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Encounter encounter = new Encounter(encounterDate, encounterInfo, patientId, doctorId);
            encounterRepository.save(encounter);
            return ResponseEntity.status(HttpStatus.CREATED).body("Encounter added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
    }
}
