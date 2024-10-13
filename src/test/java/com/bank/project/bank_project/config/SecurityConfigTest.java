package com.bank.project.bank_project.config;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

// 서버는 일관성있게 에러가 리턴되어야함
// 내가 모르는 에러가 프론트에게 날라가지 않게, 전부 제어.

@AutoConfigureMockMvc // Mock 환경에 MockMvc가 등록됨
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class SecurityConfigTest {
	
	// 가상환경에 등록된 MockMvc를 DI 
	@Autowired
	private MockMvc mvc;
	
	@Test
	public void authentication_test() throws Exception {
		// given
		
		
		// when
		ResultActions resultActions = mvc.perform(get("/api/test/test"));
        String responseBody = resultActions.andReturn().getResponse().getContentAsString();
        int httpStatusCode = resultActions.andReturn().getResponse().getStatus();
        System.out.println("테스트 : " + responseBody);
        System.out.println("테스트 : " + httpStatusCode);

        // then
        assertThat(httpStatusCode).isEqualTo(401);

	}
	
	@Test
	public void authorization_test()throws Exception {
		// given
		
		
		// when
		ResultActions resultActions = mvc.perform(get("/api/admin/test"));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		int httpStatusCode = resultActions.andReturn().getResponse().getStatus();

		System.out.println("테스트 : " + responseBody);
		System.out.println("테스트 : " + httpStatusCode);

		// then
		assertThat(httpStatusCode).isEqualTo(401);

	}
}
