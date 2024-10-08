package com.bank.project.bank_project.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.bank.project.bank_project.dto.ResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

public class CustomResponseUtil {
	private final static Logger log = LoggerFactory.getLogger(CustomResponseUtil.class);

	public static void unAuthentication(HttpServletResponse response, String msg) {
		try {
			ObjectMapper om = new ObjectMapper();
			ResponseDto<?> responseDto = new ResponseDto<>(-1, msg, null);
			String responseBody = om.writeValueAsString(responseDto);
			response.setContentType("application/json; charset=utf-8");
			response.setStatus(401);
			response.getWriter().println(responseBody);			
		}catch(Exception e){
			log.error("서버 파싱 에러");
		}
	}
}
