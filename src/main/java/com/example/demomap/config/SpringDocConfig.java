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
 * Nombre de archivo: SpringDocConfig
 * Autor: 319207
 * Fecha de creación: septiembre 08, 2023
 */

package com.example.demomap.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfig {

  /**
   * Api info.
   *
   * @return the api info
   */
  @Bean
  public OpenAPI apiInfo() {
    return new OpenAPI()
              .info(new Info()
                        .title("demo")
                        .description("Java application")
                        .version("V 1.0")
                        .license(
                                  new License()
                                            .name("Apache License Version 2.0")
                                            .url("https://www.apache.org/licenses/LICENSE-2.0"))
                        .contact(new Contact()
                                  .name("test.com")
                                  .url("https://www.test.com/")
                                  .email("test@test.com")));
  }


}
