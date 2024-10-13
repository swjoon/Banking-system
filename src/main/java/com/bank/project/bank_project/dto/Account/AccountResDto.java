package com.bank.project.bank_project.dto.Account;

import com.bank.project.bank_project.domain.account.Account;

import lombok.Data;

public class AccountResDto {

	@Data
	public static class AccountSaveResDto{
		
		private Long id;
		private Long number;
		private Long balance;
		
		public AccountSaveResDto(Account account) {
			this.id = account.getId();
			this.number = account.getNumber();
			this.balance = account.getBalance();
		}
	}
}
