package com.bank.project.bank_project.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class ResponseDto<T> {

	private final Integer code;
	private final String msg;
	private final T data;
	
}
