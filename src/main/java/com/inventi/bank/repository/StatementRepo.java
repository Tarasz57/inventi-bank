package com.inventi.bank.repository;

import com.inventi.bank.entity.StatementEntity;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepo extends CrudRepository<StatementEntity, Long> {
  List<StatementEntity> findAllByAccountNumberOrderByTimeOfOperationDesc(String acc);
  List<StatementEntity> findAllByAccountNumberAndTimeOfOperationBetweenOrderByTimeOfOperationDesc(String acc, LocalDateTime from, LocalDateTime to);
  List<StatementEntity> findAllByAccountNumberAndTimeOfOperationBeforeOrderByTimeOfOperationDesc(String acc, LocalDateTime to);
  List<StatementEntity> findAllByAccountNumberAndTimeOfOperationAfterOrderByTimeOfOperationDesc(String acc, LocalDateTime from);

  @Query(value = "SELECT SUM(amount) FROM statement where account_number = ?1", nativeQuery = true)
  Double calculateBalance(String acc);
  @Query(value = "SELECT SUM(amount) FROM statement where account_number = ?1 and time_of_operation > ?2", nativeQuery = true)
  Double calculateBalanceAfterDate(String acc, LocalDateTime from);
  @Query(value = "SELECT SUM(amount) FROM statement where account_number = ?1 and time_of_operation < ?2", nativeQuery = true)
  Double calculateBalanceBeforeDate(String acc, LocalDateTime to);
  @Query(value = "SELECT SUM(amount) FROM statement where account_number = ?1 and time_of_operation > ?2 and time_of_operation < ?3", nativeQuery = true)
  Double calculateBalanceBetweenDates(String acc, LocalDateTime from, LocalDateTime to);
}
