package com.bank.project.bank_project.dto.Account;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.user.User;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

public class AccountReqDto {

	// 계좌 등록
	@Data
	public static class AccountSaveReqDto {

		@NotNull
		@Digits(integer = 10, fraction = 0)
		private Long number;

		@NotNull
		@Digits(integer = 4, fraction = 4)
		private Long password;

		public Account toEntity(User user) {
			return Account.builder().number(number).password(password).balance(1000L).user(user).build();
		}
	}

	// 계좌 입금
	@Data
	public static class AccountDepositReqDto {

		@NotNull
		@Digits(integer = 10, fraction = 10)
		private Long number;

		@NotNull
		private Long amount; // 0원 유효성 검사

		@NotEmpty
		@Pattern(regexp = "DEPOSIT")
		private String type; // DEPOSIT

		@NotEmpty
		@Pattern(regexp = "^[0-9]{11}")
		private String tel;

	}

	@Data
	public static class AccountWithdrawReqDto {

		@NotNull
		@Digits(integer = 10, fraction = 10)
		private Long number;
		
		@NotNull
		@Digits(integer = 4, fraction = 4)
		private Long password;

		@NotNull
		private Long amount; // 0원 유효성 검사

		@NotEmpty
		@Pattern(regexp = "WITHDRAW")
		private String type; // DEPOSIT

	}
	
	@Data
	public static class AccountTransferReqDto {

		@NotNull
		@Digits(integer = 10, fraction = 10)
		private Long withdrawNumber;
		
		@NotNull
		@Digits(integer = 10, fraction = 10)
		private Long depositNumber;
		
		@NotNull
		@Digits(integer = 4, fraction = 4)
		private Long withdrawPassword;

		@NotNull
		private Long amount; // 0원 유효성 검사

		@NotEmpty
		@Pattern(regexp = "TRANSFER")
		private String type; // DEPOSIT

	}
}
