package com.bank.project.bank_project.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountDepositReqDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountSaveReqDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountTransferReqDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountWithdrawReqDto;
import com.bank.project.bank_project.handler.ex.CustomApiException;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.UserRepository;
import com.bank.project.bank_project.repository.transaction.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.EntityManager;

//@Transactional
@Sql("classpath:db/teardown.sql")
@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = WebEnvironment.MOCK)
public class AccountControllerTest extends DummyObject {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private ObjectMapper om;

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

	// jwt token -> 인증필터 -> 시큐리티 세션생성
	// setupBefore=TEST_METHOD (setUp 메서드 실행전에 수행)
	// setupBefore=TEST_EXECUTION (saveAccount_test 메서드 실행전에 수행)
	@WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION) // 디비에서 value 값을 조회해서 세션에 담아줌
	@Test
	public void saveAccount_test() throws Exception {
		// given
		AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
		accountSaveReqDto.setNumber(9999L);
		accountSaveReqDto.setPassword(1234L);
		String requestBody = om.writeValueAsString(accountSaveReqDto);
		System.out.println("테스트 : " + requestBody);

		// when
		ResultActions resultActions = mvc
				.perform(post("/api/test/account").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println("테스트 : " + responseBody);

		// then
		resultActions.andExpect(status().isCreated());
	}

	@WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	public void findUserAccount_test() throws Exception {
		// given
		Long id = 1L;
		// when
		ResultActions resultActions = mvc.perform(get("/api/test/account/" + id));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println("테스트 : " + responseBody);

		// then
		resultActions.andExpect(status().isOk());
	}

	@WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	@Test
	public void deleteAccount_test() throws Exception {
		// given
		Long number = 1111L;

		// when
		ResultActions resultActions = mvc.perform(delete("/api/test/account/" + number));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println("테스트 : " + responseBody);

		// then
		// Junit 테스트에서 delete 쿼리 로그는 DB관련(DML)으로 가장 마지막에 실행되면 발동안됨.
		assertThrows(CustomApiException.class, () -> accountRepository.findByNumber(number)
				.orElseThrow(() -> new CustomApiException("계좌를 찾을 수 없습니다")));

	}

	@Test
	public void depositAccount_test() throws Exception {
		// given
		AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
		accountDepositReqDto.setNumber(1111L);
		accountDepositReqDto.setAmount(100L);
		accountDepositReqDto.setType("DEPOSIT");
		accountDepositReqDto.setTel("01012345678");

		String requestBody = om.writeValueAsString(accountDepositReqDto);
		System.out.println("테스트 : " + requestBody);

		// when
		ResultActions resultActions = mvc
				.perform(post("/api/account/deposit").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println("테스트 : " + responseBody);

		// then
		resultActions.andExpect(status().isCreated());
	}

	@Test
	@WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	public void withdrawAccount_test() throws Exception {
		// given
		AccountWithdrawReqDto accountWithdrawReqDto = new AccountWithdrawReqDto();
		accountWithdrawReqDto.setNumber(1111L);
		accountWithdrawReqDto.setPassword(1234L);
		accountWithdrawReqDto.setAmount(100L);
		accountWithdrawReqDto.setType("WITHDRAW");

		String requestBody = om.writeValueAsString(accountWithdrawReqDto);
		System.out.println("테스트 : " + requestBody);

		// when
		ResultActions resultActions = mvc
				.perform(post("/api/account/withdraw").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println("테스트 : " + responseBody);

		// then
		resultActions.andExpect(status().isCreated());
	}

	@Test
	@WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	public void transferAccount_test() throws Exception {
		// given
		AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
		accountTransferReqDto.setWithdrawNumber(1111L);
		accountTransferReqDto.setDepositNumber(2222L);
		accountTransferReqDto.setWithdrawPassword(1234L);
		accountTransferReqDto.setAmount(100L);
		accountTransferReqDto.setType("TRANSFER");

		String requestBody = om.writeValueAsString(accountTransferReqDto);
		System.out.println("테스트 : " + requestBody);

		// when
		ResultActions resultActions = mvc
				.perform(post("/api/account/transfer").content(requestBody).contentType(MediaType.APPLICATION_JSON));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println("테스트 : " + responseBody);

		// then
		resultActions.andExpect(status().isCreated());
	}
	
	@Test
	@WithUserDetails(value = "test1", setupBefore = TestExecutionEvent.TEST_EXECUTION)
	public void findDetailAccount_test() throws Exception {
		// given
		Long number = 1111L;
		String page = "0";
		
		// when
		ResultActions resultActions = mvc
				.perform(get("/api/test/account/detail/" + number).param("page", page));
		String responseBody = resultActions.andReturn().getResponse().getContentAsString();
		System.out.println("테스트 : " + responseBody);

		// then
		resultActions.andExpect(jsonPath("$.data.transactions[0].balance").value(900L));
		resultActions.andExpect(jsonPath("$.data.transactions[1].balance").value(800L));
		resultActions.andExpect(jsonPath("$.data.transactions[2].balance").value(700L));
		resultActions.andExpect(jsonPath("$.data.transactions[3].balance").value(800L));
	}
	

}
