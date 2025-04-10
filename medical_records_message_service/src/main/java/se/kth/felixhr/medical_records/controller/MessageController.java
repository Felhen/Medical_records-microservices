package se.kth.felixhr.medical_records.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import se.kth.felixhr.medical_records.model.Message;
import se.kth.felixhr.medical_records.repository.MessageRepository;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin("https://felixhr-frontend.cloud.cbh.kth.se")
public class MessageController {

    @Autowired
    private MessageRepository messageRepository;

    @PostMapping("/send_message")
    public ResponseEntity<String> sendMessage(@RequestBody Map<String, Object> messageData) {
        try {
            Long senderId = Long.parseLong(messageData.get("sender").toString());
            Long receiverId = Long.parseLong((messageData.get("receiver").toString()));
            String content = messageData.get("content").toString();

            // Create a Message object with only sender ID and content
            Message message = new Message();
            message.setSender(senderId);
            message.setReceiver(receiverId);
            message.setContent(content);

            messageRepository.save(message);

            return ResponseEntity.status(HttpStatus.CREATED).body("Message sent successfully");
        } catch (NumberFormatException | NullPointerException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid message data format");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send message");
        }
    }

    @GetMapping("/{userId}/inbox")
    public ResponseEntity<List<Message>> getInboxMessages(@PathVariable Long userId) {
        System.out.println(userId);
        try {
            List<Message> inboxMessages = messageRepository.findByReceiver_id(userId); // Implement this method in your service
            return ResponseEntity.ok(inboxMessages);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}

