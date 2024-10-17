package com.bank.project.bank_project.dto.transaction;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.transaction.Transaction;
import com.bank.project.bank_project.util.CustomDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

public class TransactionResDto {

	@Data
	public static class TransactionDto {
		private Long id;
		private String type;
		private Long amount;
		private String sender;
		private String reciver;
		private String tel;
		private String createdAt;
		private Long balance;

		public TransactionDto(Transaction transaction, Long accountNumber) {
			this.id = transaction.getId();
			this.type = transaction.getType().getValue();
			this.amount = transaction.getAmount();
			this.sender = transaction.getSender();
			this.reciver = transaction.getReceiver();
			this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
			this.tel = transaction.getTel() == null ? "없음" : transaction.getTel();

			// 1111 계좌의 입출금 내역 (출금계좌 = null, 입금계좌 = 값) (출금계좌 = 값, 입금계좌 = null)
			if (transaction.getDepositAccount() == null) {
				this.balance = transaction.getWithdrawAccountBalance();
			} else if (transaction.getWithdrawAccount() == null) {
				this.balance = transaction.getDepositAccountBalance();
			} else {
				// 1111 계좌의 입출금 내역 (출금계좌 = 값, 입금계좌 = 값)
				if (accountNumber.longValue() == transaction.getDepositAccount().getNumber()) {
					this.balance = transaction.getDepositAccountBalance();
				} else {
					this.balance = transaction.getWithdrawAccountBalance();
				}
			}
		}
	}

	// 출금 기록
	@Data
	public static class TransactionDepositDto {
		private Long id;
		private String type;
		private String sender;
		private String reciver;
		private Long amount;
		private String tel;
		private String createdAt;
		@JsonIgnore
		private Long depositAccountBalance; // 클라이언트에게 전달X -> 서비스단에서 테스트 용도

		public TransactionDepositDto(Transaction transaction) {
			this.id = transaction.getId();
			this.type = transaction.getType().getValue();
			this.sender = transaction.getSender();
			this.reciver = transaction.getReceiver();
			this.amount = transaction.getAmount();
			this.depositAccountBalance = transaction.getDepositAccountBalance();
			this.tel = transaction.getTel();
			this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
		}
	}

	// 입금 기록
	@Data
	public static class TransactionWithdrawDto {
		private Long id;
		private String type;
		private String sender;
		private String reciver;
		private Long amount;
		private String createdAt;

		public TransactionWithdrawDto(Transaction transaction) {
			this.id = transaction.getId();
			this.type = transaction.getType().getValue();
			this.sender = transaction.getSender();
			this.reciver = transaction.getReceiver();
			this.amount = transaction.getAmount();
			this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
		}
	}

	// 이체 기록
	@Data
	public static class TransactionTransferDto {
		private Long id;
		private String type;
		private String sender;
		private String reciver;
		private Long amount;
//		@JsonIgnore
		private Long depositAccountBalance; // 클라이언트에게 전달X -> 서비스단에서 테스트 용도
		private String createdAt;

		public TransactionTransferDto(Transaction transaction) {
			this.id = transaction.getId();
			this.type = transaction.getType().getValue();
			this.sender = transaction.getSender();
			this.reciver = transaction.getReceiver();
			this.amount = transaction.getAmount();
			this.depositAccountBalance = transaction.getDepositAccountBalance();
			this.createdAt = CustomDateUtil.toStringFormat(transaction.getCreatedAt());
		}
	}

	// 거래 내역 불러오기
	@Data
	public static class TransactionListResDto {

		private List<TransactionDto> transactions = new ArrayList<>();

		public TransactionListResDto(List<Transaction> transactions, Account account) {
			this.transactions = transactions.stream().map((transaction) -> new TransactionDto(transaction, account.getNumber())).collect(Collectors.toList());
		}

	}

}
