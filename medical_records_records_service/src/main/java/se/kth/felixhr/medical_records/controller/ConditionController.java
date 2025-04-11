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
@CrossOrigin("https://felixhr-front.cloud.cbh.kth.se")
public class ConditionController {

    @Autowired
    private ConditionRepository conditionRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Value("${patient.service.url}")
    public String patientServiceUrl;

    @GetMapping("/patient/{patient_id}/conditions")
    ResponseEntity<List<Condition>> getConditionsByPatientId(@PathVariable Long patient_id){
        List<Condition> conditions = conditionRepository.findByPatientId(patient_id);
        return ResponseEntity.ok(conditions);
    }

    @Transactional
    @PostMapping("/{patientId}/add_condition")
    public ResponseEntity<String> addCondition(@PathVariable Long patientId, @RequestBody Map<String, Object> conditionData) {
        if (conditionData.get("name") == null ||
                conditionData.get("description") == null ||
                conditionData.get("date") == null ||
                conditionData.get("doctorId") == null) {
            return ResponseEntity.badRequest().body("Missing required field(s)");
        }
        String conditionName = (String) conditionData.get("name");
        String conditionDateStr = (String) conditionData.get("date");
        String conditionInfo = (String) conditionData.get("description");

        Long doctorId;
        try {
            doctorId = Long.valueOf(conditionData.get("doctorId").toString());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid doctorId");
        };

        Date conditionDate;
        try {
            conditionDate = Date.valueOf(conditionDateStr);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid date format");
        }

        // Fetch the patient by ID
        String url = patientServiceUrl + "/patient/" + patientId;
        ResponseEntity<PatientDTO> response = restTemplate.getForEntity(url, PatientDTO.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            Condition condition = new Condition(conditionName,  conditionInfo, conditionDate, patientId, doctorId);
            conditionRepository.save(condition);
            return ResponseEntity.status(HttpStatus.CREATED).body("Condition added successfully");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Patient not found");
        }
    }
}
