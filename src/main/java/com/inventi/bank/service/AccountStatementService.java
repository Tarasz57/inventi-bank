package com.inventi.bank.service;

import com.inventi.bank.entity.StatementEntity;
import com.inventi.bank.model.Statement;
import com.inventi.bank.repository.StatementRepo;
import com.inventi.bank.util.CsvUtil;
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

}
