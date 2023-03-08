package com.example.socialnetwork.domain.validators;
import com.example.socialnetwork.domain.User;
import com.example.socialnetwork.exceptions.ValidationException;

import java.util.Objects;

public class UserValidator implements Validator<User>{
    public void validate(User user)throws ValidationException
    {
        String message="";
        if(Objects.equals(user.getUsername(), ""))
            message+="Username can not be empty.\n";
        if(Objects.equals(user.getEmail(), ""))
            message+="Email can not be empty.\n";
        if(Objects.equals(user.getPassword(), ""))
            message+="Password can not be empty.\n";
        if(!message.isEmpty()){
            throw new ValidationException(message);
        }
    }
}