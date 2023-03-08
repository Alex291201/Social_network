package com.example.socialnetwork.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Message extends AbstractHasID<Integer>{
    private User from;
    private User to;
    private String message;


    public Message(Integer Id,User to, User from,String message) {
        this.setId(Id);
        this.to = to;
        this.from = from;
        this.message = message;
    }

    public Message(User to, User from,String message) {
        this.to = to;
        this.from = from;
        this.message = message;
    }

    public User getFrom() {
        return from;
    }

    public void setFrom(User from) {
        this.from = from;
    }

    public User getTo() {
        return to;
    }

    public void setTo(User to) {
        this.to = to;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Message{" +
                "from=" + from +
                ", to=" + to +
                ", message='" + message + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message1 = (Message) o;
        return Objects.equals(from, message1.from) && Objects.equals(to, message1.to) && Objects.equals(message, message1.message);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, message);
    }
}
