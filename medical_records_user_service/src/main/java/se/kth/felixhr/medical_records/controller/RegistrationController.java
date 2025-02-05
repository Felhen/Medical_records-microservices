package se.kth.felixhr.medical_records.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.felixhr.medical_records.model.Patient;
import se.kth.felixhr.medical_records.model.Role;
import se.kth.felixhr.medical_records.model.User;
import se.kth.felixhr.medical_records.repository.PatientRepository;
import se.kth.felixhr.medical_records.repository.UserRepository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class RegistrationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PatientRepository patientRepository;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Map<String, Object> userData) {
        String role = (String) userData.get("role");

        if (role != null && role.equals("patient")) {
            String username = (String) userData.get("username");
            String password = (String) userData.get("password");
            String firstName = (String) userData.get("firstName");
            String lastName = (String) userData.get("lastName");
            String birthdateStr = (String) userData.get("birthdate");
            Date birthdate = Date.valueOf(birthdateStr); // Convert string to java.sql.Date
            int personNumber = Integer.parseInt((String) userData.get("personNumber"));

            User user = new User(username, password, Role.PATIENT); // Assuming Role enum with PATIENT
            userRepository.save(user);

            Patient patient = new Patient(firstName, lastName, birthdate, personNumber, user);
            patientRepository.save(patient);

            return ResponseEntity.ok("User patient registered successfully");
        } else if (role != null && (role.equals("doctor") || role.equals("staff"))) {
            String username = (String) userData.get("username");
            String password = (String) userData.get("password");

            User user = new User(username, password, Role.valueOf(role.toUpperCase())); // Assuming Role enum
            userRepository.save(user);

            return ResponseEntity.ok("Doctor/Staff registered successfully");
        } else {
            return ResponseEntity.badRequest().body("Invalid role specified");
        }
    }
}


