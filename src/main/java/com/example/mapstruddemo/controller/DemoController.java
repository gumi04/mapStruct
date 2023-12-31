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
 * Nombre de archivo: DemoController
 * Autor: 319207
 * Fecha de creación: septiembre 08, 2023
 */

package com.example.mapstruddemo.controller;

import com.example.mapstruddemo.dto.PaginationDto;
import com.example.mapstruddemo.dto.ProductosDto;
import com.example.mapstruddemo.service.FileService;
import com.example.mapstruddemo.service.ProductosService;
import java.io.ByteArrayInputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class DemoController {

  @Autowired
  private ProductosService service;

  @Autowired
  private FileService fileService;

  @GetMapping(value = "/producto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductosDto> getProductoById(@PathVariable("id") Integer id) {
    return ResponseEntity.ok(service.getProducById(id));
  }

  @GetMapping(value = "/producto", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<PaginationDto> getProductoById(@PageableDefault(size = 10, page = 0, sort = {"id",}, direction = Sort.Direction.DESC) Pageable pageable) {
    return ResponseEntity.ok(service.pagination(pageable));
  }


//  @GetMapping(value = "/producto", produces = MediaType.APPLICATION_JSON_VALUE)
//  public ResponseEntity<?> getProductoById(@PageableDefault(size = 10, page = 0, sort = {"id", "name"}, direction = Sort.Direction.DESC) @SortDefault.SortDefaults({
//          @SortDefault(sort = "id", direction = Sort.Direction.DESC),
//          @SortDefault(sort = "name", direction = Sort.Direction.ASC)
//  }) Pageable pageable){
//    return ResponseEntity.ok(service.pagination(pageable));
//  }

  @GetMapping(value = "/csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> getFakeData() {
    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=archivo-test.csv")
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .cacheControl(CacheControl.noCache())
            .body(fileService.createCsv());
  }

  @GetMapping(value = "/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<InputStreamResource> getFakeDataExcel() {

    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=archivo.xlsx")
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .cacheControl(CacheControl.noCache())
            .body(new InputStreamResource(fileService.createExcel()));
  }
}
