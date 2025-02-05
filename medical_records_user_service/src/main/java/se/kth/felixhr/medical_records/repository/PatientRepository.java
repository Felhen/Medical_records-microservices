package se.kth.felixhr.medical_records.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import se.kth.felixhr.medical_records.model.Patient;

public interface PatientRepository extends JpaRepository<Patient, Long> {
}
