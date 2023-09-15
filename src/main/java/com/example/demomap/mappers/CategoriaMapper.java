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
 * Nombre de archivo: CategoriaMapper
 * Autor: anonimo
 * Fecha de creación: septiembre 08, 2023
 */

package com.example.demomap.mappers;

import com.example.demomap.dto.CategoriaDto;
import com.example.demomap.entity.Categoria;
import java.util.List;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

/**
 * The interface Categoria mapper.
 */
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CategoriaMapper {


  /**
   * To dto categoria dto.
   *
   * @param categoria the categoria
   * @return the categoria dto
   */
  @Mapping(source = "id", target = "id")
  @Mapping(source = "nombre", target = "name")
  @Mapping(source = "estatus", target = "status")
  CategoriaDto toDto(Categoria categoria);

  /**
   * To entity categoria.
   *
   * @param dto the dto
   * @return the categoria
   */
  @InheritInverseConfiguration
  Categoria toEntity(CategoriaDto dto);


  /**
   * To dto list list.
   *
   * @param categoriaList the categoria list
   * @return the list
   */
  List<CategoriaDto> toDtoList(List<Categoria> categoriaList);

  /**
   * To enttity list list.
   *
   * @param dtoList the dto list
   * @return the list
   */
  List<Categoria> toEnttityList(List<CategoriaDto> dtoList);
}
