package com.bank.project.bank_project.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bank.project.bank_project.dto.ResponseDto;
import com.bank.project.bank_project.dto.user.UserReqDto.JoinReqDto;
import com.bank.project.bank_project.dto.user.UserResDto.JoinResDto;
import com.bank.project.bank_project.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class UserController {
	
	private final UserService userService;
	
	@PostMapping("/join")
	public ResponseEntity<?> join(@RequestBody @Valid JoinReqDto joinReqDto, BindingResult bindingResult) {
		JoinResDto joinResDto = userService.회원가입(joinReqDto);
		return new ResponseEntity<>(new ResponseDto<>(1, "회원가입 성공", joinResDto),HttpStatus.CREATED);
	}
	
	
	
}
