package com.bank.project.bank_project.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountSaveReqDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountListResDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountSaveResDto;
import com.bank.project.bank_project.handler.ex.CustomApiException;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AccountService {

	private final UserRepository userRepository;
	private final AccountRepository accountRepository;

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
		User user = userRepository.findById(userId).orElseThrow(
				() -> new CustomApiException("유저를 찾을 수 없습니다."));
		
		List<Account> accountList = accountRepository.findByUser_id(userId);
		
		return new AccountListResDto(user, accountList);
	}
	
	@Transactional
	public void 계좌삭제(Long number, Long userId) {
		// 1. 계좌확인
		Account account = accountRepository.findByNumber(number).orElseThrow(
				()-> new CustomApiException("계좌를 찾을 수 없습니다."));
		// 2. 계좌 소유자 확인
		account.checkOwner(userId);
		// 3. 계좌 삭제
		accountRepository.deleteById(account.getId());
	}

}















