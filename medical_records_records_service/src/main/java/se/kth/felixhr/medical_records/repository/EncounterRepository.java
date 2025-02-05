package se.kth.felixhr.medical_records.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.kth.felixhr.medical_records.model.Condition;
import se.kth.felixhr.medical_records.model.Encounter;

import java.util.List;

public interface EncounterRepository extends JpaRepository<Encounter, Long> {
    @Query(value = "SELECT * FROM t_encounter WHERE patient_id = :patient_id",
            nativeQuery = true)
    List<Encounter> findByPatientId(Long patient_id);
}
