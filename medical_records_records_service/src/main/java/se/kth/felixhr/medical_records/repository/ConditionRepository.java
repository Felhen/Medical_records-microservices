package se.kth.felixhr.medical_records.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.kth.felixhr.medical_records.model.Condition;

import java.util.List;

public interface ConditionRepository extends JpaRepository<Condition, Long> {
    @Query(value = "SELECT * FROM t_condition WHERE patient_id = :patient_id",
    nativeQuery = true)
    List<Condition> findByPatientId(Long patient_id);
}