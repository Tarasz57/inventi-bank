package com.inventi.bank.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Entity
@Table(name = "statement")
@NoArgsConstructor
@Getter
public class StatementEntity {
  @Id
  @GeneratedValue(strategy = GenerationType.TABLE)
  private Long id;
  private String accountNumber;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDateTime timeOfOperation;
  private String beneficiary;
  private String comment;
  private Double amount;
  private String currency;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @CreationTimestamp
  private LocalDateTime created;
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @UpdateTimestamp
  private LocalDateTime updated;

  public StatementEntity(String accountNumber,
                         LocalDateTime timeOfOperation,
                         String beneficiary,
                         String comment,
                         double amount,
                         String currency) {
    this.accountNumber = accountNumber;
    this.timeOfOperation = timeOfOperation;
    this.beneficiary = beneficiary;
    this.comment = comment;
    this.amount = amount;
    this.currency = currency;
  }
}
