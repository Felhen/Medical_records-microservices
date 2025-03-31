package se.kth.felixhr.medical_records.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.felixhr.medical_records.model.Role;
import se.kth.felixhr.medical_records.model.User;
import se.kth.felixhr.medical_records.repository.UserRepository;

import java.util.Map;


@RestController
@CrossOrigin("http://localhost:3000")
@RequestMapping("/login")
public class LoginController {

    @Autowired
    private UserRepository userRepository;

    @PostMapping
    public ResponseEntity<Object> loginUser(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("pass");

        User user = userRepository.findByUsername(username);

        if (user != null && user.getEmail().equals(password)) {
            String responseMessage = "Login successful for user: " + username + " Role: " + user.getRole() + " User ID: " + user.getId();

            // Check if the user is a patient
            if (user.getRole() == Role.PATIENT && user.getPatient() != null) {
                responseMessage += " Patient ID: " + user.getPatient().getId();
            } else {
                responseMessage += " Patient ID: " + 0;
            }
            return ResponseEntity.ok().body(responseMessage);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }
}






