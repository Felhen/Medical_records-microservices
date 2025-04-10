package se.kth.felixhr.medical_records.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.felixhr.medical_records.model.Patient;
import se.kth.felixhr.medical_records.model.Role;
import se.kth.felixhr.medical_records.model.User;
import se.kth.felixhr.medical_records.repository.UserRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@CrossOrigin("https://felixhr-frontend.cloud.cbh.kth.se")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/user")
    User newUser(@RequestBody User newUser){
        return userRepository.save(newUser);
    }

    @GetMapping("/users")
    List<User> getAllUsers(){
        return userRepository.findAll();
    }

    @GetMapping("/user/by-keycloak-id/{keycloakId}")
    public ResponseEntity<Map<String, Object>> getUserByKeycloakId(@PathVariable String keycloakId) {
        User user = userRepository.findByKeycloakId(keycloakId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        Map<String, Object> response = new HashMap<>();
        response.put("userId", user.getId());
        response.put("role", user.getRole().toString());

        if (Role.PATIENT.equals(user.getRole())) {
            Patient patient = user.getPatient();
            if (patient != null) {
                response.put("patientId", patient.getId());
            }
        }

        System.out.println(response);
        return ResponseEntity.ok(response);
    }
}

