package se.kth.felixhr.medical_records.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import se.kth.felixhr.medical_records.model.Message;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    @Query(value = "SELECT * FROM t_message WHERE receiver_id = :receiver_id",
            nativeQuery = true)
    List<Message> findByReceiver_id(Long receiver_id);
}
