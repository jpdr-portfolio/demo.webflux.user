package com.jpdr.apps.demo.webflux.user.service;

import com.jpdr.apps.demo.webflux.commons.caching.CacheHelper;
import com.jpdr.apps.demo.webflux.user.exception.UserNotFoundException;
import com.jpdr.apps.demo.webflux.user.model.UserData;
import com.jpdr.apps.demo.webflux.user.repository.UserRepository;
import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import com.jpdr.apps.demo.webflux.user.service.impl.AppServiceImpl;
import jakarta.validation.ValidationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.jpdr.apps.demo.webflux.user.util.TestDataGenerator.getNewUserDto;
import static com.jpdr.apps.demo.webflux.user.util.TestDataGenerator.getUserData;
import static com.jpdr.apps.demo.webflux.user.util.TestDataGenerator.getUserDto;
import static com.jpdr.apps.demo.webflux.user.util.TestDataGenerator.getUsersData;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class AppServiceTest {

  @InjectMocks
  private AppServiceImpl appService;
  
  @Mock
  private UserRepository userRepository;
  
  @Mock
  private CacheHelper cacheHelper;
  
  @Test
  @DisplayName("OK - Create User")
  void givenUserWhenCreateUserThenReturnUser(){
    
    UserDto requestUser = getNewUserDto();
    UserData expectedUser = getUserData();
    
    when(userRepository.save(any(UserData.class)))
      .thenAnswer(i -> {
        UserData userData = i.getArgument(0);
        userData.setId(1L);
        return Mono.just(userData);
      });
    
    StepVerifier.create(appService.createUser(requestUser))
      .assertNext(dto -> assertUser(expectedUser, dto))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("Error - Create User - Mail Not Valid")
  void givenMailNotValidUserWhenCreateUserThenReturnError(){
    
    UserDto requestUser = getNewUserDto();
    requestUser.setEmail("<>");
    
    StepVerifier.create(appService.createUser(requestUser))
      .expectError(ValidationException.class)
      .verify();
  }
  
  @Test
  @DisplayName("OK - Get users")
  void givenUsersWhenGetUsersThenReturnUsers(){
    
    List<UserData> expectedUsersData = getUsersData();
    
    Map<Long, UserData> expectedUsersMap = expectedUsersData.stream()
      .collect(Collectors.toMap(UserData::getId, Function.identity()));
    
    when(userRepository.findAllByIsActiveIsTrueOrderById())
      .thenReturn(Flux.fromIterable(expectedUsersData));
    
    StepVerifier.create(appService.getUsers())
      .assertNext(users -> {
        for(UserDto dto : users){
          assertUser(expectedUsersMap.get(dto.getId()), dto);
        }
      })
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find user by Id")
  void givenIdWhenFindUserByIdThenReturnUser(){
    
    UserData expectedUserData = getUserData();
    
    when(userRepository.findUserByIdAndIsActiveIsTrue(anyLong()))
      .thenReturn(Mono.just(expectedUserData));
    
    StepVerifier.create(appService.getUserById(1))
      .assertNext(dto -> assertUser(expectedUserData, dto))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("Error - Find user by Id")
  void givenIdWhenFindUserByIdThenReturnNotFound(){
    
    when(userRepository.findUserByIdAndIsActiveIsTrue(anyLong()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.getUserById(1))
      .expectError(UserNotFoundException.class)
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find user by Email")
  void givenEmailWhenFindUserByEmailThenReturnUser(){

    UserData expectedUserData = getUserData();
    UserDto expectedUser = getUserDto();

    when(userRepository.findUserByEmailAndIsActiveIsTrue(anyString()))
      .thenReturn(Mono.just(expectedUserData));

    StepVerifier.create(appService.getUserByEmail(expectedUser))
      .assertNext(dto -> assertUser(expectedUserData, dto))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("Error - Find by Email - Not found")
  void givenEmailWhenFindUserByEmailThenReturnNotFound(){
    
    UserDto expectedUser = getUserDto();
    
    when(userRepository.findUserByEmailAndIsActiveIsTrue(anyString()))
      .thenReturn(Mono.empty());
    
    StepVerifier.create(appService.getUserByEmail(expectedUser))
      .expectError(UserNotFoundException.class)
      .verify();
  }
  
  private static void assertUser(UserData entity, UserDto dto){
    assertEquals(entity.getId(), dto.getId());
    assertEquals(entity.getName(), dto.getName());
    assertEquals(entity.getEmail(), dto.getEmail());
    assertEquals(entity.getAddress(),dto.getAddress());
    assertTrue(dto.getIsActive());
    assertNotNull(dto.getCreationDate());
    assertTrue(dto.getDeletionDate().isBlank());
  }
  
}
