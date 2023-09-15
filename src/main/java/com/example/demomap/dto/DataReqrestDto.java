/*
 *                     GNU GENERAL PUBLIC LICENSE
 *                        Version 3, 29 June 2007
 *
 *  Copyright (C) 2007 Free Software Foundation, Inc. <https://fsf.org/>
 *  Everyone is permitted to copy and distribute verbatim copies
 *  of this license document, but changing it is not allowed.
 *
 *                             Preamble
 *
 *   The GNU General Public License is a free, copyleft license for
 * software and other kinds of works.
 *
 *   The licenses for most software and other practical works are designed
 * to take away your freedom to share and change the works.  By contrast,
 * the GNU General Public License is intended to guarantee your freedom to
 * share and change all versions of a program--to make sure it remains free
 * software for all its users.  We, the Free Software Foundation, use the
 * GNU General Public License for most of our software; it applies also to
 * any other work released this way by its authors.  You can apply it to
 * your programs, too.
 *
 * Nombre de archivo: DataReqrestDto
 * Autor: anonimo
 * Fecha de creación: septiembre 13, 2023
 */

package com.example.demomap.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * The type Data reqrest dto.
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DataReqrestDto {
  /**
   * The Id.
   */
  private Integer id;
  /**
   * The Email.
   */
  private String email;
  /**
   * The First name.
   */
  @JsonProperty(value = "first_name")
  private String firstName;
  /**
   * The Last name.
   */
  @JsonProperty(value = "last_name")
  private String lastName;
  /**
   * The Avatar.
   */
  private String avatar;
}
