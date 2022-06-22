package com.ftn.isa.model;


import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Version
    @Column(columnDefinition = "integer DEFAULT 0", nullable = false)
    private Long version;

    @Column(name = "is_changed",columnDefinition = "boolean DEFAULT false", nullable = false)
    private boolean isChanged;

    public Notification(String message, boolean isAnswered, String localDateTime){
        this.message = message;
        this.isAnswered = isAnswered;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        this.sentTime = LocalDateTime.parse(localDateTime, formatter);
    }

    public Notification(String message, String localDateTime){
        this.message = message;
        this.isAnswered = false;
        this.sentTime = LocalDateTime.now();
    }

    public Notification(String message){
        this.message = message;
        this.isAnswered = false;
        this.sentTime = LocalDateTime.now();
    }

    public Notification() {

    }

    public Long getVersion() {
        return version;
    }

    public boolean isChanged() {
        return isChanged;
    }

    public void setChanged(boolean changed) {
        isChanged = changed;
    }

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
