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
 * Nombre de archivo: ControllerExceptionHandler.java
 * Autor: gumaro
 * Fecha de creación: Ago 08, 2023
 */

package com.example.demomap.exceptions;


import com.example.demomap.dto.ErrorMessageDefaultDto;
import java.nio.file.AccessDeniedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * The class Controller exception handler.
 */
@ControllerAdvice
public class ExceptionHandlerController {

  /**
   * The constant LOGGER.
   */
  private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerController.class);
  /**
   * The constant MESSAGE_FAILED.
   */
  private static final String MESSAGE_FAILED = "FAILED";

  /**
   * Not found request error exception.
   *
   * @param exception the exception generated
   * @return The ErrorMessageDefault dto with the error details
   */
  @ResponseStatus(HttpStatus.NOT_FOUND)
  @ExceptionHandler({HttpClientErrorException.class})
  @ResponseBody
  public ErrorMessageDefaultDto notFoundRequest(Exception exception) {
    return new ErrorMessageDefaultDto(
            MESSAGE_FAILED,
            String.valueOf(HttpStatus.NOT_FOUND.value()),
            exception.getMessage());
  }

  /**
   * Bad request error exception.
   *
   * @param exception the exception generated
   * @return The ErrorMessageDefault dto with the error details
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler({
          HttpRequestMethodNotSupportedException.class,
          MissingPathVariableException.class,
          MissingRequestHeaderException.class,
          MissingServletRequestParameterException.class,
          MethodArgumentTypeMismatchException.class,
          HttpMessageNotReadableException.class})
  @ResponseBody
  public ErrorMessageDefaultDto badRequest(Exception exception) {
    LOGGER.info(exception.getMessage());
    return new ErrorMessageDefaultDto(
            MESSAGE_FAILED,
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            exception.getMessage());
  }

  /**
   * Bad request error validation exception.
   *
   * @param exception the exception generated
   * @return The ErrorMessageDefault dto with the error details
   */
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ExceptionHandler(MethodArgumentNotValidException.class)
  @ResponseBody
  public ErrorMessageDefaultDto badRequestValid(MethodArgumentNotValidException exception) {
    LOGGER.info(exception.getMessage());
    StringBuilder stringBuilder = new StringBuilder();
    exception.getBindingResult().getFieldErrors().forEach(error -> {
      String fielName = error.getField();
      String message = error.getDefaultMessage();
      stringBuilder.append("field " + fielName + ": error -> " + message + ", ");
    });
    return new ErrorMessageDefaultDto(
            MESSAGE_FAILED,
            String.valueOf(HttpStatus.BAD_REQUEST.value()),
            stringBuilder.toString());
  }

  /**
   * Unautorized error exception.
   *
   * @param exception the exception
   * @return the error message default dto
   */
  //TODO: agregar despues las excepciones de spring security
    /*@ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler({
    })
    @ResponseBody
    public ErrorMessageDefaultDto unautorized(MethodArgumentNotValidException exception) {
        LOGGER.info(exception.getMessage());
        return new ErrorMessageDefaultDto(
               MESSAGE_FAILED,
               String.valueOf(HttpStatus.UNAUTHORIZED.value()),
               HttpStatus.UNAUTHORIZED.getReasonPhrase());
    }*/


  /**
   * Unautorized error exception.
   *
   * @param exception the exception
   * @return the error message default dto
   */
  @ResponseStatus(HttpStatus.TOO_MANY_REQUESTS)
  @ExceptionHandler({
          HttpClientErrorException.TooManyRequests.class
  })
  @ResponseBody
  public ErrorMessageDefaultDto unautorized(MethodArgumentNotValidException exception) {
    LOGGER.info(exception.getMessage());
    return new ErrorMessageDefaultDto(
            MESSAGE_FAILED,
            String.valueOf(HttpStatus.TOO_MANY_REQUESTS.value()),
            exception.getMessage());
  }


  /**
   * Forbidden request error exception.
   *
   * @param exception the exception
   * @return The ErrorMessageDefault dto with the error details
   */
  @ResponseStatus(HttpStatus.FORBIDDEN)
  @ExceptionHandler({
          AccessDeniedException.class
  })
  @ResponseBody
  public ErrorMessageDefaultDto forbiddenRequest(Exception exception) {
    LOGGER.info(exception.getMessage());
    return new ErrorMessageDefaultDto(
            MESSAGE_FAILED,
            String.valueOf(HttpStatus.FORBIDDEN.value()),
            exception.getMessage());
  }


  /**
   * Fatal error error exception
   *
   * @param exception the exception generated
   * @return The ErrorMessageDefault dto with the error details
   */
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  @ExceptionHandler({
          Exception.class,
          InternalServerException.class,
          BusinessRuleException.class
  })
  @ResponseBody
  public ErrorMessageDefaultDto fatalError(Exception exception) {
    LOGGER.info(exception.getMessage());
    return new ErrorMessageDefaultDto(
            MESSAGE_FAILED,
            String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
            exception.getMessage());
  }

}