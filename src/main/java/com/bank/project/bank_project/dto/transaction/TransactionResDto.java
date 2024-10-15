package com.bank.project.bank_project.dto.transaction;

import com.bank.project.bank_project.domain.transaction.Transaction;
import com.bank.project.bank_project.util.CustomDateUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Data;

public class TransactionResDto {

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
}
