package com.example.socialnetwork.domain;

import java.time.LocalDate;
import java.util.Objects;

public class FriendRequest extends AbstractHasID<Integer>{
    private User from;
    private User to;
    private LocalDate sentat;

    public FriendRequest(Integer Id, User from, User to) {
        this.setId(Id);
        this.from = from;
        this.to = to;
        this.sentat = LocalDate.now();
    }

    public FriendRequest(User from, User to) {
        this.from = from;
        this.to = to;
        this.sentat = LocalDate.now();
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

    public LocalDate getSentat() {
        return sentat;
    }

    public void setSentat(LocalDate sentat) {
        this.sentat = sentat;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FriendRequest that = (FriendRequest) o;
        return Objects.equals(from, that.from) && Objects.equals(to, that.to) && Objects.equals(sentat, that.sentat);
    }

    @Override
    public int hashCode() {
        return Objects.hash(from, to, sentat);
    }

    @Override
    public String toString() {
        return "FriendRequest{" +
                "form=" + from +
                ", to=" + to +
                ", sentat=" + sentat +
                '}';
    }
}
