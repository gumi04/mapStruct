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
 * Nombre de archivo: RandonFakeServiceImpl
 * Autor: 319207
 * Fecha de creación: septiembre 08, 2023
 */

package com.example.mapstruddemo.service;

import com.example.mapstruddemo.dto.FakeDto;
import java.util.Locale;
import net.datafaker.Faker;
import org.springframework.stereotype.Service;

@Service
public class RandonFakeServiceImpl implements RandoFakeService {
  @Override
  public FakeDto getDummyData() {
    Faker faker = new Faker(Locale.ENGLISH);
    FakeDto response = new FakeDto();
    response.setCumpleanos(faker.date());
    response.setDireccion(faker.address());
    response.setZodico(faker.zodiac().sign());
    response.setNegocios(faker.business());
    response.setFullName(faker.name());
    System.out.println(response);
    return null;
  }
}
