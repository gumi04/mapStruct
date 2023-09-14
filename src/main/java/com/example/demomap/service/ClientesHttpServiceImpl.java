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
 * Nombre de archivo: ClientesHttpServiceImpl
 * Autor: 319207
 * Fecha de creación: septiembre 11, 2023
 */

package com.example.demomap.service;


import com.example.demomap.dto.ReqresResponseDto;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@Log4j2
public class ClientesHttpServiceImpl implements ClientesHttpService {

  private final  WebClient client;

  public ClientesHttpServiceImpl(WebClient.Builder client, @Value("${url-reqres}") String url){
    this.client = client.baseUrl(url).build();
  }

  @Override
  public ReqresResponseDto consumirServicio(Integer id) {
    log.info("test");
    return client
            .get()
            //.uri(uriBuilder -> uriBuilder.path("/users/{id}").build(id))
            .uri(uriBuilder -> uriBuilder.path("/users/2").build())
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(ReqresResponseDto.class).block();

  }
}
