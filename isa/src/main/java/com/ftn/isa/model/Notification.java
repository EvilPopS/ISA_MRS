package com.ftn.isa.model;


import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class Notification {

    @Id
    @SequenceGenerator(name = "my_seq_gen_notification", sequenceName = "my_seq_gen_notification", initialValue = 1, allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "my_seq_gen_notification")
    private Long id;

    @Column(name = "message", nullable = false)
    private String message;

    @Column(name = "is_answered", nullable = false)
    private boolean isAnswered;

    @Column(name = "sent_time", nullable = false)
    private LocalDateTime sentTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAnswered() {
        return isAnswered;
    }

    public void setAnswered(boolean answered) {
        isAnswered = answered;
    }

    public LocalDateTime getSentTime() {
        return sentTime;
    }

    public void setSentTime(LocalDateTime sentTime) {
        this.sentTime = sentTime;
    }
}
