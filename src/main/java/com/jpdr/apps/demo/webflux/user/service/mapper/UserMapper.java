package com.jpdr.apps.demo.webflux.user.service.mapper;

import com.jpdr.apps.demo.webflux.user.model.UserData;
import com.jpdr.apps.demo.webflux.user.service.dto.UserDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring", imports = {org.apache.commons.lang3.ObjectUtils.class})
public interface UserMapper {

  UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
  
  @Mapping(target = "id", expression = "java(null)")
  @Mapping(target = "isActive", expression = "java(null)")
  @Mapping(target = "creationDate", expression = "java(null)")
  @Mapping(target = "deletionDate", expression = "java(null)")
  UserData dtoToEntity(UserDto dto);
  
  @Mapping(target = "creationDate", expression = "java(ObjectUtils.defaultIfNull(entity.getCreationDate(),\"\").toString())" )
  @Mapping(target = "deletionDate", expression = "java(ObjectUtils.defaultIfNull(entity.getDeletionDate(),\"\").toString())" )
  UserDto entityToDto(UserData entity);

}
