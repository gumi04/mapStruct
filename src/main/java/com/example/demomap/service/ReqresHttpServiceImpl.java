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
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@Log4j2
public class ReqresHttpServiceImpl implements ReqresHttpService {

  private  WebClient client;

  @Autowired
  public ReqresHttpServiceImpl(@Qualifier("reqresWebClient") WebClient client) {
    this.client = client;
  }

  @Override
  public ReqresResponseDto consumirServicio(Integer id) {
    return client
            .get()
            .uri(uriBuilder -> uriBuilder.path("/users/{id}").build(id))
              .accept(MediaType.APPLICATION_JSON)
              .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .retrieve()
            .bodyToMono(ReqresResponseDto.class).block();

  }
}
