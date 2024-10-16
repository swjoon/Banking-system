package com.bank.project.bank_project.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.bank.project.bank_project.config.jwt.JwtAuthenticationFilter;
import com.bank.project.bank_project.config.jwt.JwtAuthorizationFilter;
import com.bank.project.bank_project.domain.user.UserEnum;
import com.bank.project.bank_project.util.CustomResponseUtil;

@Configuration
public class SecurityConfig {
	private final Logger log = LoggerFactory.getLogger(getClass());

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		log.debug(" 로그 : BCryptPaawordEncoder 빈 등록됨 ");
		return new BCryptPasswordEncoder();
	}
	
	@Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		log.debug(" 로그 : filterChain 빈 등록됨");
		
		http.headers(head -> head.frameOptions(option -> option.disable()))
				.cors(cors -> cors.configurationSource(configurationSource())) // CORS 설정
				.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.formLogin(login -> login.disable()).httpBasic(basic -> basic.disable())
				
				 .addFilter(new JwtAuthenticationFilter(authenticationManager(http)))
				 .addFilter(new JwtAuthorizationFilter(authenticationManager(http)))

				// 기본적인 spring security 는 인증,권한과 관련된 문제가 발생시 ExceptionTranslationFilter 여기로 넘긴다
				// 제어권 가져오기 ↓
				.exceptionHandling(except -> except.authenticationEntryPoint((request, response, authException) -> {
					CustomResponseUtil.fail(response, "로그인을 진행해 주세요",HttpStatus.UNAUTHORIZED);
				}))
				
				// 권한 제어
				.exceptionHandling(except -> except.accessDeniedHandler((request, response, authException) -> {
					CustomResponseUtil.fail(response, "권한이 없습니다",HttpStatus.FORBIDDEN);
				}))
				
				.authorizeHttpRequests(request -> request.requestMatchers("/api/test/**").authenticated()
						.requestMatchers("/api/admin/**").hasRole("" + UserEnum.ADMIN).anyRequest().permitAll());

		return http.build();
	}

	// CORS 설정
	public CorsConfigurationSource configurationSource() {

		log.debug(" 로그 : CorsConfigurationSource 설정이 filterChain에 등록됨");

		CorsConfiguration configuration = new CorsConfiguration();

		configuration.addAllowedHeader("*");
		configuration.addAllowedMethod("*"); // GET, POST ..
		configuration.addAllowedOrigin("*"); // IP
		configuration.setAllowCredentials(true); // 클라이언트에서 쿠키 요청 허용
		configuration.addExposedHeader("Authorization"); // 옛날에는 default
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
