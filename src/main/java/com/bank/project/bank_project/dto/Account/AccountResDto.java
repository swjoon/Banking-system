package com.bank.project.bank_project.dto.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.transaction.Transaction;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.dto.transaction.TransactionResDto.TransactionDepositDto;
import com.bank.project.bank_project.dto.transaction.TransactionResDto.TransactionTransferDto;
import com.bank.project.bank_project.dto.transaction.TransactionResDto.TransactionWithdrawDto;

import lombok.Data;

public class AccountResDto {

	@Data
	public static class AccountDto {

		private Long id;
		private Long number;
		private Long balance;

		public AccountDto(Account account) {
			this.id = account.getId();
			this.number = account.getNumber();
			this.balance = account.getBalance();
		}

	}

	// 계좌 등록
	@Data
	public static class AccountSaveResDto {

		private Long id;
		private Long number;
		private Long balance;

		public AccountSaveResDto(Account account) {
			this.id = account.getId();
			this.number = account.getNumber();
			this.balance = account.getBalance();
		}
	}

	// 계좌 목록
	@Data
	public static class AccountListResDto {

		private String fullname;
		private List<AccountDto> accounts = new ArrayList<>();

		public AccountListResDto(User user, List<Account> accounts) {
			this.fullname = user.getFullname();
			this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
		}
	}

	// 계좌 입금
	@Data
	public static class AccountDepositResDto {
		private Long id; // 계좌 ID
		private Long number; // 계좌번호
		private TransactionDepositDto transaction;

		public AccountDepositResDto(Account account, Transaction transaction) {
			this.id = account.getId();
			this.number = account.getNumber();
			this.transaction = new TransactionDepositDto(transaction);
		}
	}
	
	// 계좌 출금
	@Data
	public static class AccountWithdrawResDto {
		private Long id; // 계좌 ID
		private Long number; // 계좌번호
		private Long balance; // 잔액
		private TransactionWithdrawDto transaction;

		public AccountWithdrawResDto(Account account, Transaction transaction) {
			this.id = account.getId();
			this.number = account.getNumber();
			this.balance = account.getBalance();
			this.transaction = new TransactionWithdrawDto(transaction);
		}
	}
	
	@Data
	public static class AccountTransferResDto {
		private Long id; // 계좌 ID
		private Long number; // 계좌번호
		private Long balance; // 잔액
		private TransactionTransferDto transaction;

		public AccountTransferResDto(Account account, Transaction transaction) {
			this.id = account.getId();
			this.number = account.getNumber();
			this.balance = account.getBalance();
			this.transaction = new TransactionTransferDto(transaction);
		}
	}
}
