package com.bank.project.bank_project.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bank.project.bank_project.config.auth.LoginUser;
import com.bank.project.bank_project.dto.ResponseDto;
import com.bank.project.bank_project.dto.transaction.TransactionResDto.TransactionListResDto;
import com.bank.project.bank_project.service.TransactionService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class TransactionController {

	private final TransactionService transactionService;

	@GetMapping("/test/account/{number}/transaction")
	public ResponseEntity<?> findTransactionList(@PathVariable("number") Long number,
			@RequestParam(value = "type" , defaultValue = "ALL") String type,
			@RequestParam(value = "page", defaultValue = "0") Integer page,
			@AuthenticationPrincipal LoginUser loginUser) {

		TransactionListResDto transactionListResDto = transactionService.입출금목록보기(loginUser.getUser().getId(), number,
				type, page);

		return ResponseEntity.ok().body(new ResponseDto<>(1, "입출금 목록 불러오기 성공", transactionListResDto));
	}
}
