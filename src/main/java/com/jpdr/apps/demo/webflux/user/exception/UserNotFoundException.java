package com.jpdr.apps.demo.webflux.user.exception;

public class UserNotFoundException extends RuntimeException{
  
  public UserNotFoundException(int id){
    super("User "+ id +" not found");
  }
  
  public UserNotFoundException(String email){
    super("User "+ email +" not found");
  }
  
}