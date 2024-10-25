package com.jpdr.apps.demo.webflux.user.service;

import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AppService {
  
  Flux<UserDto> getUsers();
  Mono<UserDto> getUserById(int id);
  Mono<UserDto> getUserByEmail(UserDto dto);
  Mono<UserDto> createUser(UserDto dto);
  
}
