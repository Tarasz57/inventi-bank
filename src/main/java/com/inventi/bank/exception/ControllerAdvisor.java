package com.inventi.bank.exception;

import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import java.io.IOException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class ControllerAdvisor extends ResponseEntityExceptionHandler {

  @ExceptionHandler({IOException.class, FileReadingException.class, CsvRequiredFieldEmptyException.class})
  public ResponseEntity<Object> handleReadAndParseException(
      Exception ex) {

    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("error", ex.getCause().getMessage());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

  @ExceptionHandler(CsvDataTypeMismatchException.class)
  public ResponseEntity<Object> handleCsvParseException(CsvDataTypeMismatchException ex, WebRequest request) {
    Map<String, Object> body = new LinkedHashMap<>();
    body.put("timestamp", LocalDateTime.now());
    body.put("error", ex.getCause().getMessage() + " at line number " + ex.getLineNumber());

    return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
  }

}
