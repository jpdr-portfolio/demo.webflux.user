package com.jpdr.apps.demo.webflux.user.controller;

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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AppController {
  
  private final AppService appService;
  
  @GetMapping("/users")
  public ResponseEntity<Flux<UserDto>> getUsers(){
    return new ResponseEntity<>(appService.getUsers(), HttpStatus.OK);
  }
  
  @GetMapping("/users/{id}")
  public ResponseEntity<Mono<UserDto>> getUserById(@PathVariable Integer id){
    return new ResponseEntity<>(appService.getUserById(id), HttpStatus.OK);
  }
  
  @PostMapping("/users")
  public ResponseEntity<Mono<UserDto>> createUser(@RequestBody UserDto userDto){
    return new ResponseEntity<>(appService.createUser(userDto), HttpStatus.CREATED);
  }
  
  @PostMapping("/users/find-by-email")
  public ResponseEntity<Mono<UserDto>> findUserByEmail(@RequestBody UserDto userDto){
    return new ResponseEntity<>(appService.getUserByEmail(userDto), HttpStatus.OK);
  }
  
  
}
