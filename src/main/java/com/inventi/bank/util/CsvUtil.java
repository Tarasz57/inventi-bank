package com.inventi.bank.util;

import com.inventi.bank.exception.FileReadingException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.List;

public class CsvUtil {

  private static final Logger logger = LoggerFactory.getLogger(CsvUtil.class);

  public static <T> List<T> parseCsvToModel(MultipartFile file, Class<T> responseType){
    List<T> models;

    try (Reader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
      // create csv bean reader
      CsvToBean<T> csvToBean = new CsvToBeanBuilder(reader)
          .withType(responseType)
          .withIgnoreLeadingWhiteSpace(true)
          .build();

      // convert `CsvToBean` object to list of models
      models = csvToBean.parse();
    } catch (IOException ex) {
      logger.error(ex.getCause().getMessage());
      throw new FileReadingException(file.getOriginalFilename());
    }

    return models;
  }

}
