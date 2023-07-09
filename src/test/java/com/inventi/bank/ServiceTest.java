package com.inventi.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import com.inventi.bank.entity.StatementEntity;
import com.inventi.bank.model.AccountBalance;
import com.inventi.bank.model.Statement;
import com.inventi.bank.repository.StatementRepo;
import com.inventi.bank.service.AccountStatementService;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

@ExtendWith(MockitoExtension.class)
public class ServiceTest {

  @Mock
  private StatementRepo statementRepo;

  @InjectMocks
  private AccountStatementService accountStatementService;

  @Test
  void statementParseTest() {
    List<List<Statement>> statements = Collections.singletonList(Collections.emptyList());
    MockMultipartFile[] file = {
      new MockMultipartFile(
        "file",
        "hello.csv",
        MediaType.TEXT_PLAIN_VALUE,
        "accountNumber,timeOfOperation,beneficiary,comment,amount,currency".getBytes()
      )
    };

    List<List<Statement>> returnedStatements = accountStatementService.parseAndSaveStatements(file);

    assertEquals(statements.size(), returnedStatements.size());
    assertEquals(statements.get(0).size(), returnedStatements.get(0).size());
  }

  @Test
  void findStatementsTest() {
    StatementEntity statementEntity = new StatementEntity(
        "123",
        LocalDateTime.now(),
        "6",
        "f",
        23.0,
        "USD"
    );

    List<StatementEntity> statementEntities = Collections.singletonList(statementEntity);

    when(statementRepo.findAllByAccountNumberOrderByTimeOfOperationDesc(any()))
        .thenReturn(statementEntities);

    List<Statement> statements = accountStatementService.findStatements("123", null, null);

    assertEquals(statements.size(), statementEntities.size());
    assertEquals(statements.get(0).getAccountNumber(), statementEntities.get(0).getAccountNumber());
  }

  @Test
  void findStatementsBeforeTest() {
    StatementEntity statementEntityBefore = new StatementEntity(
        "123",
        LocalDateTime.now().minusDays(10),
        "6",
        "f",
        23.0,
        "USD"
    );
    List<StatementEntity> statementEntitiesBefore = Collections.singletonList(statementEntityBefore);
    when(statementRepo.findAllByAccountNumberAndTimeOfOperationBeforeOrderByTimeOfOperationDesc(any(), any()))
        .thenReturn(statementEntitiesBefore);

    List<Statement> statements = accountStatementService.findStatements("123", null, new Date());

    assertEquals(statements.size(), statementEntitiesBefore.size());
    assertEquals(statements.get(0).getAccountNumber(), statementEntitiesBefore.get(0).getAccountNumber());
  }

  @Test
  void findStatementsAfterTest() {
    StatementEntity statementEntityAfter = new StatementEntity(
        "123",
        LocalDateTime.now().plusDays(10),
        "6",
        "f",
        23.0,
        "USD"
    );
    List<StatementEntity> statementEntitiesAfter = Collections.singletonList(statementEntityAfter);
    when(statementRepo.findAllByAccountNumberAndTimeOfOperationAfterOrderByTimeOfOperationDesc(any(), any()))
        .thenReturn(statementEntitiesAfter);

    List<Statement> statements = accountStatementService.findStatements("123",  new Date(), null);

    assertEquals(statements.size(), statementEntitiesAfter.size());
    assertEquals(statements.get(0).getAccountNumber(), statementEntitiesAfter.get(0).getAccountNumber());

  }

  @Test
  void findStatementsBetweenTest() {
    StatementEntity statementEntityBetween = new StatementEntity(
        "123",
        LocalDateTime.now().plusDays(5),
        "6",
        "f",
        23.0,
        "USD"
    );
    List<StatementEntity> statementEntitiesBetween = Collections.singletonList(statementEntityBetween);
    when(statementRepo.findAllByAccountNumberAndTimeOfOperationBetweenOrderByTimeOfOperationDesc(any(), any(), any()))
        .thenReturn(statementEntitiesBetween);

    List<Statement> statements = accountStatementService.findStatements("123", new Date(), new Date());

    assertEquals(statements.size(), statements.size());
    assertEquals(statements.get(0).getAccountNumber(), statements.get(0).getAccountNumber());

  }

  @Test
  void calculateBalanceTest() {
    Double balance = 20.0;
    String account = "12";

    when(statementRepo.calculateBalance(account)).thenReturn(balance);

    AccountBalance calculatedBalance = accountStatementService.calculateBalance("12", null, null);

    assertEquals(balance, calculatedBalance.getBalance());
  }

  @Test
  void calculateBalanceBeforeTest() {
    Double balance = 20.0;
    String account = "12";

    when(statementRepo.calculateBalanceBeforeDate(eq(account), any())).thenReturn(balance);

    AccountBalance calculatedBalance = accountStatementService.calculateBalance("12", null, new Date());

    assertEquals(balance, calculatedBalance.getBalance());
  }

  @Test
  void calculateBalanceAfterTest() {
    Double balance = 20.0;
    String account = "12";

    when(statementRepo.calculateBalanceAfterDate(eq(account), any())).thenReturn(balance);

    AccountBalance calculatedBalance = accountStatementService.calculateBalance("12", new Date(), null);

    assertEquals(balance, calculatedBalance.getBalance());
  }

  @Test
  void calculateBalanceBetweenTest() {
    Double balance = 20.0;
    String account = "12";

    when(statementRepo.calculateBalanceBetweenDates(eq(account), any(), any())).thenReturn(balance);

    AccountBalance calculatedBalance = accountStatementService.calculateBalance("12", new Date(), new Date());

    assertEquals(balance, calculatedBalance.getBalance());
  }
}
