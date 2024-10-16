package com.bank.project.bank_project.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.transaction.Transaction;
import com.bank.project.bank_project.domain.transaction.TransactionEnum;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.domain.user.UserEnum;
import com.bank.project.bank_project.repository.AccountRepository;

public class DummyObject {

	protected static User newUser(String username, String fullname) {

		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

		String encPassword = passwordEncoder.encode("12345678");

		return User.builder().username(username).password(encPassword).email(username + "@nate.com").fullname(fullname)
				.role(UserEnum.CUSTOMER).build();
	}

	protected User newMockUser(Long id, String username, String fullname) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("12345678");
		return User.builder().id(id).username(username).password(encPassword).email(username + "@nate.com")
				.fullname(fullname).role(UserEnum.CUSTOMER).createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now()).build();
	}

	protected User newMockAc(Long id, String username, String fullname) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encPassword = passwordEncoder.encode("12345678");
		return User.builder().id(id).username(username).password(encPassword).email(username + "@nate.com")
				.fullname(fullname).role(UserEnum.CUSTOMER).createdAt(LocalDateTime.now())
				.updatedAt(LocalDateTime.now()).build();
	}

	protected Account newAccount(Long number, User user) {
		return Account.builder().number(number).password(1234L).balance(1000L).user(user).build();
	}

	protected Account newMockAccount(Long id, Long number, Long balance, User user) {
		return Account.builder().id(id).number(number).password(1234L).balance(balance).user(user)
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
	}

	protected static Transaction newMockDepositTransaction(Long id, Account account) {
		account.deposit(100L);
		Transaction transaction = Transaction.builder().id(id).withdrawAccount(null).depositAccount(account)
				.withdrawAccountBalance(null).depositAccountBalance(account.getBalance()).amount(100L)
				.type(TransactionEnum.DEPOSIT).sender("ATM").receiver(account.getNumber() + "").tel("01012345678")
				.createdAt(LocalDateTime.now()).updatedAt(LocalDateTime.now()).build();
		return transaction;
	}

	protected Transaction newDepositTransaction(Account account, AccountRepository accountRepository) {
		account.deposit(100L); // 1000원이 있었다면 900원이 됨
		// 더티체킹이 안되기 때문에
		if (accountRepository != null) {
			accountRepository.save(account);
		}
		Transaction transaction = Transaction.builder().withdrawAccount(null).depositAccount(account)
				.withdrawAccountBalance(null).depositAccountBalance(account.getBalance()).amount(100L)
				.type(TransactionEnum.DEPOSIT).sender("ATM").receiver(account.getNumber() + "").tel("01012345678")
				.build();
		return transaction;
	}

	protected Transaction newWithdrawTransaction(Account account, AccountRepository accountRepository) {
		account.withdraw(100L); // 1000원이 있었다면 900원이 됨

		// Repository Test에서는 더티체킹 됨
		// Controller Test에서는 더티체킹 안됨
		if (accountRepository != null) {
			accountRepository.save(account);
		}
		Transaction transaction = Transaction.builder().withdrawAccount(account).depositAccount(null)
				.withdrawAccountBalance(account.getBalance()).depositAccountBalance(null).amount(100L)
				.type(TransactionEnum.WITHDRAW).sender(account.getNumber() + "").receiver("ATM").build();
		return transaction;
	}

	protected Transaction newTransferTransaction(Account withdrawAccount, Account depositAccount,
			AccountRepository accountRepository) {
		withdrawAccount.withdraw(100L);
		depositAccount.deposit(100L);
		// 더티체킹이 안되기 때문에
		if (accountRepository != null) {
			accountRepository.save(withdrawAccount);
			accountRepository.save(depositAccount);
		}
		Transaction transaction = Transaction.builder().withdrawAccount(withdrawAccount).depositAccount(depositAccount)
				.withdrawAccountBalance(withdrawAccount.getBalance()).depositAccountBalance(depositAccount.getBalance())
				.amount(100L).type(TransactionEnum.TRANSFER).sender(withdrawAccount.getNumber() + "")
				.receiver(depositAccount.getNumber() + "").build();
		return transaction;
	}
}
