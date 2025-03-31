package se.kth.felixhr.medical_records.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.felixhr.medical_records.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
    User findByKeycloakId(String keycloakId);
}
