package com.bank.project.bank_project.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.test.context.support.TestExecutionEvent;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import com.bank.project.bank_project.config.dummy.DummyObject;
import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.transaction.Transaction;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.UserRepository;
import com.bank.project.bank_project.repository.transaction.TransactionRepository;

import jakarta.persistence.EntityManager;

@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class TransactionControllerTest extends DummyObject {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private EntityManager em;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	private void dataSetting() {
		User test1 = userRepository.save(newUser("test1", "테스트1"));
		User test2 = userRepository.save(newUser("test2", "테스트2"));
		User test3 = userRepository.save(newUser("test3", "테스트3"));
		User admin = userRepository.save(newUser("admin", "관리자"));

		Account test1Account1 = accountRepository.save(newAccount(1111L, test1));
		Account test2Account = accountRepository.save(newAccount(2222L, test2));
		Account test3Account = accountRepository.save(newAccount(3333L, test3));
		Account test1Account2 = accountRepository.save(newAccount(4444L, test1));

		Transaction withdrawTransaction1 = transactionRepository
				.save(newWithdrawTransaction(test1Account1, accountRepository));
		Transaction depositTransaction1 = transactionRepository
				.save(newDepositTransaction(test2Account, accountRepository));
		Transaction transferTransaction1 = transactionRepository
				.save(newTransferTransaction(test1Account1, test2Account, accountRepository));
		Transaction transferTransaction2 = transactionRepository
				.save(newTransferTransaction(test1Account1, test3Account, accountRepository));
		Transaction transferTransaction3 = transactionRepository
				.save(newTransferTransaction(test2Account, test1Account1, accountRepository));
	}

	@BeforeEach
	public void setUp() {
		dataSetting();
		em.clear();
	}

	@Test
	@WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	public void findTransactionList_test() throws Exception {
		Long number = 1111L;
		String type = "ALL";
		String page = "0";
		
		ResultActions resultActions = mvc
				.perform(get("/api/test/account/" + number + "/transaction").param("type", type).param("page", page));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println("테스트 : " + responseBody);

		resultActions.andExpect(jsonPath("$.data.transactions[0].balance").value(900L));
		resultActions.andExpect(jsonPath("$.data.transactions[1].balance").value(800L));
		resultActions.andExpect(jsonPath("$.data.transactions[2].balance").value(700L));
		resultActions.andExpect(jsonPath("$.data.transactions[3].balance").value(800L));
	}
	

}
