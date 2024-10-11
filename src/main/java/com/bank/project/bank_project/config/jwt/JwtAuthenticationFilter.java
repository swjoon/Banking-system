package com.bank.project.bank_project.config.jwt;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.bank.project.bank_project.config.auth.LoginUser;
import com.bank.project.bank_project.dto.user.UserReqDto.LoginReqDto;
import com.bank.project.bank_project.dto.user.UserResDto.LoginResDto;
import com.bank.project.bank_project.util.CustomResponseUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	private final Logger log = LoggerFactory.getLogger(getClass());
	
	private AuthenticationManager authenticationManager;

	public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
		super(authenticationManager);
		setFilterProcessesUrl("/api/login"); // filter 로그인 url 변경
		this.authenticationManager = authenticationManager;
	}

	// POST : /api/login
	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException {

		try {
			log.debug(" 로그 : attemptAuthentication 호출");
			ObjectMapper om = new ObjectMapper();
			LoginReqDto loginReqDto = om.readValue(request.getInputStream(), LoginReqDto.class);

			// 강제 로그인
			UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
					loginReqDto.getUsername(), loginReqDto.getPassword());

			// UserDetailsService의 loadUserByUsername 호출
			Authentication authentication = authenticationManager.authenticate(authenticationToken);
			return authentication;
		} catch (Exception e) {
			// securityfilterchain의 authenticationEntryPoint부분에 걸린다
			throw new InternalAuthenticationServiceException(e.getMessage());
		}
	}
	
	// 로그인 실패
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        CustomResponseUtil.fail(response, "로그인 실패", HttpStatus.UNAUTHORIZED);
    }

	// 위 메서드 성공시 호출
	@Override
	protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
			Authentication authResult) throws IOException, ServletException {
		
		log.debug(" 로그 : successfulAuthentication 호출");
		
		LoginUser loginUser = (LoginUser) authResult.getPrincipal();
		String jwtToken = JwtProcess.create(loginUser);
		response.addHeader(JwtVO.HEADER, jwtToken);

		LoginResDto loginResDto = new LoginResDto(loginUser.getUser());
		
		CustomResponseUtil.success(response, loginResDto);
	}

}
