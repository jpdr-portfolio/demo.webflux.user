package com.jpdr.apps.demo.webflux.user.service;

import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import reactor.core.publisher.Mono;

import java.util.List;

public interface AppService {
  
  Mono<List<UserDto>> getUsers();
  Mono<UserDto> getUserById(long id);
  Mono<UserDto> getUserByEmail(UserDto dto);
  Mono<UserDto> createUser(UserDto dto);
  
}
