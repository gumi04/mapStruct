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
 * Nombre de archivo: FileServiceImpl
 * Autor: anonimo
 * Fecha de creación: septiembre 11, 2023
 */

package com.example.demomap.service;

import com.example.demomap.dto.CsvDto;
import com.example.demomap.dto.DataExcelDto;
import com.example.demomap.dto.FakeDto;
import com.example.demomap.dto.ProductosDto;
import com.example.demomap.exceptions.BusinessRuleException;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * The type File service.
 */
@Log4j2
@Service
public class FileServiceImpl implements FileService {


  /**
   * The Rando fake service.
   */
  @Autowired
  private RandoFakeService randoFakeService;
  /**
   * The Productos service.
   */
  @Autowired
  private ProductosService productosService;

  /**
   * The Header excel.
   */
  private List<String> headerExcel = Arrays.asList("Nombre", "Direccion", "Zodiaco");
  /**
   * The Header csv.
   */
  private List<String> headerCSV = Arrays.asList("id", "nombre", "fecha");

  @Override
  public byte[] createCsv() {

    try (ByteArrayOutputStream arrayOutputStream = new ByteArrayOutputStream();
         CSVPrinter csvPrinter = new CSVPrinter(new OutputStreamWriter(arrayOutputStream), CSVFormat.DEFAULT)) {
      csvPrinter.printRecord(headerCSV);
      for (ProductosDto item : productosService.getAllProducts()) {
        csvPrinter.printRecord(item.getId(), item.getNombre(), item.getFecha());
      }
      csvPrinter.flush();
      return arrayOutputStream.toByteArray();
    } catch (Exception ex) {
      log.info("Error al crear csv".concat(ex.getMessage()));
      throw new BusinessRuleException("Error al crear el libro csv ".concat(ex.getMessage()));
    }

  }

  @Override
  public ByteArrayInputStream createExcel() {

    try (Workbook book = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
      Sheet sheet = book.createSheet("fake");
      createHeader(sheet);
      processData(sheet, randoFakeService.getDummyData());
      book.write(out);
      return new ByteArrayInputStream(out.toByteArray());
    } catch (Exception ex) {
      log.info("Error al crear el libro de excel ".concat(ex.getMessage()));
      throw new BusinessRuleException("Error al crear el libro de excel ".concat(ex.getMessage()));
    }
  }

  @Override
  public List<CsvDto> readCsv(MultipartFile file) {

    if (!FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase("csv")) {
      throw new BusinessRuleException("El archivo no puede ser procesado");
    }
    /*try (CSVReader reader = new CSVReaderBuilder(
            new InputStreamReader(file.getInputStream()))
            .withSkipLines(1)
            .build()) {
      String[] line = null;
      while (!Objects.isNull(line = reader.readNext())) {
        System.out.println(Arrays.toString(line));
      }
    } catch (Exception ex) {
      log.info("Error al leer libro ".concat(ex.getMessage()));
      throw new BusinessRuleException("Error al leer libro ".concat(ex.getMessage()));
    }*/
    try (CSVReader reader = new CSVReaderBuilder(
            new InputStreamReader(file.getInputStream()))
            .withSkipLines(1)
            .build()) {
      CsvToBean<CsvDto> csvToBean =
              new CsvToBeanBuilder<CsvDto>(reader)
                      .withType(CsvDto.class)
                      .withSeparator(',')
                      .withIgnoreLeadingWhiteSpace(true)
                      .withIgnoreEmptyLine(true)
                      .build();


      return csvToBean.parse();
    } catch (Exception ex) {
      log.info("Error al leer libro ".concat(ex.getMessage()));
      throw new BusinessRuleException("Error al leer libro ".concat(ex.getMessage()));
    }
  }

  @Override
  public List<DataExcelDto> readExcel(MultipartFile file) {
    if (!FilenameUtils.getExtension(file.getOriginalFilename()).equalsIgnoreCase("xlsx")) {
      throw new BusinessRuleException("El archivo no puede ser procesado");
    }
    try (Workbook book = WorkbookFactory.create(file.getInputStream())) {
      List<DataExcelDto> response = new ArrayList<>();
      Sheet sheet = book.getSheetAt(0);
      int countRow = 1;
      Row row = sheet.getRow(countRow);
      while (Objects.nonNull(row)) {
        DataExcelDto item = new DataExcelDto();
        Cell cell;
        cell = row.getCell(0);
        item.setFullName(cell.getStringCellValue());
        cell = row.getCell(1);
        item.setStreetAddress(cell.getStringCellValue());
        cell = row.getCell(2);
        item.setZodico(cell.getStringCellValue());
        cell = row.getCell(3);
        item.setFecha(cell.getLocalDateTimeCellValue().toLocalDate());
        response.add(item);
        countRow++;
        row = sheet.getRow(countRow);
      }
      return response;
    } catch (Exception ex) {
      log.info("No se puede leer el libro ".concat(ex.getMessage()));
      throw new BusinessRuleException("o se puede leer el libro ".concat(ex.getMessage()));
    }
  }

  /**
   * Create header sheet.
   *
   * @param sheet the sheet
   * @return the sheet
   */
  private Sheet createHeader(Sheet sheet) {
    Row row = sheet.createRow(0);
    for (int i = 0; i < headerExcel.size(); i++) {
      Cell cell = row.createCell(i);
      cell.setCellValue(headerExcel.get(i));
    }
    return sheet;
  }

  /**
   * Process data sheet.
   *
   * @param sheet    the sheet
   * @param dataList the data list
   * @return the sheet
   */
  private Sheet processData(Sheet sheet, List<FakeDto> dataList) {
    Integer count = 1;
    for (FakeDto item : dataList) {
      Row row = sheet.createRow(count);
      writeData(sheet, row, item);
      count++;
    }
    return sheet;
  }

  /**
   * Write data sheet.
   *
   * @param sheet the sheet
   * @param row   the row
   * @param data  the data
   * @return the sheet
   */
  private Sheet writeData(Sheet sheet, Row row, FakeDto data) {
    Cell cell;
    cell = row.createCell(0);
    cell.setCellValue(data.getFullName());
    cell = row.createCell(1);
    cell.setCellValue(data.getStreetAddress());
    cell = row.createCell(2);
    cell.setCellValue(data.getZodico());
    return sheet;
  }
}
