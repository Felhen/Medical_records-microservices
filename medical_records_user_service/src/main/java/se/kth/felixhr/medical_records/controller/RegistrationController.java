package se.kth.felixhr.medical_records.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.felixhr.medical_records.model.Patient;
import se.kth.felixhr.medical_records.model.Role;
import se.kth.felixhr.medical_records.model.User;
import se.kth.felixhr.medical_records.repository.PatientRepository;
import se.kth.felixhr.medical_records.repository.UserRepository;
import se.kth.felixhr.medical_records.security.KeycloakService;

import java.sql.Date;
import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin("http://localhost:3000")
public class RegistrationController {

    @Autowired private KeycloakService keycloakService;
    @Autowired private UserRepository userRepository;
    @Autowired private PatientRepository patientRepository;

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody Map<String, Object> userData) {
        String username = (String) userData.get("username");
        String email = (String) userData.get("email");
        String password = (String) userData.get("password");
        String role = (String) userData.get("role");

        String keycloakId = keycloakService.createUserInKeycloak(username, email, password, role);
        System.out.println(keycloakId); //DEBUG
        if (keycloakId == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Keycloak user creation failed");
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setKeycloakId(keycloakId);
        user.setRole(Role.valueOf(role.toUpperCase()));
        userRepository.save(user);

        if (role.equalsIgnoreCase("PATIENT")) {
            String firstName = (String) userData.get("firstName");
            String lastName = (String) userData.get("lastName");
            String birthdateStr = (String) userData.get("birthdate");
            Date birthdate = Date.valueOf(birthdateStr);
            int personNumber = Integer.parseInt((String) userData.get("personNumber"));

            Patient patient = new Patient(firstName, lastName, birthdate, personNumber, user);
            patientRepository.save(patient);
        }

        return ResponseEntity.ok("User created successfully");
    }
}


