package com.bank.project.bank_project.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.transaction.Transaction;
import com.bank.project.bank_project.dto.transaction.TransactionResDto.TransactionListResDto;
import com.bank.project.bank_project.handler.ex.CustomApiException;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.transaction.TransactionRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TransactionService {

	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;
	
	public TransactionListResDto 입출금목록보기(Long userId, Long accountNumber, String type, int page) {
		Account accountPS = accountRepository.findByNumber(accountNumber).orElseThrow(
				()-> new CustomApiException("해당 계좌를 찾을 수 없습니다."));
		
		accountPS.checkOwner(userId);
		
		List<Transaction> transactionListPs = transactionRepository.findTransactionList(accountPS.getId(), type, page);
		
		return new TransactionListResDto(transactionListPs, accountPS);
	}
	
	
}
