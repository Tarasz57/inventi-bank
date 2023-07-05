package com.inventi.bank.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.inventi.bank.entity.StatementEntity;
import com.inventi.bank.model.Statement;
import com.inventi.bank.util.CsvUtil;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.List;

@Service
public class AccountStatementService {

  public List<List<Statement>> parseAndSaveStatements(MultipartFile[] files) {
    List<List<Statement>> statements = Collections.emptyList();
    for(var file : files){
      List<Statement> parsedStatement = CsvUtil.parseCsvToModel(file, Statement.class);
      statements.add(parsedStatement);
      //TODO add save to repo here
    }
    return statements;
  }

  public Object mapToStatementEntity(Object object){
    ObjectMapper mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    StatementEntity statementEntity = mapper.convertValue(object, StatementEntity.class);
    return statementEntity;
  }

}
