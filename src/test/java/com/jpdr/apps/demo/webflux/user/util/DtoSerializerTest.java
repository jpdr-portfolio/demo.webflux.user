package com.jpdr.apps.demo.webflux.user.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jpdr.apps.demo.webflux.commons.caching.DtoSerializer;
import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.jpdr.apps.demo.webflux.user.util.TestDataGenerator.getUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class DtoSerializerTest {
  
  private DtoSerializer<UserDto> serializer;
  private ObjectMapper objectMapper;
  
  @BeforeEach
  void beforeEach(){
    this.objectMapper = new ObjectMapper();
    this.serializer = new DtoSerializer<>(this.objectMapper,UserDto.class);
  }
  
  @Test
  @DisplayName("OK - Serialize and Deserialize User as bytes")
  void givenDataWhenSerializeThenReturnDataBytes(){
    UserDto data = getUserDto();
    byte[] serializedData = this.serializer.serialize(data);
    UserDto deserializedData = this.serializer.deserialize(serializedData);
    assertEquals(data, deserializedData);
  }
  
  @Test
  @DisplayName("OK - Serialize and Deserialize User as String")
  void givenDataWhenSerializeThenReturnDataString(){
    UserDto data = getUserDto();
    String serializedData = this.serializer.toString(data);
    UserDto deserializedData = this.serializer.fromString(serializedData);
    assertEquals(data, deserializedData);
  }
  
}
