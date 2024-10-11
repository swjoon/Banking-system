package com.bank.project.bank_project.config.jwt;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bank.project.bank_project.config.auth.LoginUser;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.domain.user.UserEnum;

//@Sql("classpath:db/teardown.sql")

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class JwtAuthorizationFilterTest {
	
    @Autowired
    private MockMvc mvc;

    @Test
    public void authorization_success_test() throws Exception {
    	System.out.println("테스트 : authorization_success_test()");
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);
        String jwtToken = JwtProcess.create(loginUser);

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/test").header(JwtVO.HEADER, jwtToken));

        // then
        resultActions.andExpect(status().isNotFound());
    }

    @Test
    public void authorization_fail_test() throws Exception {
    	System.out.println("테스트 : authorization_fail_test()");
        // given

        // when
        ResultActions resultActions = mvc.perform(get("/api/s/test"));

        // then
        resultActions.andExpect(status().isUnauthorized()); // 401
    }

    @Test
    public void authorization_admin_test() throws Exception {
    	System.out.println("테스트 : authorization_admin_test()");
        // given
        User user = User.builder().id(1L).role(UserEnum.CUSTOMER).build();
        LoginUser loginUser = new LoginUser(user);
        String jwtToken = JwtProcess.create(loginUser);

        // when
        ResultActions resultActions = mvc.perform(get("/api/admin/test").header(JwtVO.HEADER, jwtToken));

        // then
        resultActions.andExpect(status().isForbidden()); // 403
    }
}