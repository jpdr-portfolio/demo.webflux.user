package com.jpdr.apps.demo.webflux.user.repository;

import com.jpdr.apps.demo.webflux.user.model.UserData;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<UserData, Integer> {
  
  Flux<UserData> findAllByIsActiveIsTrue();
  Mono<UserData> findUserByIdAndIsActiveIsTrue(Integer id);
  Mono<UserData> findUserByEmailAndIsActiveIsTrue(String email);
  
}