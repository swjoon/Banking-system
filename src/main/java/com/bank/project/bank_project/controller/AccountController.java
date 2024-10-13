package com.bank.project.bank_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.project.bank_project.config.auth.LoginUser;
import com.bank.project.bank_project.dto.ResponseDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountSaveReqDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountSaveResDto;
import com.bank.project.bank_project.service.AccountService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class AccountController {

	private final AccountService accountService;

	@PostMapping("/test/account")
	public ResponseEntity<?> saveAccount(@RequestBody @Valid AccountSaveReqDto accountSaveReqDto,
			BindingResult bindingResult, @AuthenticationPrincipal LoginUser loginUser) {

		AccountSaveResDto accountSaveResDto = accountService.계좌등록(accountSaveReqDto, loginUser.getUser().getId());

		return new ResponseEntity<>(new ResponseDto<>(1, "계좌 생성을 완료했습니다.", accountSaveResDto), HttpStatus.CREATED);
	}

}
