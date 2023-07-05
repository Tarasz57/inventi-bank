package com.inventi.bank.entity;

import java.time.LocalDateTime;

public class StatementEntity {
  private String accountNumber;
  private LocalDateTime timeOfOperation;
  private String beneficiary;
  private String comment;
  private double amount;
  private String currency;
  private LocalDateTime created;
  private LocalDateTime updated;
}
