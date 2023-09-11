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
 * Autor: 319207
 * Fecha de creación: septiembre 11, 2023
 */

package com.example.mapstruddemo.service;

import com.example.mapstruddemo.dto.FakeDto;
import com.example.mapstruddemo.dto.ProductosDto;
import com.example.mapstruddemo.exceptions.BusinessRuleException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.List;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Log4j2
@Service
public class FileServiceImpl implements FileService {


  @Autowired
  private RandoFakeService randoFakeService;
  @Autowired
  private ProductosService productosService;

  private List<String> headerExcel = Arrays.asList("Nombre", "Direccion", "Zodiaco");
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
      log.info("Error al crear el libro ".concat(ex.getMessage()));
      throw new BusinessRuleException("Error al crear el libro ".concat(ex.getMessage()));
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
    } catch (IOException ex) {
      log.info("Error al crear el libro ".concat(ex.getMessage()));
      throw new BusinessRuleException("Error al crear el libro ".concat(ex.getMessage()));
    }
  }

  private Sheet createHeader(Sheet sheet) {
    Row row = sheet.createRow(0);
    for (int i = 0; i < headerExcel.size(); i++) {
      Cell cell = row.createCell(i);
      cell.setCellValue(headerExcel.get(i));
    }
    return sheet;
  }

  private Sheet processData(Sheet sheet, List<FakeDto> dataList) {
    Integer count = 1;
    for (FakeDto item : dataList) {
      Row row = sheet.createRow(count);
      writeData(sheet, row, item);
      count++;
    }
    return sheet;
  }

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
