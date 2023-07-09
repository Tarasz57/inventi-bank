package com.inventi.bank.controller;

import com.inventi.bank.model.Statement;
import com.inventi.bank.service.AccountStatementService;
import com.opencsv.CSVWriter;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/statement")
public class StatementController {

  private final AccountStatementService accountStatementService;

  @Autowired
  StatementController(AccountStatementService accountStatementService) {
    this.accountStatementService = accountStatementService;
  }

  @PostMapping("/import")
  public ResponseEntity importStatement(@RequestParam MultipartFile[] files) {
    return ResponseEntity.ok(accountStatementService.parseAndSaveStatements(files));
  }

  @GetMapping("/export")
  public void exportStatement(
      @RequestParam List<String> accounts,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateTo,
      HttpServletResponse response)
      throws IOException, CsvRequiredFieldEmptyException, CsvDataTypeMismatchException {

    OutputStream servletOutputStream = response.getOutputStream();
    ZipOutputStream zos = new ZipOutputStream(servletOutputStream);

    for(String account : accounts){
      List<Statement> statements = accountStatementService.findStatements(account, dateFrom, dateTo);
      String filename = account + "_statement.csv";
      ZipEntry entry = new ZipEntry(filename);
      zos.putNextEntry(entry);

      OutputStreamWriter outputStreamWriter = new OutputStreamWriter(zos);

      StatefulBeanToCsv<Statement> writer = new StatefulBeanToCsvBuilder<Statement>(outputStreamWriter)
          .withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
          .withSeparator(CSVWriter.DEFAULT_SEPARATOR)
          .withOrderedResults(true)
          .build();

      writer.write(statements);
      outputStreamWriter.flush();

      zos.closeEntry();
    }
    zos.close();
  }

  @GetMapping("/calculate-balance")
  public ResponseEntity calculateBalance(
      @RequestParam String account,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateFrom,
      @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) Date dateTo) {
    return ResponseEntity.ok(accountStatementService.calculateBalance(account, dateFrom, dateTo));
  }

}
