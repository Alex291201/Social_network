package com.example.socialnetwork.domain.validators;


import com.example.socialnetwork.exceptions.ValidationException;

/**
 *
 * @param <T>
 */
public interface Validator<T> {
    void validate(T entity) throws ValidationException;
}
