package se.kth.felixhr.medical_records.model;

import jakarta.persistence.*;

@Entity
@Table(name  = "t_message")
public class Message {

    private Long id;
    private Long sender;
    private Long receiver;
    private String content;

    public Message(Long sender, Long receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
    }

    public Message() {
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "message_id")
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    @Column(name = "sender_id", nullable = false)
    public Long getSender() {
        return sender;
    }
    public void setSender(Long sender) {
        this.sender = sender;
    }

    @Column(name = "receiver_id", nullable = false)
    public Long getReceiver() {
        return receiver;
    }
    public void setReceiver(Long receiver) {
        this.receiver = receiver;
    }

    @Column(name = "content", nullable = false)
    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }
}
