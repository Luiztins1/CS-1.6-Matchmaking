package com.unnamed.matchmaking.cs16_matchmaking.exceptions;

public class LoginNotFoundException extends RuntimeException{
    public LoginNotFoundException(String message){
        super(message);
    }
}
