package com.jpdr.apps.demo.webflux.user.util;

import com.jpdr.apps.demo.webflux.user.model.UserData;
import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Stream;

public class TestDataGenerator {
  
  public static final String NAME = "John Smith";
  public static final String EMAIL = "johnsmith@mail.com";
  public static final String ADDRESS = "123 Street, City, State";
  public static final String BIRTH_DATE = "1970-01-01";
  public static final String GENDER = "Female";
  public static final String CREATION_DATE = "2024-10-14T10:39:45.732446-03:00";
  
  public static UserDto getNewUserDto(){
    return UserDto.builder()
      .id(null)
      .name(NAME)
      .email(EMAIL)
      .address(ADDRESS)
      .birthDate(BIRTH_DATE)
      .gender(GENDER)
      .creationDate(null)
      .deletionDate(null)
      .isActive(null)
      .build();
  }
  
  public static UserDto getUserDto(){
    return getUserDto(1);
  }
  
  public static List<UserDto> getUsers(){
    return Stream.iterate(1, n -> n + 1)
      .limit(3)
      .map(TestDataGenerator::getUserDto)
      .toList();
  }
  
  public static UserDto getUserDto(int id){
    return UserDto.builder()
      .id(id)
      .name(NAME)
      .email(EMAIL)
      .address(ADDRESS)
      .birthDate(BIRTH_DATE)
      .gender(GENDER)
      .isActive(true)
      .creationDate(CREATION_DATE)
      .deletionDate(null)
      .build();
  }
  
  public static List<UserData> getUsersData(){
    return Stream.iterate(1, n -> n + 1)
      .limit(3)
      .map(TestDataGenerator::getUserData)
      .toList();
  }
  
  public static UserData getUserData(){
    return  getUserData(1);
  }
  
  public static UserData getUserData(int id){
    return UserData.builder()
      .id(id)
      .name(NAME)
      .email(EMAIL)
      .address(ADDRESS)
      .birthDate(LocalDate.parse(BIRTH_DATE))
      .gender(GENDER)
      .isActive(true)
      .creationDate(OffsetDateTime.parse(CREATION_DATE))
      .deletionDate(null)
      .build();
  }
  

  
}
