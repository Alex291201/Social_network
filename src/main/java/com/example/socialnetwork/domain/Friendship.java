package com.example.socialnetwork.domain;

import java.time.LocalDate;
import java.util.Objects;

public class Friendship extends AbstractHasID<Integer>{
    private User user1;
    private User user2;
    private LocalDate friendsfrom;

    public Friendship(Integer Id,User user1, User user2) {
        this.setId(Id);
        this.user1 = user1;
        this.user2 = user2;
        this.friendsfrom = LocalDate.now();
    }

    public Friendship(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
        this.friendsfrom = LocalDate.now();
    }


    public User getUser1() {
        return user1;
    }

    public void setUser1(User user1) {
        this.user1 = user1;
    }

    public User getUser2() {
        return user2;
    }

    public void setUser2(User user2) {
        this.user2 = user2;
    }

    public LocalDate getFriendsfrom() {
        return friendsfrom;
    }

    public void setFriendsfrom(LocalDate friendsfrom) {
        this.friendsfrom = friendsfrom;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Friendship that = (Friendship) o;
        return Objects.equals(user1, that.user1) && Objects.equals(user2, that.user2) && Objects.equals(friendsfrom, that.friendsfrom);
    }

    @Override
    public String toString() {
        return "Friendship{" +
                "user1=" + user1 +
                ", user2=" + user2 +
                ", friendsfrom=" + friendsfrom +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2, friendsfrom);
    }
}
