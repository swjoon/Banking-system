package com.bank.project.bank_project.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.project.bank_project.domain.account.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

}
