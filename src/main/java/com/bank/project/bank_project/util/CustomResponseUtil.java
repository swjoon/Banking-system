package com.bank.project.bank_project.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import com.bank.project.bank_project.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class CustomResponseUtil {
	private final static Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);
	
	public static void success(HttpServletResponse response, Object dto) {
		try {
			ObjectMapper om = new ObjectMapper();
			ResponseDto<?> responseDto = new ResponseDto<>(1, "로그인 성공", dto);
			String responseBody = om.writeValueAsString(responseDto);
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(200);
			response.getWriter().println(responseBody);			
		}catch(Exception e){
			log.error("서버 파싱 에러");
		}
	}
	
	public static void fail(HttpServletResponse response, String msg, HttpStatus httpStatus) {
        try {
            ObjectMapper om = new ObjectMapper();
            ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
            String responseBody = om.writeValueAsString(responseDto);
            response.setContentType("application/json; charset=utf-8");
            response.setStatus(httpStatus.value());
            response.getWriter().println(responseBody); // 예쁘게 메시지를 포장하는 공통적인 응답 DTO를 만들어보자!!
        } catch (Exception e) {
            log.error("서버 파싱 에러");
        }
    
	}
}