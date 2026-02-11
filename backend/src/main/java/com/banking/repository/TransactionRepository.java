package com.banking.repository;

import com.banking.entity.Transaction;
import com.banking.enums.TransactionType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    Page<Transaction> findByType(TransactionType type, Pageable pageable);
    Page<Transaction> findByAccount_AccountNumber(String accountNumber, Pageable pageable);
    Page<Transaction> findByAccount_AccountNumberAndType(String accountNumber, TransactionType type, Pageable pageable);
}
