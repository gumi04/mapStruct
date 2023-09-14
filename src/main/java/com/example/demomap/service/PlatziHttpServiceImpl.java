package com.example.demomap.service;

import com.example.demomap.dto.PlatziCreateUserDto;
import com.example.demomap.dto.UserPlatziDto;
import com.example.demomap.exceptions.BusinessRuleException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Slf4j
public class PlatziHttpServiceImpl implements PlatziHttpService {

  @Autowired
  @Qualifier("platziWebClient")
  private WebClient client;


  @Override
  public UserPlatziDto getUserById(Integer id) {
    return client
              .get()
              .uri(uriBuilder -> uriBuilder.path("/users/{id}").build(id))
              .accept(MediaType.APPLICATION_JSON)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .retrieve()
              .bodyToMono(UserPlatziDto.class)
              .onErrorResume(BusinessRuleException.class, e ->
                Mono.error(new BusinessRuleException("Error al consultar el usuario".concat(e.getMessage()))))
              .block();

  }

  @Override
  public UserPlatziDto save(PlatziCreateUserDto user) {
    return client
              .post()
              .uri("/users/")
              .accept(MediaType.APPLICATION_JSON)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .body(Mono.just(user), PlatziCreateUserDto.class)
              .retrieve()
              .bodyToMono(UserPlatziDto.class).block();


  }


}
