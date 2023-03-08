package com.example.socialnetwork.domain;

public class AbstractHasID <E> implements HasID<E> {
    E Id;
    @Override
    public E getId() {
        return Id;
    }

    @Override
    public void setId(E id) {
        Id = id;
    }
}
