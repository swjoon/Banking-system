package com.bank.project.bank_project.config.jwt;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.bank.project.bank_project.config.auth.LoginUser;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.domain.user.UserEnum;

public class JwtProcess {
	private final Logger log = LoggerFactory.getLogger(getClass());

	public static String create(LoginUser loginUser) {
		String jwtToken = JWT.create().withSubject("bank")
				.withExpiresAt(new Date(System.currentTimeMillis() + JwtVO.EXPIRATION_TIME))
				.withClaim("id", loginUser.getUser().getId())
				.withClaim("role", loginUser.getUser().getRole() + "")
				.sign(Algorithm.HMAC512(JwtVO.SECRET));
		return JwtVO.TOKEN_PREFIX + jwtToken;

	}

	public static LoginUser verify(String Token) {
		DecodedJWT decodedJWT = JWT.require(Algorithm.HMAC512(JwtVO.SECRET)).build().verify(Token);
		Long id = decodedJWT.getClaim("id").asLong();
		String role = decodedJWT.getClaim("role").asString();
		User user = User.builder().id(id).role(UserEnum.valueOf(role)).build();
		LoginUser loginUser = new LoginUser(user);
		return loginUser;
	}

}
