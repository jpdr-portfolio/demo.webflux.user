package com.jpdr.apps.demo.webflux.user.util;

import com.jpdr.apps.demo.webflux.commons.caching.CacheHelper;
import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;

import static com.jpdr.apps.demo.webflux.user.util.TestDataGenerator.getUserDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.DisplayName.class)
class CacheHelperTest {
  
  private CacheHelper cacheHelper;
  
  @Mock
  private CacheManager cacheManager;
  
  @Mock
  private Cache cache;
  
  @BeforeEach
  void beforeEach(){
    this.cacheHelper = new CacheHelper(this.cacheManager);
  }
  
  
  @Test
  @DisplayName("OK - Put")
  void givenDataWhenPutThenReturnData(){
    UserDto data = getUserDto();
    
    when(this.cacheManager.getCache(anyString())).thenReturn(this.cache);
    
    UserDto putData = this.cacheHelper.put(      "cache", 1, data);
    assertEquals(data,putData);
  }
  
}
