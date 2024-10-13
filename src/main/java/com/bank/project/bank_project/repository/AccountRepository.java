package com.bank.project.bank_project.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.project.bank_project.domain.account.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

	// checkpoint : 리펙토링 (join fetch)
	Optional<Account> findByNumber(Long number);
	
}
