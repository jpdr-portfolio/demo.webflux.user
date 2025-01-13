package com.jpdr.apps.demo.webflux.user.controller;

import com.jpdr.apps.demo.webflux.eventlogger.component.EventLogger;
import com.jpdr.apps.demo.webflux.user.service.AppService;
import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {
  
  private final AppService appService;
  private final EventLogger eventLogger;
  
  @GetMapping("/users")
  public Mono<ResponseEntity<List<UserDto>>> getUsers(){
    return this.appService.getUsers()
      .doOnNext(list -> this.eventLogger.logEvent("getUsers", list))
      .map(users -> new ResponseEntity<>(users, HttpStatus.OK));
  }
  
  @GetMapping("/users/{id}")
  public Mono<ResponseEntity<UserDto>> getUserById(@PathVariable Long id){
    return this.appService.getUserById(id)
      .doOnNext(user -> this.eventLogger.logEvent("getUserById", user))
      .map(user -> new ResponseEntity<>(user, HttpStatus.OK));
  }
  
  @PostMapping("/users")
  public Mono<ResponseEntity<UserDto>> createUser(@RequestBody UserDto userDto){
    return this.appService.createUser(userDto)
      .doOnNext(user -> this.eventLogger.logEvent("createUser", user))
      .map(user -> new ResponseEntity<>(user, HttpStatus.CREATED));
  }
  
  @PostMapping("/users/find-by-email")
  public Mono<ResponseEntity<UserDto>> findUserByEmail(@RequestBody UserDto userDto){
    return this.appService.getUserByEmail(userDto)
      .doOnNext(user -> this.eventLogger.logEvent("findUserByEmail", user))
      .map(user -> new ResponseEntity<>(user, HttpStatus.OK));
  }
  
  
}
