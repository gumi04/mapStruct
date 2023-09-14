package com.example.demomap.config;

import io.netty.channel.ChannelOption;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

@Configuration
public class WebClientConfiguration {

  @Value("${api-reqres}")
  private String reqresUrl;

  @Value("${api-platzi}")
  private String platziUrl;

  @Value("${api-reqres-timeout}")
  private int reqresTimeout;


  @Bean(name = "reqresWebClient")
  public WebClient configWebClientReqres() {
    HttpClient client = HttpClient
              .create()
              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, reqresTimeout);
    return WebClient
              .builder()
              .baseUrl(reqresUrl)
              .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
              .clientConnector(new ReactorClientHttpConnector(client))
              .build();
  }

  @Bean(name = "platziWebClient")
  public WebClient configWebClientPlatzi() {
    HttpClient client = HttpClient
              .create()
              .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, reqresTimeout);
    return WebClient
              .builder()
              .baseUrl(platziUrl)
              .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
              .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
              .clientConnector(new ReactorClientHttpConnector(client))
              .build();
  }
}
