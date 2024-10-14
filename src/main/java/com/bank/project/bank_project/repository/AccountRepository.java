package com.bank.project.bank_project.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.bank.project.bank_project.domain.account.Account;

public interface AccountRepository extends JpaRepository<Account, Long>{

	// checkpoint : 리펙토링 (join fetch)
	// @Query("SELECT ac FROM Account.ac JOIN FETCH ac.user WHERE ac.number = :number" ) // 매 순간 user를 가져올 때 사용.
	Optional<Account> findByNumber(Long number);
	
	List<Account> findByUser_id(Long id);
	
}
