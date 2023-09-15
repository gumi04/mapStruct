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
 * Nombre de archivo: PaginationDto
 * Autor: anonimo
 * Fecha de creación: septiembre 08, 2023
 */

package com.example.demomap.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * The type Pagination dto.
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaginationDto {

  /**
   * The Data.
   */
  private List<ProductosDto> data;
  /**
   * The Page number.
   */
  private Integer pageNumber;
  /**
   * The Page size.
   */
  private Integer pageSize;
  /**
   * The Last page.
   */
  private Boolean lastPage;
  /**
   * The Total elements.
   */
  private Long totalElements;
  /**
   * The Total pages.
   */
  private Integer totalPages;
  /**
   * The First page.
   */
  private Boolean firstPage;
  /**
   * The Number of elements.
   */
  private Integer numberOfElements;

}
