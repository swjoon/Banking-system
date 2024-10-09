package com.bank.project.bank_project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bank.project.bank_project.dto.user.UserReqDto.JoinReqDto;
import com.fasterxml.jackson.databind.ObjectMapper;

@AutoConfigureMockMvc
@TestMethodOrder(OrderAnnotation.class)
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class UserControllerTest{

	@Autowired
	private MockMvc mvc;
	
	@Autowired
	private ObjectMapper om;
	
	@Test
	@Order(1)
	public void join_test() throws Exception{
		
		// given
		JoinReqDto joinReqDto = JoinReqDto.builder().username("testuser").password("12345678").email("email1@test.com").fullname("테스트").build();
		
		String requestBody = om.writeValueAsString(joinReqDto);
		
		// when
		ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		
//		String responseBody = resultActions.andReturn().getResponse().getContentAsString(null);
		
		// then
		resultActions.andExpect(status().isCreated());

	}
	
	@Test
	@Order(2)
	public void join_fail_test() throws Exception{
		
		// given
		JoinReqDto joinReqDto = JoinReqDto.builder().username("testuser").password("12345678").email("email@test.com").fullname("테스트").build();
		
		String requestBody = om.writeValueAsString(joinReqDto);
		
		// when
		ResultActions resultActions = mvc.perform(post("/api/join").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		
//		String responseBody = resultActions.andReturn().getResponse().getContentAsString(null);
		
		// then
		resultActions.andExpect(status().is5xxServerError());

	}
	
}
