package com.inventi.bank;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.inventi.bank.controller.StatementController;
import com.inventi.bank.exception.ControllerAdvisor;
import com.inventi.bank.model.AccountBalance;
import com.inventi.bank.model.Statement;
import com.inventi.bank.service.AccountStatementService;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
public class ControllerTest {

  @Mock
  AccountStatementService accountStatementService;

  @InjectMocks
  private StatementController statementController;

  private MockMvc mockMvc;

  @BeforeEach
  public void init() {
    mockMvc = MockMvcBuilders
        .standaloneSetup(statementController)
        .setControllerAdvice(new ControllerAdvisor())
        .build();
  }

  @Test
  void importStatementTest() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "files",
            "hello.csv",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            ("accountNumber,timeOfOperation,beneficiary,comment,amount\n"
                + "121,2022-09-21T02:15:24.521Z,4,com,23.6,EUR").getBytes()
        );

    List<Statement> statements = Collections.singletonList(new Statement(
        "121", LocalDateTime.now(),"4","com",23.6,"EUR"
    ));

    when(accountStatementService.parseAndSaveStatements(any())).thenReturn(
        Collections.singletonList(statements));

    mockMvc.perform(MockMvcRequestBuilders
        .multipart("/statement/import")
            .file(file))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0][0].amount").value(23.6));
  }

  @Test
  void exportStatementsTest() throws Exception {
    List<Statement> statements = Collections.singletonList(new Statement(
        "121", LocalDateTime.now(),"4","com",23.6,"EUR"
    ));

    when(accountStatementService.findStatements(any(), any(), any())).thenReturn(statements);

    MvcResult result = mockMvc.perform(MockMvcRequestBuilders
            .get("/statement/export?accounts=121"))
        .andExpect(status().isOk()).andReturn();

    byte[] byteArray = result.getResponse().getContentAsByteArray();
    ByteArrayInputStream inputStream = new ByteArrayInputStream(byteArray);
    final var zin = new ZipInputStream(inputStream);
      ZipEntry entry;
      String name;

    while ((entry = zin.getNextEntry()) != null) {
      name = entry.getName();
      zin.closeEntry();
      assertEquals("121_statement.csv", name);
    }
  }

  @Test
  void accountBalanceEndpointTest() throws Exception {
    AccountBalance mockBalance = new AccountBalance(23.0);
    when(accountStatementService.calculateBalance(any(), any(), any())).thenReturn(mockBalance);

    mockMvc.perform(MockMvcRequestBuilders
        .get("/statement/calculate-balance?account=1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.balance").value(23.0));
  }

  @Test
  void controllerAdviceTest() throws Exception {
    MockMultipartFile file =
        new MockMultipartFile(
            "files",
            "hello.csv",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            "accountNumber,timeOfOperation,comment,amount".getBytes()
        );

    when(accountStatementService.parseAndSaveStatements(any())).thenCallRealMethod();

    mockMvc.perform(MockMvcRequestBuilders
            .multipart("/statement/import")
            .file(file))
        .andExpect(status().isBadRequest())
        .andReturn();
  }

}
