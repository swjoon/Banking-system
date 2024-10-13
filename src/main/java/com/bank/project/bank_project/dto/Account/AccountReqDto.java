package com.bank.project.bank_project.dto.Account;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.user.User;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

public class AccountReqDto {
	
	
	
	@Data
	public static class AccountSaveReqDto{
		
		@NotNull
		@Digits(integer = 10, fraction = 0)
		private Long number;
		
		@NotNull
		@Digits(integer = 4, fraction = 4)
		private Long password;
		
		public Account toEntity(User user) {
			return Account.builder()
					.number(number)
					.password(password)
					.balance(1000L)
					.user(user)
					.build();
		}
		
	}
}
