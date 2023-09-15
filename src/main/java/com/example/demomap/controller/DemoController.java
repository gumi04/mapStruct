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
 * Autor: anonimo
 * Fecha de creación: septiembre 08, 2023
 */

package com.example.demomap.controller;




import com.example.demomap.dto.*;
import com.example.demomap.service.PlatziHttpService;
import com.example.demomap.service.ReqresHttpService;
import com.example.demomap.service.FileService;
import com.example.demomap.service.ProductosService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type Demo controller.
 */
@RestController
@RequestMapping("/api")
public class DemoController {

  /**
   * The Service.
   */
  @Autowired
  private ProductosService service;

  /**
   * The File service.
   */
  @Autowired
  private FileService fileService;

  /**
   * The Clientes http service.
   */
  @Autowired
  private ReqresHttpService clientesHttpService;

  /**
   * The Platzi http service.
   */
  @Autowired
  private PlatziHttpService platziHttpService;

  /**
   * Gets producto by id.
   *
   * @param id the id
   * @return the producto by id
   */
  @GetMapping(value = "/producto/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ProductosDto> getProductoById(@PathVariable("id") Integer id) {
    return ResponseEntity.ok(service.getProducById(id));
  }


  /**
   * Gets producto by id.
   *
   * @param pageable the pageable
   * @return the producto by id
   */
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

  /**
   * Gets fake data.
   *
   * @return the fake data
   */
  @GetMapping(value = "/csv", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<byte[]> getFakeData() {
    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=archivo-test.csv")
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .cacheControl(CacheControl.noCache())
            .body(fileService.createCsv());
  }

  /**
   * Gets fake data excel.
   *
   * @return the fake data excel
   */
  @GetMapping(value = "/excel", produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
  public ResponseEntity<InputStreamResource> getFakeDataExcel() {

    return ResponseEntity
            .ok()
            .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=archivo.xlsx")
            .contentType(MediaType.parseMediaType("application/octet-stream"))
            .cacheControl(CacheControl.noCache())
            .body(new InputStreamResource(fileService.createExcel()));
  }

  /**
   * Gets service reqres.
   *
   * @param id the id
   * @return the service reqres
   */
  @GetMapping(value = "reqres/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<ReqresResponseDto> getServiceReqres(@PathVariable("id") Integer id) {
    return ResponseEntity.ok(clientesHttpService.consumirServicio(id));
  }

  /**
   * Gets user platzi.
   *
   * @param id the id
   * @return the user platzi
   */
  @GetMapping(value = "/platzi/user/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserPlatziDto> getUserPlatzi(@PathVariable("id") Integer id) {
    return ResponseEntity.ok(platziHttpService.getUserById(id));
  }

  /**
   * Save user platzi response entity.
   *
   * @param userDto the user dto
   * @return the response entity
   */
  @PostMapping(value = "/platzi/user", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<UserPlatziDto> saveUserPlatzi(@RequestBody PlatziCreateUserDto userDto) {
    return ResponseEntity.ok(platziHttpService.save(userDto));
  }


  /**
   * Load file response entity.
   *
   * @param file the file
   * @return the response entity
   */
  @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<CsvDto>> loadFile(@RequestParam(value = "file") MultipartFile file) {
    return ResponseEntity.ok(fileService.readCsv(file));
  }

  /**
   * Load file ex response entity.
   *
   * @param file the file
   * @return the response entity
   */
  @PostMapping(value = "/fileEx", produces = MediaType.APPLICATION_JSON_VALUE)
  public ResponseEntity<List<DataExcelDto>> loadFileEx(@RequestParam(value = "file") MultipartFile file) {
    return ResponseEntity.ok(fileService.readExcel(file));
  }

  /**
   * Gets uuid.
   *
   * @return the uuid
   */
  @GetMapping(value = "/uuid", produces = MediaType.TEXT_PLAIN_VALUE)
  public ResponseEntity<String> getUuid() {

    return ResponseEntity
            .ok()
            .body(UUID.randomUUID().toString());
  }


}
