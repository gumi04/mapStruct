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
 * Nombre de archivo: ErrorMessageDefaultDto
 * Autor: anonimo
 * Fecha de creación: septiembre 08, 2023
 */

package com.example.demomap.dto;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

/**
 * The class Error message default.
 */
@Getter
@Setter
public class ErrorMessageDefaultDto {


  /**
   * The status of the response.
   */
  private String status;

  /**
   * The status code of the response.
   */
  private String statusCode;

  /**
   * The Message of the exception.
   */
  private String message;

  /**
   * The Date time.
   */
  private LocalDateTime dateTime;

  /**
   * Instantiates a new Error message default dto.
   */
  public ErrorMessageDefaultDto() {

  }


  /**
   * Instantiates a new Error message default.
   *
   * @param status     the status
   * @param statusCode the status code
   * @param message    the message
   */
  public ErrorMessageDefaultDto(String status, String statusCode, String message) {
    this.status = status;
    this.statusCode = statusCode;
    this.message = message;
    this.dateTime = LocalDateTime.now();
  }

}
