package com.bank.project.bank_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.project.bank_project.config.auth.LoginUser;
import com.bank.project.bank_project.dto.ResponseDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountSaveReqDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountListResDto;
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

	@GetMapping("/test/account/{id}")
	public ResponseEntity<?> findUserAccount(@PathVariable("id") Long id, @AuthenticationPrincipal LoginUser loginUser){
		//		if(id != loginUser.getUser().getId()) -> throw 에러 // 권한처리 여부 고민. -> 
		
		AccountListResDto accountListResDto = accountService.계좌목록보기_유저별(loginUser.getUser().getId());
		
		return new ResponseEntity<>(new ResponseDto<>(1, "계좌 목록 불러오기 완료.", accountListResDto), HttpStatus.OK); 
	}
	
	@DeleteMapping("/test/account/{number}")
	public ResponseEntity<?> deleteAccount(@PathVariable("number") Long number, @AuthenticationPrincipal LoginUser loginUser){
		accountService.계좌삭제(number, loginUser.getUser().getId());
		return new ResponseEntity<>(new ResponseDto<>(1, "계좌 삭제 완료.", null), HttpStatus.OK); 
	}
	
}
