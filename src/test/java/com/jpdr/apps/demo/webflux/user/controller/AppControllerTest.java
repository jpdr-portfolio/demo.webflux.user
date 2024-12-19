package com.jpdr.apps.demo.webflux.user.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.eventlogger.component.EventLogger;
import com.jpdr.apps.demo.webflux.user.service.AppService;
import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.FluxExchangeResult;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.BodyInserters;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.jpdr.apps.demo.webflux.user.util.TestDataGenerator.getNewUserDto;
import static com.jpdr.apps.demo.webflux.user.util.TestDataGenerator.getUserDto;
import static com.jpdr.apps.demo.webflux.user.util.TestDataGenerator.getUsers;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class AppControllerTest {
  
  @Autowired
  private WebTestClient webTestClient;
  @MockBean
  private AppService appService;
  @Autowired
  private ObjectMapper objectMapper;
  @MockBean
  private EventLogger eventLogger;
  
  @Test
  @DisplayName("OK - Get users")
  void givenUsersWhenFindUsersThenReturnUsers() throws Exception{
    
    List<UserDto> expectedUsers = getUsers();
    String expectedBody = objectMapper.writeValueAsString(expectedUsers);

    when(appService.getUsers()).thenReturn(Mono.just(expectedUsers));
    
    FluxExchangeResult<String> exchangeResult = this.webTestClient.get()
      .uri("/users")
      .exchange()
      .expectHeader()
      .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
      .isOk()
      .returnResult(String.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedBody -> assertEquals(expectedBody, receivedBody))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find user by Id")
  void givenIdWhenFindUserByIdThenReturnUser(){
    
    UserDto expectedUser = getUserDto();
    when(appService.getUserById(anyInt())).thenReturn(Mono.just(expectedUser));
    
    FluxExchangeResult<UserDto> exchangeResult = this.webTestClient.get()
      .uri("/users/" + expectedUser.getId())
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(UserDto.class);

    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedUser -> assertEquals(expectedUser, receivedUser))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Find user by email")
  void givenEmailWhenFindUserByEmailThenReturnUser(){
    
    UserDto expectedUser = getUserDto();
    when(appService.getUserByEmail(any(UserDto.class))).thenReturn(Mono.just(expectedUser));
    
    FluxExchangeResult<UserDto> exchangeResult = this.webTestClient.post()
      .uri("/users/find-by-email")
      .accept(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(expectedUser))
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isOk()
      .returnResult(UserDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedUser -> assertEquals(expectedUser, receivedUser))
      .expectComplete()
      .verify();
  }
  
  @Test
  @DisplayName("OK - Create User")
  void givenUserWhenCreateUserThenReturnUser(){

    UserDto requestUser = getNewUserDto();
    UserDto expectedUser = getUserDto();
    when(appService.createUser(any(UserDto.class))).thenReturn(Mono.just(expectedUser));
    
    FluxExchangeResult<UserDto> exchangeResult = this.webTestClient.post()
      .uri("/users")
      .accept(MediaType.APPLICATION_JSON)
      .body(BodyInserters.fromValue(requestUser))
      .exchange()
      .expectHeader()
        .contentType(MediaType.APPLICATION_JSON)
      .expectStatus()
        .isCreated()
      .returnResult(UserDto.class);
    
    StepVerifier.create(exchangeResult.getResponseBody())
      .assertNext(receivedUser -> assertEquals(expectedUser, receivedUser))
      .expectComplete()
      .verify();
  }
  

}
