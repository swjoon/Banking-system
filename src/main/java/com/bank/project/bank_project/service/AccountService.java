package com.bank.project.bank_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.transaction.Transaction;
import com.bank.project.bank_project.domain.transaction.TransactionEnum;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountDepositReqDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountSaveReqDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountTransferReqDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountWithdrawReqDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountDepositResDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountListResDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountSaveResDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountTransferResDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountWithdrawResDto;
import com.bank.project.bank_project.handler.ex.CustomApiException;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.TransactionRepository;
import com.bank.project.bank_project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

	private final UserRepository userRepository;
	private final AccountRepository accountRepository;
	private final TransactionRepository transactionRepository;

	@Transactional
	public AccountSaveResDto 계좌등록(AccountSaveReqDto accountSaveReqDto, Long userId) {
		// DB 에 userId 있는지 여부확인
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

		// 계좌 중복 여부확인
		Optional<Account> opAccount = accountRepository.findByNumber(accountSaveReqDto.getNumber());

		if (opAccount.isPresent()) {
			throw new CustomApiException("해당 계좌가 이미 존재합니다.");
		}

		// 계좌 등록
		Account account = accountRepository.save(accountSaveReqDto.toEntity(user));

		// DTO 반환
		return new AccountSaveResDto(account);
	}

	public AccountListResDto 계좌목록보기_유저별(Long userId) {
		User user = userRepository.findById(userId).orElseThrow(() -> new CustomApiException("유저를 찾을 수 없습니다."));

		List<Account> accountList = accountRepository.findByUser_id(userId);

		return new AccountListResDto(user, accountList);
	}

	@Transactional
	public void 계좌삭제(Long number, Long userId) {
		// 1. 계좌확인
		Account account = accountRepository.findByNumber(number)
				.orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

		// 2. 계좌 소유자 확인
		account.checkOwner(userId);

		// 3. 계좌 삭제
		accountRepository.deleteById(account.getId());
	}

	@Transactional
	public AccountDepositResDto 계좌입금(AccountDepositReqDto accountDepositReqDto) {
		// 1. 입금 금액 확인
		if (accountDepositReqDto.getAmount() <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다.");
		}

		// 2. 계좌 존재여부 확인
		Account accountPS = accountRepository.findByNumber(accountDepositReqDto.getNumber())
				.orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

		// 3. 계좌 입금
		accountPS.deposit(accountDepositReqDto.getAmount());

		// 4. 거래내역 남기기

		Transaction transaction = Transaction.builder().withdrawAccount(null).depositAccount(accountPS)
				.withdrawAccountBalance(null).depositAccountBalance(accountPS.getBalance())
				.amount(accountDepositReqDto.getAmount()).type(TransactionEnum.DEPOSIT).sender("ATM")
				.receiver(accountDepositReqDto.getNumber() + "").tel(accountDepositReqDto.getTel()).build();

		Transaction transactionPS = transactionRepository.save(transaction);

		return new AccountDepositResDto(accountPS, transactionPS);
	}

	@Transactional
	public AccountWithdrawResDto 계좌출금(AccountWithdrawReqDto accountWithdrawReqDto, Long userId) {
		// 1. 출금 금액 확인
		if (accountWithdrawReqDto.getAmount() <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 출금할 수 없습니다.");
		}

		// 2. 계좌 존재여부 확인
		Account withdrawAccountPS = accountRepository.findByNumber(accountWithdrawReqDto.getNumber())
				.orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다."));

		// 3. 출금 계좌 소유자 확인
		withdrawAccountPS.checkOwner(userId);

		// 4. 계좌 비밀번호 확인
		withdrawAccountPS.checkPassword(accountWithdrawReqDto.getPassword());

		// 5. 계좌 잔액여부 확인
		withdrawAccountPS.checkBalance(accountWithdrawReqDto.getAmount());

		// 6. 계좌 출금
		withdrawAccountPS.withdraw(accountWithdrawReqDto.getAmount());

		// 7. 거래내역 남기기
		Transaction transaction = Transaction.builder().withdrawAccount(withdrawAccountPS).depositAccount(null)
				.withdrawAccountBalance(withdrawAccountPS.getBalance()).depositAccountBalance(null)
				.amount(accountWithdrawReqDto.getAmount()).type(TransactionEnum.WITHDRAW)
				.sender(accountWithdrawReqDto.getNumber() + "").receiver("ATM").build();
		
		Transaction transactionPS = transactionRepository.save(transaction);
		
		return new AccountWithdrawResDto(withdrawAccountPS, transactionPS);
	}
	
	@Transactional
	public AccountTransferResDto 계좌이체(AccountTransferReqDto accountTransferReqDto, Long userId) {
		// 1. 출금 계좌와 입금 계좌 동일 여부 체크
		if(accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber().longValue()) {
			throw new CustomApiException("입금계좌와 출금계좌가 동일합니다.");
		}
		
		// 2. 출금 금액 확인
		if (accountTransferReqDto.getAmount() <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 출금할 수 없습니다.");
		}

		// 3. 계좌 존재여부 확인
		Account withdrawAccountPS = accountRepository.findByNumber(accountTransferReqDto.getWithdrawNumber())
				.orElseThrow(() -> new CustomApiException("출금 계좌를 찾을 수 없습니다."));
		
		Account depositAccountPS = accountRepository.findByNumber(accountTransferReqDto.getDepositNumber())
				.orElseThrow(() -> new CustomApiException("입금 계좌를 찾을 수 없습니다."));

		// 4. 출금 계좌 소유자 확인
		withdrawAccountPS.checkOwner(userId);

		// 5. 계좌 비밀번호 확인
		withdrawAccountPS.checkPassword(accountTransferReqDto.getWithdrawPassword());

		// 6. 계좌 잔액여부 확인
		withdrawAccountPS.checkBalance(accountTransferReqDto.getAmount());

		// 7. 이체하기
		withdrawAccountPS.withdraw(accountTransferReqDto.getAmount());
		depositAccountPS.deposit(accountTransferReqDto.getAmount());

		// 8. 거래내역 남기기
		Transaction transaction = Transaction.builder().withdrawAccount(withdrawAccountPS).depositAccount(depositAccountPS)
				.withdrawAccountBalance(withdrawAccountPS.getBalance()).depositAccountBalance(depositAccountPS.getBalance())
				.amount(accountTransferReqDto.getAmount()).type(TransactionEnum.WITHDRAW)
				.sender(accountTransferReqDto.getWithdrawNumber() + "").receiver(accountTransferReqDto.getDepositNumber()+"").build();
		
		Transaction transactionPS = transactionRepository.save(transaction);

		return new AccountTransferResDto(withdrawAccountPS, transactionPS);
	}

}
