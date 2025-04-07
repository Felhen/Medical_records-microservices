package se.kth.felixhr.medical_records.controller;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.felixhr.medical_records.model.Condition;
import se.kth.felixhr.medical_records.model.PatientDTO;
import se.kth.felixhr.medical_records.repository.ConditionRepository;
import org.springframework.web.client.RestTemplate;

import java.sql.Date;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class ConditionController {

    @Autowired
    private ConditionRepository conditionRepository;

    @Autowired
    private RestTemplate restTemplate; // for making HTTP calls to patient service

    @Value("${patient.service.url}")
    private String patientServiceUrl;

    @GetMapping("/patient/{patient_id}/conditions")
    ResponseEntity<List<Condition>> getConditionsByPatientId(@PathVariable Long patient_id){
        List<Condition> conditions = conditionRepository.findByPatientId(patient_id);
        return ResponseEntity.ok(conditions);
    }

    @Transactional
    @PostMapping("/{patientId}/add_condition")
    public ResponseEntity<String> addCondition(@PathVariable Long patientId, @RequestBody Map<String, Object> conditionData) {
        String conditionName = (String) conditionData.get("name");
        String conditionDateStr = (String) conditionData.get("date");
        String conditionInfo = (String) conditionData.get("description");

        // Parse doctorId string into long
        Long doctorId =  Long.valueOf(conditionData.get("doctorId").toString());

        // Parse encounter_date string into a Date object
        Date conditionDate = Date.valueOf(conditionDateStr);

        // Fetch the patient by ID
        String url = patientServiceUrl + "/patient/" + patientId;
        ResponseEntity<PatientDTO> response = restTemplate.getForEntity(url, PatientDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {

            Condition condition = new Condition(conditionName,  conditionInfo, conditionDate, patientId, doctorId);

            // Save the Encounter object to the database
            conditionRepository.save(condition);

            return ResponseEntity.status(HttpStatus.CREATED).body("Condition added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
    }
}
