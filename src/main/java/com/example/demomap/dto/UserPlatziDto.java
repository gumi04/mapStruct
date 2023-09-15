package com.example.demomap.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type User platzi dto.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserPlatziDto {
  /**
   * The Id.
   */
  private Integer id;
  /**
   * The Email.
   */
  private String email;
  /**
   * The Password.
   */
  private String password;
  /**
   * The Name.
   */
  private String name;
  /**
   * The Role.
   */
  private String role;
  /**
   * The Avatar.
   */
  private String avatar;
  /**
   * The Creation at.
   */
  private LocalDateTime creationAt;
  /**
   * The Updated at.
   */
  private LocalDateTime updatedAt;
}
