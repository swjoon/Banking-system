package com.bank.project.bank_project.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.bank.project.bank_project.dto.ResponseDto;
import com.bank.project.bank_project.handler.ex.CustomApiException;
import com.bank.project.bank_project.handler.ex.CustomForbiddenException;
import com.bank.project.bank_project.handler.ex.CustomValidationException;

@RestControllerAdvice
public class CustomExceptionHandler {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	@ExceptionHandler(CustomApiException.class)
	public ResponseEntity<?> apiException(CustomApiException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_GATEWAY);
	}
	
	@ExceptionHandler(CustomValidationException.class)
	public ResponseEntity<?> apiException(CustomValidationException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), e.getErrorMap()), HttpStatus.BAD_GATEWAY);
	}
	
	@ExceptionHandler(CustomForbiddenException.class)
	public ResponseEntity<?> forbiddenException(CustomForbiddenException e) {
		log.error(e.getMessage());
		return new ResponseEntity<>(new ResponseDto<>(-1, e.getMessage(), null), HttpStatus.BAD_GATEWAY);
	}
}
