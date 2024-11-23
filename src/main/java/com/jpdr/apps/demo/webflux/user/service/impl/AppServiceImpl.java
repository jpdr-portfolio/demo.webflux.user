package com.jpdr.apps.demo.webflux.user.service.impl;

import com.jpdr.apps.demo.webflux.eventlogger.component.EventLogger;
import com.jpdr.apps.demo.webflux.user.exception.UserNotFoundException;
import com.jpdr.apps.demo.webflux.user.model.UserData;
import com.jpdr.apps.demo.webflux.user.repository.UserRepository;
import com.jpdr.apps.demo.webflux.user.service.AppService;
import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import com.jpdr.apps.demo.webflux.user.service.mapper.UserMapper;
import com.jpdr.apps.demo.webflux.user.util.InputValidator;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {
  
  private final UserRepository userRepository;
  private final EventLogger eventLogger;
  
  @Override
  public Mono<List<UserDto>> getUsers() {
    log.debug("getUsers");
    return this.userRepository.findAllByIsActiveIsTrue()
      .doOnNext(userData -> log.debug(userData.toString()))
      .map(UserMapper.INSTANCE::entityToDto)
      .collectList()
      .doOnNext(list -> this.eventLogger.logEvent("getUsers", list));
  }
  
  @Override
  public Mono<UserDto> getUserById(int id) {
    log.debug("getUserById");
    return this.userRepository.findUserByIdAndIsActiveIsTrue(id)
      .switchIfEmpty(Mono.error(new UserNotFoundException(id)))
      .doOnNext(userData -> log.debug(userData.toString()))
      .map(UserMapper.INSTANCE::entityToDto)
      .doOnNext(user -> this.eventLogger.logEvent("getUserById", user));
  }
  
  @Override
  public Mono<UserDto> getUserByEmail(UserDto dto) {
    log.debug("getUserByEmail");
    return Mono.just(dto)
      .filter(userDto -> userDto != null && userDto.getEmail() != null)
      .switchIfEmpty(Mono.error(new ValidationException("Invalid email")))
      .flatMap(userDto -> this.userRepository.findUserByEmailAndIsActiveIsTrue(userDto.getEmail()))
      .switchIfEmpty(Mono.error(new UserNotFoundException(dto.getEmail())))
      .doOnNext(userData -> log.debug(userData.toString()))
      .map(UserMapper.INSTANCE::entityToDto)
      .doOnNext(user -> this.eventLogger.logEvent("getUserByEmail", user));
  }
  
  @Override
  @Transactional
  public Mono<UserDto> createUser(UserDto userDto) {
    log.debug("createUser");
    return Mono.from(validateUser(userDto))
      .map(validUser -> {
        UserData userData = UserMapper.INSTANCE.dtoToEntity(validUser);
        userData.setIsActive(true);
        userData.setCreationDate(OffsetDateTime.now());
        return userData;
      } )
      .flatMap(this.userRepository::save)
      .doOnNext(savedUserData -> log.debug(savedUserData.toString()))
      .map(UserMapper.INSTANCE::entityToDto)
      .doOnNext(user -> this.eventLogger.logEvent("createUser", user));
  }
  
  private Mono<UserDto> validateUser(UserDto userDto){
    return Mono.just(userDto)
      .filter(user -> user != null &&
        InputValidator.isValidName(user.getName()) &&
        InputValidator.isValidEmail(user.getEmail()) &&
        InputValidator.isValidName(user.getAddress()))
      .switchIfEmpty(Mono.error(new ValidationException("Invalid user data")));
  }
  
}
