package com.unnamed.matchmaking.cs16_matchmaking.exceptions;

public class UserAuthNotFoundException extends RuntimeException{
    public UserAuthNotFoundException(String message){
        super(message);
    }
}
