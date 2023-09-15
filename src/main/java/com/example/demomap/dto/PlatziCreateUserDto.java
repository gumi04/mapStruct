package com.example.demomap.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Platzi create user dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlatziCreateUserDto {

  /**
   * The Name.
   */
  private String name;
  /**
   * The Email.
   */
  private String email;
  /**
   * The Password.
   */
  private String password;
  /**
   * The Avatar.
   */
  private String avatar;

}
