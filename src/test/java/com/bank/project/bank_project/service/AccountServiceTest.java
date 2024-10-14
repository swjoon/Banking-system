package com.bank.project.bank_project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import com.bank.project.bank_project.config.dummy.DummyObject;
import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountSaveReqDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountListResDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountSaveResDto;
import com.bank.project.bank_project.handler.ex.CustomApiException;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {
	@InjectMocks // 모든 Mock들이 InjectMocks 로 주입됨
	private AccountService accountService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AccountRepository accountRepository;

	@Spy // 진짜 객체를 InjectMocks에 주입한다.
	private ObjectMapper om;

	@Test
	public void 계좌등록_test() throws Exception {
		// given
		Long userId = 1L;

		AccountSaveReqDto accountSaveReqDto = new AccountSaveReqDto();
		accountSaveReqDto.setNumber(1000100011L);
		accountSaveReqDto.setPassword(1234L);

		// stub 1L
		User test = newMockUser(userId, "test", "테스트");
		when(userRepository.findById(any())).thenReturn(Optional.of(test));

		// stub 2
		when(accountRepository.findByNumber(any())).thenReturn(Optional.empty());

		// stub 3
		Account testAccount = newMockAccount(1L, 1000100011L, 1000L, test);
		when(accountRepository.save(any())).thenReturn(testAccount);

		// when
		AccountSaveResDto accountSaveRespDto = accountService.계좌등록(accountSaveReqDto, userId);
		String responseBody = om.writeValueAsString(accountSaveRespDto);
		System.out.println("테스트 : " + responseBody);

		// then
		assertThat(accountSaveRespDto.getNumber()).isEqualTo(1000100011L);
	}
//	    
//	    @Test
//	    public void 계좌목록보기_유저별_test() throws Exception {
//	        // given
//	        Long userId = 1L;
//
//	        // stub
//	        User test = newMockUser(1L, "test", "테스트");
//	        when(userRepository.findById(userId)).thenReturn(Optional.of(test));
//
//	        Account testAccount1 = newMockAccount(1L, 1111L, 1000L, test);
//	        Account testAccount2 = newMockAccount(2L, 2222L, 1000L, test);
//	        List<Account> accountList = Arrays.asList(testAccount1, testAccount2);
//	        when(accountRepository.findByUser_id(any())).thenReturn(accountList);
//
//	        // when
//	        AccountListResDto accountListResDto = accountService.계좌목록보기_유저별(userId);
//
//	        // then
//	        assertThat(accountListResDto.getFullname()).isEqualTo("테스트");
//	        assertThat(accountListResDto.getAccounts().size()).isEqualTo(2);
//	    }

	@Test
	public void 계좌삭제_test() throws Exception {
		// given
		Long number = 1111L;
		Long userId = 2L;

		// stub
		User test = newMockUser(1L, "test", "테스트");
		Account testAccount = newMockAccount(1L, 1111L, 1000L, test);
		when(accountRepository.findByNumber(any())).thenReturn(Optional.of(testAccount));

		// when
		assertThrows(CustomApiException.class, () -> accountService.계좌삭제(number, userId));
	}
}
