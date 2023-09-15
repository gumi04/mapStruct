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
 * Nombre de archivo: DemoControllerTest
 * Autor: anonimo
 * Fecha de creación: septiembre 13, 2023
 */

package com.example.demomap.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.demomap.dto.CsvDto;
import com.example.demomap.dto.DataExcelDto;
import com.example.demomap.dto.DataReqrestDto;
import com.example.demomap.dto.ErrorMessageDefaultDto;
import com.example.demomap.dto.FakeDto;
import com.example.demomap.dto.PaginationDto;
import com.example.demomap.dto.PlatziCreateUserDto;
import com.example.demomap.dto.ProductosDto;
import com.example.demomap.dto.ReqresResponseDto;
import com.example.demomap.dto.SupportReqrestDto;
import com.example.demomap.dto.UserPlatziDto;
import com.example.demomap.entity.Categoria;
import com.example.demomap.entity.Productos;
import com.example.demomap.exceptions.BusinessRuleException;
import com.example.demomap.repository.ProductosRepositorio;
import com.example.demomap.service.RandoFakeService;
import com.example.demomap.service.ReqresHttpService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

/**
 * The type Demo controller test.
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureWireMock(port = 0)
@AutoConfigureMockMvc
@Slf4j
@ActiveProfiles("test")
class DemoControllerTest {

  @MockBean
  private ProductosRepositorio productosRepositorio;
  @MockBean
  private RandoFakeService randoFakeService;
  @Autowired
  private ReqresHttpService clientesHttpService;

  @Autowired
  private MockMvc mvc;


  private static MockWebServer mockWebServer;
  ;

  private ObjectMapper mapper = new ObjectMapper();

  /**
   * Sets .
   *
   * @throws IOException the io exception
   */
  @BeforeAll
  static void setup() throws IOException {
    mockWebServer = new MockWebServer();
    mockWebServer.start(9999);
  }

  /**
   * Tear down.
   *
   * @throws IOException the io exception
   */
  @AfterAll
  static void tearDown() throws IOException {
    mockWebServer.shutdown();
  }

  /**
   * Gets producto by id ok.
   *
   * @throws Exception the exception
   */
  @Test
  void getProductoByIdOk() throws Exception {
    Categoria categoria = new Categoria();
    categoria.setId(1);
    categoria.setEstatus(true);
    categoria.setNombre("categoria");

    Productos producto = new Productos();
    producto.setId(1);
    producto.setName("name");
    producto.setDate(LocalDateTime.now());
    producto.setCategoria(categoria);


    Optional<Productos> productoDb = Optional.of(producto);
    Mockito.when(productosRepositorio.findById(ArgumentMatchers.anyInt())).thenReturn(productoDb);

    MvcResult result = mvc.perform(get("/api/producto/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn();

    ProductosDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            ProductosDto.class);

    assertEquals(Integer.parseInt(responseController.getId()), producto.getId());
    assertEquals(responseController.getNombre(), producto.getName());
  }

  /**
   * Gets producto by id bad.
   *
   * @throws Exception the exception
   */
  @Test
  void getProductoByIdBad() throws Exception {

    mapper.registerModule(new JavaTimeModule());
    Mockito.when(productosRepositorio.findById(ArgumentMatchers.anyInt())).thenReturn(Optional.empty());

    MvcResult result = mvc.perform(get("/api/producto/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isInternalServerError())
            .andReturn();

    ErrorMessageDefaultDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            ErrorMessageDefaultDto.class);

    assertEquals("FAILED", responseController.getStatus());
    assertEquals("A business error occurred -> Product not found.", responseController.getMessage());


  }

  /**
   * Test get producto paginationd ok.
   *
   * @throws Exception the exception
   */
  @Test
  void testGetProductoPaginationdOk() throws Exception {
    Categoria categoria = new Categoria();
    categoria.setId(1);
    categoria.setEstatus(true);
    categoria.setNombre("categoria");

    Productos producto1 = new Productos();
    producto1.setId(1);
    producto1.setName("name");
    producto1.setDate(LocalDateTime.now());
    producto1.setCategoria(categoria);

    Productos producto2 = new Productos();
    producto2.setId(12);
    producto2.setName("name2");
    producto2.setDate(LocalDateTime.now());
    producto2.setCategoria(categoria);

    List<Productos> productosList = Arrays.asList(producto1, producto2);
    Page<Productos> productosPage = new PageImpl<>(productosList);

    Mockito.when(productosRepositorio.paginationProducts(ArgumentMatchers.any())).thenReturn(productosPage);

    MvcResult result = mvc.perform(get("/api/producto")
                    .queryParam("page", "0")
                    .queryParam("size", "2")
                    .queryParam("sort", "id")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn();

    PaginationDto responseController = mapper.readValue(result.getResponse().getContentAsString(), PaginationDto.class);

    assertEquals(2, responseController.getData().size());
    assertEquals(1, responseController.getTotalPages());
    assertEquals(2, responseController.getTotalElements());

  }

  /**
   * Gets fake data.
   *
   * @throws Exception the exception
   */
  @Test
  void getFakeData() throws Exception {
    Categoria categoria = new Categoria();
    categoria.setId(1);
    categoria.setEstatus(true);
    categoria.setNombre("categoria");

    Productos producto1 = new Productos();
    producto1.setId(1);
    producto1.setName("name");
    producto1.setDate(LocalDateTime.of(2023, 9, 11, 0, 0));
    producto1.setCategoria(categoria);


    List<Productos> productosList = Arrays.asList(producto1);

    Mockito.when(productosRepositorio.findAll()).thenReturn(productosList);

    MvcResult result = mvc.perform(get("/api/csv")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn();

    //String response = "id,nombre,fecha\n1,name,2023-09-11";

    assertEquals("attachment; filename=archivo-test.csv", result.getResponse().getHeader("Content-Disposition"));
    //assertEquals(result.getResponse().getContentAsString(), response.trim());
  }

  /**
   * Gets fake data bad.
   *
   * @throws Exception the exception
   */
  @Test
  void getFakeDataBad() throws Exception {
    mapper.registerModule(new JavaTimeModule());

    Mockito.when(productosRepositorio.findAll()).thenThrow(new BusinessRuleException("fallo"));

    MvcResult result = mvc.perform(get("/api/csv")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isInternalServerError())
            .andReturn();

    ErrorMessageDefaultDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            ErrorMessageDefaultDto.class);

    assertEquals("FAILED", responseController.getStatus());
    assertEquals("A business error occurred -> Error al crear el libro csv A business error occurred -> fallo..", responseController.getMessage());
  }

  /**
   * Gets fake data excel ok.
   *
   * @throws Exception the exception
   */
  @Test
  void getFakeDataExcelOK() throws Exception {
    FakeDto dummyData = new FakeDto();
    dummyData.setStreetAddress("address");
    dummyData.setZodico("libra");
    dummyData.setFullName("name");


    List<FakeDto> dataList = Arrays.asList(dummyData);

    Mockito.when(randoFakeService.getDummyData()).thenReturn(dataList);

    MvcResult result = mvc.perform(get("/api/excel")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn();

    //String response = "id,nombre,fecha\n1,name,2023-09-11";

    assertEquals("attachment; filename=archivo.xlsx", result.getResponse().getHeader("Content-Disposition"));
  }

  /**
   * Gets fake data excel bad.
   *
   * @throws Exception the exception
   */
  @Test
  void getFakeDataExcelBad() throws Exception {
    mapper.registerModule(new JavaTimeModule());
    Mockito.when(randoFakeService.getDummyData()).thenThrow(new BusinessRuleException("fallo"));

    MvcResult result = mvc.perform(get("/api/excel")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isInternalServerError())
            .andReturn();

    //String response = "id,nombre,fecha\n1,name,2023-09-11";

    ErrorMessageDefaultDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            ErrorMessageDefaultDto.class);

    assertEquals("FAILED", responseController.getStatus());
    assertEquals("A business error occurred -> Error al crear el libro de excel A business error occurred -> fallo..", responseController.getMessage());
  }

  /**
   * Load file bad extension.
   *
   * @throws Exception the exception
   */
  @Test
  void loadFileBadExtension() throws Exception {
    mapper.registerModule(new JavaTimeModule());

    Path path = Paths.get("src/test/java/resources/esquema.json");
    byte[] archivo = IOUtils.toByteArray(this.getClass().getResourceAsStream("/files/esquema.json"));

    //Cargamos el archivo
    MockMultipartFile mockMultipartFile = new MockMultipartFile("esquema.json", "", "application/json", archivo);

    MvcResult result = mvc.perform(multipart("/api/file")
                    .file("file", mockMultipartFile.getBytes())
                    .characterEncoding("UTF-8"))
            .andExpect(status().isInternalServerError())
            .andReturn();

    ErrorMessageDefaultDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            ErrorMessageDefaultDto.class);


    assertEquals("FAILED", responseController.getStatus());
    assertEquals("A business error occurred -> El archivo no puede ser procesado.", responseController.getMessage());
  }

  /**
   * Load file ok.
   *
   * @throws Exception the exception
   */
  @Test
  void loadFileOk() throws Exception {
    mapper.registerModule(new JavaTimeModule());
    byte[] archivo = IOUtils.toByteArray(this.getClass().getResourceAsStream("/files/archivo-test.csv"));

    //Cargamos el archivo
    MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "archivo-test.csv", "text/csv", archivo);

    MvcResult result = mvc.perform(multipart("/api/file")
                    .file(mockMultipartFile)
                    .characterEncoding("UTF-8"))
            .andExpect(status().isOk())
            .andReturn();

    List<CsvDto> dataController = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<CsvDto>>() {
    });

    assertEquals(1, dataController.size());
    assertEquals(1, dataController.get(0).getId());
  }

  /**
   * Load file bad.
   *
   * @throws Exception the exception
   */
  @Test
  void loadFileBad() throws Exception {
    mapper.registerModule(new JavaTimeModule());

    byte[] archivo = IOUtils.toByteArray(this.getClass().getResourceAsStream("/files/archivo-testBadDates.csv"));

    //Cargamos el archivo
    MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "archivo-test.csv", "text/csv", archivo);

    MvcResult result = mvc.perform(multipart("/api/file")
                    .file(mockMultipartFile)
                    .characterEncoding("UTF-8"))
            .andExpect(status().isInternalServerError())
            .andReturn();


    ErrorMessageDefaultDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            ErrorMessageDefaultDto.class);

    assertEquals("FAILED", responseController.getStatus());
    assertEquals("A business error occurred -> Error al leer libro java.time.format.DateTimeParseException: Text '25/03/2023' could not be parsed at index 0.", responseController.getMessage());
  }

  /**
   * Load file ex bad extension.
   *
   * @throws Exception the exception
   */
  @Test
  void loadFileExBadExtension() throws Exception {
    mapper.registerModule(new JavaTimeModule());

    byte[] archivo = IOUtils.toByteArray(this.getClass().getResourceAsStream("/files/esquema.json"));

    //Cargamos el archivo
    MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "esquema.json", "application/json", archivo);

    MvcResult result = mvc.perform(multipart("/api/fileEx")
                    .file("file", mockMultipartFile.getBytes())
                    .characterEncoding("UTF-8"))
            .andExpect(status().isInternalServerError())
            .andReturn();

    ErrorMessageDefaultDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            ErrorMessageDefaultDto.class);

    assertEquals("FAILED", responseController.getStatus());
    assertEquals("A business error occurred -> El archivo no puede ser procesado.", responseController.getMessage());
  }

  /**
   * Load file ex ok.
   *
   * @throws Exception the exception
   */
  @Test
  void loadFileExOk() throws Exception {
    mapper.registerModule(new JavaTimeModule());

    byte[] archivo = IOUtils.toByteArray(this.getClass().getResourceAsStream("/files/test-ok.xlsx"));

    //Cargamos el archivo
    MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "test-ok.xlsx", "application/vnd.ms-excel", archivo);

    MvcResult result = mvc.perform(multipart("/api/fileEx")
                    .file(mockMultipartFile)
                    .characterEncoding("UTF-8"))
            .andExpect(status().isOk())
            .andReturn();

    List<DataExcelDto> dataController = mapper.readValue(result.getResponse().getContentAsString(), new TypeReference<List<DataExcelDto>>() {
    });

    assertEquals(1, dataController.size());
    assertEquals("Phillip Hodkiewicz", dataController.get(0).getFullName());
    assertEquals("43122 Deidra Plaza", dataController.get(0).getStreetAddress());
    assertEquals("Aries", dataController.get(0).getZodico());
  }

  /**
   * Load file ex bad.
   *
   * @throws Exception the exception
   */
  @Test
  void loadFileExBad() throws Exception {
    mapper.registerModule(new JavaTimeModule());
    byte[] archivo = IOUtils.toByteArray(this.getClass().getResourceAsStream("/files/archivo.xlsx"));

    //Cargamos el archivo
    MockMultipartFile mockMultipartFile = new MockMultipartFile("file", "archivo.xlsx", "application/vnd.ms-excel", archivo);

    MvcResult result = mvc.perform(multipart("/api/fileEx")
                    .file(mockMultipartFile)
                    .characterEncoding("UTF-8"))
            .andExpect(status().isInternalServerError())
            .andReturn();

    ErrorMessageDefaultDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            ErrorMessageDefaultDto.class);

    assertEquals("FAILED", responseController.getStatus());
    assertEquals("A business error occurred -> o se puede leer el libro Cannot get a NUMERIC value from a STRING cell.", responseController.getMessage());
  }

  /**
   * Gets uuid.
   *
   * @throws Exception the exception
   */
  @Test
  void getUuid() throws Exception {
    MvcResult result = mvc.perform(get("/api/uuid")
                    .contentType(MediaType.TEXT_PLAIN_VALUE))
            .andExpect(status().isOk()).andReturn();

    String responseController = result.getResponse().getContentAsString();
    log.info(responseController);
    assertNotNull(responseController);
  }


  /**
   * Gets service.
   *
   * @throws Exception the exception
   */
  @Test
  void getService() throws Exception {
    ReqresResponseDto responeService = ReqresResponseDto
            .builder()
            .data(DataReqrestDto
                    .builder()
                    .id(2)
                    .email("janet.weaver@reqres.in")
                    .firstName("raul")
                    .lastName("Weaver")
                    .avatar("https://reqres.in/img/faces/2-image.jpg")
                    .build())
            .support(SupportReqrestDto.builder()
                    .url("https://reqres.in/#support-heading")
                    .text("To keep ReqRes free, contributions towards server costs are appreciated!")
                    .build())
            .build();

    mockWebServer.enqueue(new MockResponse()
            .setBody(mapper.writeValueAsString(responeService))
            .addHeader("Content-Type", "application/json").setResponseCode(200));


    MvcResult result = mvc.perform(get("/api/reqres/user/1")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn();

    ReqresResponseDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            ReqresResponseDto.class);

    assertEquals(responeService.getData().getId(), responseController.getData().getId());
    assertEquals(responeService.getData().getFirstName(), responseController.getData().getFirstName());

  }

  /**
   * Gets user platzi.
   *
   * @throws Exception the exception
   */
  @Test
  void getUserPlatzi() throws Exception {
    mapper.registerModule(new JavaTimeModule());
    UserPlatziDto responseService = new UserPlatziDto();
    responseService.setAvatar("avatar");
    responseService.setName("name");
    responseService.setId(1);
    responseService.setEmail("email");
    responseService.setRole("role");
    responseService.setPassword("password");
    responseService.setCreationAt(LocalDateTime.now());
    responseService.setUpdatedAt(LocalDateTime.now());


    mockWebServer.enqueue(new MockResponse()
            .setBody(mapper.writeValueAsString(responseService))
            .addHeader("Content-Type", "application/json").setResponseCode(200));


    MvcResult result = mvc.perform(get("/api/platzi/user/100")
                    .contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andReturn();

    UserPlatziDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            UserPlatziDto.class);

    assertEquals(responseService.getAvatar(), responseController.getAvatar());
    assertEquals(responseService.getId(), responseController.getId());
  }

  /**
   * Save user platzi.
   *
   * @throws Exception the exception
   */
  @Test
  void saveUserPlatzi() throws Exception {
    mapper.registerModule(new JavaTimeModule());
    PlatziCreateUserDto user = new PlatziCreateUserDto();
    user.setAvatar("avatar");
    user.setEmail("email");
    user.setPassword("password");
    user.setName("name");

    UserPlatziDto responseService = new UserPlatziDto();
    responseService.setAvatar("avatar");
    responseService.setName("name");
    responseService.setId(1);
    responseService.setEmail("email");
    responseService.setRole("role");
    responseService.setPassword("password");
    responseService.setCreationAt(LocalDateTime.now());
    responseService.setUpdatedAt(LocalDateTime.now());

    mockWebServer.enqueue(new MockResponse()
            .setBody(mapper.writeValueAsString(responseService))
            .addHeader("Content-Type", "application/json").setResponseCode(200));

    MvcResult result = mvc.perform(post("/api/platzi/user")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(mapper.writeValueAsBytes(user)))
            .andExpect(status().isOk())
            .andReturn();

    UserPlatziDto responseController = mapper.readValue(result.getResponse().getContentAsString(),
            UserPlatziDto.class);

    assertEquals(responseService.getAvatar(), responseController.getAvatar());
    assertEquals(responseService.getId(), responseController.getId());
  }
}