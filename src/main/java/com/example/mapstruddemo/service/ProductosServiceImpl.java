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
 * Nombre de archivo: ProductosServiceImpl
 * Autor: 319207
 * Fecha de creación: septiembre 08, 2023
 */

package com.example.mapstruddemo.service;

import com.example.mapstruddemo.dto.PaginationDto;
import com.example.mapstruddemo.dto.ProductosDto;
import com.example.mapstruddemo.entity.Productos;
import com.example.mapstruddemo.exceptions.BusinessRuleException;
import com.example.mapstruddemo.mappers.ProductsMapper;
import com.example.mapstruddemo.repository.ProductosRepositorio;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductosServiceImpl implements ProductosService {

  @Autowired
  private ProductosRepositorio repositorio;

  @Autowired
  private ProductsMapper mapper;

  @Override
  public ProductosDto getProducById(Integer id) {

    Productos response = repositorio.findById(id).orElseThrow(() -> new BusinessRuleException("Product not found"));
    return mapper.toDto(response);
  }

  @Override
  public PaginationDto pagination(Pageable pageable) {
    //Pageable pageable = PageRequest.of(page, size, );
    Page<Productos> data = repositorio.paginationProducts(pageable);

    return PaginationDto.builder()
            .data(mapper.togetDtosList(data.getContent()))
            .pageNumber(data.getNumber())
            .pageSize(data.getSize())
            .lastPage(data.isLast())
            .totalElements(data.getTotalElements())
            .totalPages(data.getTotalPages())
            .firstPage(data.isFirst())
            .numberOfElements(data.getNumberOfElements())
            .build();
  }

  @Override
  public List<ProductosDto> getAllProducts() {
    return mapper.togetDtosList(repositorio.findAll());
  }
}
