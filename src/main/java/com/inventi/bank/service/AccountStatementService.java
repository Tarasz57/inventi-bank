package com.inventi.bank.service;

import com.inventi.bank.entity.StatementEntity;
import com.inventi.bank.model.AccountBalance;
import com.inventi.bank.model.Statement;
import com.inventi.bank.repository.StatementRepo;
import com.inventi.bank.util.CsvUtil;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountStatementService {

  private final StatementRepo statementRepo;

  @Autowired
  public AccountStatementService(StatementRepo statementRepo) {
    this.statementRepo = statementRepo;
  }

  public List<List<Statement>> parseAndSaveStatements(MultipartFile[] files) {
    List<List<Statement>> statements = new ArrayList<>();
    for (var file : files) {
      List<Statement> parsedStatement = CsvUtil.parseCsvToModel(file, Statement.class);
      statements.add(parsedStatement);
      List<StatementEntity> statementEntity = mapToStatementEntity(parsedStatement);
      statementRepo.saveAll(statementEntity);
    }
    return statements;
  }

  public List<Statement> findStatements(String account, Date dateFrom, Date dateTo) {
      List<StatementEntity> savedStatements;
      LocalDateTime from = dateFrom != null ? dateFrom.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime() : null;
      LocalDateTime to = dateTo != null ? dateTo.toInstant().atZone(ZoneId.of("UTC")).toLocalDateTime() : null;
      if(from != null && to != null){
        savedStatements = statementRepo.
            findAllByAccountNumberAndTimeOfOperationBetweenOrderByTimeOfOperationDesc(
                account,
                from,
                to);
      }
      else if(to != null){
        savedStatements = statementRepo.findAllByAccountNumberAndTimeOfOperationBeforeOrderByTimeOfOperationDesc(
            account,
            to
        );
      } else if (from != null){
        savedStatements = statementRepo.findAllByAccountNumberAndTimeOfOperationAfterOrderByTimeOfOperationDesc(
            account,
            from
        );
      } else {
        savedStatements = statementRepo.findAllByAccountNumberOrderByTimeOfOperationDesc(account);
      }

      List<Statement> statements = mapToStatement(savedStatements);
    return statements;
  }

  public AccountBalance calculateBalance(String account, Date dateFrom, Date dateTo) {
    return new AccountBalance(statementRepo.calculateBalance(account));
  }

  private List<StatementEntity> mapToStatementEntity(List<Statement> statements) {
    List<StatementEntity> statementEntities = statements.stream()
        .map(statement ->
            new StatementEntity(
                statement.getAccountNumber(),
                statement.getTimeOfOperation(),
                statement.getBeneficiary(),
                statement.getComment(),
                statement.getAmount(),
                statement.getCurrency()
            )
        )
        .collect(Collectors.toList());
    return statementEntities;
  }

  private List<Statement> mapToStatement(List<StatementEntity> statements) {
    List<Statement> statementEntities = statements.stream()
        .map(statement ->
            new Statement(
                statement.getAccountNumber(),
                statement.getTimeOfOperation(),
                statement.getBeneficiary(),
                statement.getComment(),
                statement.getAmount(),
                statement.getCurrency()
            )
        )
        .collect(Collectors.toList());
    return statementEntities;
  }

}
