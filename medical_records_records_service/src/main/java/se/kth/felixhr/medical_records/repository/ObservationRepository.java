package se.kth.felixhr.medical_records.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.kth.felixhr.medical_records.model.Condition;
import se.kth.felixhr.medical_records.model.Observation;

import java.util.List;

public interface ObservationRepository extends JpaRepository<Observation, Long> {
    @Query(value = "SELECT * FROM t_observation WHERE patient_id = :patient_id",
            nativeQuery = true)
    List<Observation> findByPatientId(Long patient_id);
}
