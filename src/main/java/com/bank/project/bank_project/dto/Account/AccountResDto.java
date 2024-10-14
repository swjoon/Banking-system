package com.bank.project.bank_project.dto.Account;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.user.User;

import lombok.Data;

public class AccountResDto {

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

	
	
	@Data
	public static class AccountListResDto {

		private String fullname;
		private List<AccountDto> accounts = new ArrayList<>();

		public AccountListResDto(User user, List<Account> accounts) {
			this.fullname = user.getFullname();
			this.accounts = accounts.stream().map(AccountDto::new).collect(Collectors.toList());
		}
	}
}
