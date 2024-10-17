package com.bank.project.bank_project.domain.transaction;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.bank.project.bank_project.domain.account.Account;

import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Table(name = "transaction_tb")
@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Transaction {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@ManyToOne(fetch = FetchType.LAZY)
	private Account withdrawAccount;

	@JoinColumn(foreignKey = @ForeignKey(value = ConstraintMode.NO_CONSTRAINT))
	@ManyToOne(fetch = FetchType.LAZY)
	private Account depositAccount;
	
	@Column(nullable = false)
	private Long amount;
	
	private Long withdrawAccountBalance;
	private Long depositAccountBalance;
	
	@Column(nullable = false)
	@Enumerated(EnumType.STRING)
	private TransactionEnum type;
	
	private String sender;
	private String receiver;
	private String tel;
	
	@CreatedDate
	@Column(nullable = false)
	private LocalDateTime createdAt;

	@LastModifiedDate
	@Column(nullable = false)
	private LocalDateTime updatedAt;
	
}
