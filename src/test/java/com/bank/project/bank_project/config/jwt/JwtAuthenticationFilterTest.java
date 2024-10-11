package com.bank.project.bank_project.config.jwt;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bank.project.bank_project.config.dummy.DummyObject;
import com.bank.project.bank_project.dto.user.UserReqDto.LoginReqDto;
import com.bank.project.bank_project.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;

//SpringBootTest 하는 곳에는 전부다 teardown.sql 을 붙여주자.
//@Sql("classpath:db/teardown.sql") // 실행시점 : BeforeEach 실행 직전마다
@Transactional
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class JwtAuthenticationFilterTest extends DummyObject {

	@Autowired
	private ObjectMapper om;
	@Autowired
	private MockMvc mvc;
	@Autowired
	private UserRepository userRepository;

	@BeforeEach
	public void setUp() throws Exception {
		userRepository.save(newUser("test", "테스트"));
	}

	@Test
	public void successfulAuthentication_test() throws Exception {
		// given
		LoginReqDto loginReqDto = new LoginReqDto();
		loginReqDto.setUsername("test");
		loginReqDto.setPassword("12345678");
		String requestBody = om.writeValueAsString(loginReqDto);
		System.out.println("테스트 : " + requestBody);

		// when
		ResultActions resultActions = mvc
				.perform(post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);
		System.out.println("테스트 : " + responseBody);
		System.out.println("테스트 : " + jwtToken);

		// then
		resultActions.andExpect(status().isOk());
		assertNotNull(jwtToken);
		assertTrue(jwtToken.startsWith(JwtVO.TOKEN_PREFIX));
		resultActions.andExpect(jsonPath("$.data.username").value("test"));
	} // 롤백

	@Test
	public void unsuccessfulAuthentication_test() throws Exception {
		// given
		LoginReqDto loginReqDto = new LoginReqDto();
		loginReqDto.setUsername("test");
		loginReqDto.setPassword("12345");
		String requestBody = om.writeValueAsString(loginReqDto);
		System.out.println("테스트 : " + requestBody);

		// when
		ResultActions resultActions = mvc
				.perform(post("/api/login").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		String jwtToken = resultActions.andReturn().getResponse().getHeader(JwtVO.HEADER);
		System.out.println("테스트 : " + responseBody);
		System.out.println("테스트 : " + jwtToken);

		// then
		resultActions.andExpect(status().isUnauthorized());
	}
}