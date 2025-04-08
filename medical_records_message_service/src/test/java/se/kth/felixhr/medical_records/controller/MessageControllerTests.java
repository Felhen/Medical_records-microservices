package se.kth.felixhr.medical_records.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.http.ResponseEntity;
import se.kth.felixhr.medical_records.model.Message;
import se.kth.felixhr.medical_records.repository.MessageRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MessageControllerTests {

    @InjectMocks
    private MessageController messageController;

    @Mock
    private MessageRepository messageRepository;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void sanityTest() {
        System.out.println("Sanity test ran!");
        assert true;
    }

    @Test
    public void testSendMessage_ValidData_ReturnsCreated() {
        Map<String, Object> request = new HashMap<>();
        request.put("sender", 1L);
        request.put("receiver", 2L);
        request.put("content", "Hello");

        ResponseEntity<String> response = messageController.sendMessage(request);

        assertEquals(201, response.getStatusCodeValue());
        assertEquals("Message sent successfully", response.getBody());
        verify(messageRepository, times(1)).save(any(Message.class));
    }

    @Test
    public void testSendMessage_InvalidFormat_ReturnsBadRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("sender", "abc"); // not a number
        request.put("receiver", 2L);
        request.put("content", "Hello");

        ResponseEntity<String> response = messageController.sendMessage(request);

        assertEquals(400, response.getStatusCodeValue());
        verify(messageRepository, never()).save(any());
    }

    @Test
    public void testSendMessage_MissingField_ReturnsBadRequest() {
        Map<String, Object> request = new HashMap<>();
        request.put("sender", 1L);
        // missing receiver
        request.put("content", "Hello");

        ResponseEntity<String> response = messageController.sendMessage(request);

        assertEquals(400, response.getStatusCodeValue());
        verify(messageRepository, never()).save(any());
    }

    @Test
    public void testSendMessage_Exception_ReturnsServerError() {
        Map<String, Object> request = new HashMap<>();
        request.put("sender", 1L);
        request.put("receiver", 2L);
        request.put("content", "Hello");

        doThrow(new RuntimeException("Database down")).when(messageRepository).save(any());

        ResponseEntity<String> response = messageController.sendMessage(request);

        assertEquals(500, response.getStatusCodeValue());
    }

    @Test
    public void testGetInboxMessages_ReturnsMessages() {
        Long userId = 1L;
        List<Message> messages = List.of(
                new Message(2L, userId, "Hi"),
                new Message(3L, userId, "Test")
        );

        when(messageRepository.findByReceiver_id(userId)).thenReturn(messages);

        ResponseEntity<List<Message>> response = messageController.getInboxMessages(userId);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
    }

    @Test
    public void testGetInboxMessages_Exception_ReturnsServerError() {
        Long userId = 1L;

        when(messageRepository.findByReceiver_id(userId)).thenThrow(new RuntimeException("Error"));

        ResponseEntity<List<Message>> response = messageController.getInboxMessages(userId);

        assertEquals(500, response.getStatusCodeValue());
        assertNull(response.getBody());
    }
}
