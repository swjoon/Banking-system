package com.bank.project.bank_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.project.bank_project.domain.transaction.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long>{

}
