package com.bank.project.bank_project.domain.account;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.handler.ex.CustomApiException;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "account_tb")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false, length = 10)
	private Long number;

	@Column(nullable = false, length = 4)
	private Long password;

	@Column(nullable = false)
	private Long balance;

	@ManyToOne(fetch = FetchType.LAZY)
	private User user;

	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;

	public void deposit(Long amount) {
		balance = balance + amount;
	}
	
	public void withdraw(Long amount) {
		checkBalance(amount);
		balance -= amount;
	}

	public void checkOwner(Long userId) {
		if (user.getId().longValue() != userId.longValue()) { // Lazy 로딩어이어도 id를 조회할 때는 select 쿼리가 날라가지 않는다.
			throw new CustomApiException("계좌 소유자가 아닙니다.");
		}
	}

	public void checkPassword(Long password) {
		if(this.password.longValue() != password.longValue()) {
			throw new CustomApiException("계좌 비밀번호가 일치하지 않습니다.");
		}
	}

	public void checkBalance(Long amount) {
		if(this.balance.longValue() < amount.longValue()) {
			throw new CustomApiException("계좌 잔액이 부족합니다.");
		}
	}

}
