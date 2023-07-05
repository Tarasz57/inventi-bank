package com.inventi.bank.model;

import com.opencsv.bean.CsvBindByName;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Statement {
  @CsvBindByName(required = true)
  private String accountNumber;
  @CsvBindByName(required = true)
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
