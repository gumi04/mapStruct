package com.example.demomap.service;

import com.example.demomap.dto.PlatziCreateUserDto;
import com.example.demomap.dto.UserPlatziDto;

public interface PlatziHttpService {

  UserPlatziDto getUserById(Integer id);

  UserPlatziDto save(PlatziCreateUserDto user);
}
