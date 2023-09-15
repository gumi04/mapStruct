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
 * Nombre de archivo: ProductosService
 * Autor: anonimo
 * Fecha de creación: septiembre 08, 2023
 */

package com.example.demomap.service;

import com.example.demomap.dto.PaginationDto;
import com.example.demomap.dto.ProductosDto;
import java.util.List;
import org.springframework.data.domain.Pageable;

/**
 * The interface Productos service.
 */
public interface ProductosService {

  /**
   * Gets produc by id.
   *
   * @param id the id
   * @return the produc by id
   */
  ProductosDto getProducById(Integer id);

  /**
   * Pagination pagination dto.
   *
   * @param pageable the pageable
   * @return the pagination dto
   */
  PaginationDto pagination(Pageable pageable);

  /**
   * Gets all products.
   *
   * @return the all products
   */
  List<ProductosDto> getAllProducts();
}
