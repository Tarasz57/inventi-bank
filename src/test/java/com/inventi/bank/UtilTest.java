package com.inventi.bank;

import com.inventi.bank.model.Statement;
import com.inventi.bank.util.CsvUtil;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class UtilTest {

  @Test
  void parseCsvToModelTest() {
    MockMultipartFile file =
        new MockMultipartFile(
            "file",
            "hello.csv",
            MediaType.MULTIPART_FORM_DATA_VALUE,
            ("accountNumber,timeOfOperation,beneficiary,comment,amount,currency\n"
                + "121,2022-09-21T02:15:24.521Z,4,com,23.6,EUR").getBytes()
        );

    List<Statement> statements = CsvUtil.parseCsvToModel(file, Statement.class);

    assertEquals(statements.get(0).getAccountNumber(), "121");
  }

}
