package com.inventi.bank.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
//@NoArgsConstructor(access = AccessLevel.PRIVATE)
//@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Statement {
  @CsvBindByName(required = true)
  private String accountNumber;
  @CsvBindByName(required = true)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime timeOfOperation;
  @CsvBindByName(required = true)
  private String beneficiary;
  @CsvBindByName
  private String comment;
  @CsvBindByName(required = true)
  private double amount;
  @CsvBindByName(required = true)
  private String currency;
}
