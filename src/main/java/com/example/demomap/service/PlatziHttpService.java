package com.example.demomap.service;

import com.example.demomap.dto.PlatziCreateUserDto;
import com.example.demomap.dto.UserPlatziDto;

/**
 * The interface Platzi http service.
 */
public interface PlatziHttpService {

  /**
   * Gets user by id.
   *
   * @param id the id
   * @return the user by id
   */
  UserPlatziDto getUserById(Integer id);

  /**
   * Save user platzi dto.
   *
   * @param user the user
   * @return the user platzi dto
   */
  UserPlatziDto save(PlatziCreateUserDto user);
}
