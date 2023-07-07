package com.inventi.bank.repository;

import com.inventi.bank.entity.StatementEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatementRepo extends CrudRepository<StatementEntity, Long> {
}
