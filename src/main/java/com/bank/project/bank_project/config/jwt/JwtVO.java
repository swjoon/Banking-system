package com.bank.project.bank_project.config.jwt;

public interface JwtVO {

	// 배포시 환경변수로 바꿔야함
	public static final String SECRET = "fe5d05c7-8c3d-4421-924a-bc7a8993dc55";
	public static final int EXPIRATION_TIME = 1000 * 60 * 60 * 24 * 7;
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER = "Authorization";
	
}
