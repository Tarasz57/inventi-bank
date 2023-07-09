package com.inventi.bank.model;

import com.opencsv.bean.CsvBindByName;
import com.opencsv.bean.CsvDate;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
public class Statement {
  @CsvBindByName(required = true)
  private String accountNumber;
  @CsvBindByName(required = true)
  @CsvDate(value = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
  private LocalDateTime timeOfOperation;
  @CsvBindByName(required = true)
  private String beneficiary;
  @CsvBindByName
  private String comment;
  @CsvBindByName(required = true)
  private Double amount;
  @CsvBindByName(required = true)
  private String currency;

  public Statement(String accountNumber,
      LocalDateTime timeOfOperation,
      String beneficiary,
      String comment,
      Double amount,
      String currency) {
    this.accountNumber = accountNumber;
    this.timeOfOperation = timeOfOperation;
    this.beneficiary = beneficiary;
    this.comment = comment;
    this.amount = amount;
    this.currency = currency;
  }
}
