package com.inventi.bank.exception;

public class FileReadingException extends RuntimeException {

  public FileReadingException(String fileName){
    super("Error reading file " + fileName);
  }

}
