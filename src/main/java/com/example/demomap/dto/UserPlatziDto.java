package com.example.demomap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPlatziDto {
  private Integer id;
  private String email;
  private String password;
  private String name;
  private String role;
  private String avatar;
  private LocalDateTime creationAt;
  private LocalDateTime updatedAt;
}
