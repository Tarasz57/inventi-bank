package com.inventi.bank.controller;

import com.inventi.bank.service.AccountStatementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/statement")
public class StatementController {

  private final AccountStatementService accountStatementService;

  @Autowired
  StatementController(AccountStatementService accountStatementService) {
    this.accountStatementService = accountStatementService;
  }

  @PostMapping("/import")
  public ResponseEntity importStatement(@RequestParam("files") MultipartFile[] files) {
    return ResponseEntity.ok(accountStatementService.parseAndSaveStatements(files));
  }

}
