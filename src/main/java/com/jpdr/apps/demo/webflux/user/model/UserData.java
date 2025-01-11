package com.jpdr.apps.demo.webflux.user.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDate;
import java.time.OffsetDateTime;

@Data
@Table("userdata")
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserData {
  
  @Id
  @Column("id")
  Integer id;
  @Column("name")
  String name;
  @Column("email")
  String email;
  @Column("address")
  String address;
  @Column("birth_date")
  LocalDate birthDate;
  @Column("gender")
  String gender;
  @Column("city")
  String city;
  @Column("country")
  String country;
  @Column("is_active")
  Boolean isActive;
  @Column("creation_date")
  OffsetDateTime creationDate;
  @Column("deletion_date")
  OffsetDateTime deletionDate;
}
