package com.jpdr.apps.demo.webflux.user.configuration;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.commons.caching.DtoSerializer;
import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@EnableCaching
@Configuration
public class CacheConfig {
  
  @Bean
  public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory,
    ObjectMapper objectMapper){
    
    ObjectMapper mapper = objectMapper.copy()
      .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    mapper.setVisibility(PropertyAccessor.ALL, Visibility.ANY);
    mapper.findAndRegisterModules();
    
    RedisSerializationContext.SerializationPair<UserDto> userDtoSerializationPair =
      RedisSerializationContext.SerializationPair.fromSerializer(
        new DtoSerializer<>(mapper, UserDto.class));
    
    RedisCacheConfiguration cacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
      .serializeValuesWith(userDtoSerializationPair);
    
    return RedisCacheManager.builder(redisConnectionFactory)
      .cacheDefaults(cacheConfiguration)
      .build();
    
  }
  
}
