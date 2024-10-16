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
import com.bank.project.bank_project.domain.transaction.Transaction;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountDepositReqDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountSaveReqDto;
import com.bank.project.bank_project.dto.Account.AccountReqDto.AccountTransferReqDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountDepositResDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountListResDto;
import com.bank.project.bank_project.dto.Account.AccountResDto.AccountSaveResDto;
import com.bank.project.bank_project.handler.ex.CustomApiException;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.UserRepository;
import com.bank.project.bank_project.repository.transaction.TransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
public class AccountServiceTest extends DummyObject {
	@InjectMocks // 모든 Mock들이 InjectMocks 로 주입됨
	private AccountService accountService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private AccountRepository accountRepository;

	@Mock
	private TransactionRepository transactionRepository;

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

	@Test
	public void 계좌목록보기_유저별_test() throws Exception {
		// given
		Long userId = 1L;

		// stub
		User test = newMockUser(1L, "test", "테스트");
		when(userRepository.findById(userId)).thenReturn(Optional.of(test));

		Account testAccount1 = newMockAccount(1L, 1111L, 1000L, test);
		Account testAccount2 = newMockAccount(2L, 2222L, 1000L, test);
		List<Account> accountList = Arrays.asList(testAccount1, testAccount2);
		when(accountRepository.findByUser_id(any())).thenReturn(accountList);

		// when
		AccountListResDto accountListResDto = accountService.계좌목록보기_유저별(userId);

		// then
		assertThat(accountListResDto.getFullname()).isEqualTo("테스트");
		assertThat(accountListResDto.getAccounts().size()).isEqualTo(2);
	}

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

	@Test
	public void 계좌입금_test() throws Exception {
		// given
		AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
		accountDepositReqDto.setNumber(1111L);
		accountDepositReqDto.setAmount(100L);
		accountDepositReqDto.setType("DEPOSIT");
		accountDepositReqDto.setTel("01088887777");

		// stub 1
		User test = newMockUser(1L, "test", "테스트"); // 실행됨
		Account testAccount1 = newMockAccount(1L, 1111L, 1000L, test); // 실행됨 - ssarAccount1 -> 1000원
		when(accountRepository.findByNumber(any())).thenReturn(Optional.of(testAccount1)); // accountService가 호출되어야 실행

		System.out.println("테스트 : account1 입금 전 계좌 잔액 : " + testAccount1.getBalance()); // 위의 when 은 아직 실행안된상태. -> 잔액
																						// 1000

		// stub 2 (스텁이 진행될 때 마다 연관된 객체는 새로 만들어서 주입하기 - 타이밍 때문에 꼬인다)
		Account testAccount2 = newMockAccount(1L, 1111L, 2000L, test); // 실행됨 - ssarAccount1 -> 1000원
		Transaction transaction = newMockDepositTransaction(1L, testAccount2);
		System.out.println("테스트 : account2 입금 전 계좌 잔액 : " + testAccount2.getBalance()); // 위의 when 은 아직 실행안된상태. -> 잔액
																						// 2000
		when(transactionRepository.save(any())).thenReturn(transaction); // 실행안됨

		// when
		AccountDepositResDto accountDepositResDto = accountService.계좌입금(accountDepositReqDto);
		System.out.println("테스트 : 트랜잭션 입금계좌 잔액 : " + accountDepositResDto.getTransaction().getDepositAccountBalance());
		System.out.println("테스트 : account1 계좌 잔액 : " + testAccount1.getBalance());
		System.out.println("테스트 : account2 계좌 잔액 : " + testAccount2.getBalance());

		// then
		assertThat(testAccount1.getBalance()).isEqualTo(1100L);
		assertThat(accountDepositResDto.getTransaction().getDepositAccountBalance()).isEqualTo(2100L);
	}

	@Test
	public void 계좌입금_test2() throws Exception {
		// given
		AccountDepositReqDto accountDepositReqDto = new AccountDepositReqDto();
		accountDepositReqDto.setNumber(1111L);
		accountDepositReqDto.setAmount(1000L);
		accountDepositReqDto.setType("DEPOSIT");
		accountDepositReqDto.setTel("01012345678");

		// stub 1
		User test = newMockUser(1L, "test", "테스트"); // 실행됨
		Account testAccount1 = newMockAccount(1L, 1111L, 1000L, test); // 실행됨 - ssarAccount1 -> 1000원
		when(accountRepository.findByNumber(any())).thenReturn(Optional.of(testAccount1));

		// stub 2
		User test2 = newMockUser(1L, "test", "테스트"); // 실행됨
		Account testAccount2 = newMockAccount(1L, 1111L, 1000L, test2); // 실행됨 - ssarAccount1 -> 1000원
		Transaction transaction = newMockDepositTransaction(1L, testAccount2);
		when(transactionRepository.save(any())).thenReturn(transaction); // 실행안됨

		// when
		AccountDepositResDto accountDepositResDto = accountService.계좌입금(accountDepositReqDto);
		String responseBody = om.writeValueAsString(accountDepositResDto);
		System.out.println("테스트 : " + responseBody);

		// then
		assertThat(testAccount1.getBalance()).isEqualTo(2000L);
	}

	@Test
	public void 계좌입금_test3() throws Exception {
		// given
		Account account = newMockAccount(1L, 1111L, 1000L, null);
		Long amount = 100L;

		// when
		if (amount <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다");
		}
		account.deposit(100L);

		// then
		assertThat(account.getBalance()).isEqualTo(1100L);
	}

	@Test
	public void 계좌출금_test() throws Exception {
		// given
		Long amount = 100L;
		Long password = 1234L;
		Long userId = 1L;

		User test = newMockUser(1L, "test", "테스트");
		Account testAccount = newMockAccount(1L, 1111L, 1000L, test);

		// when
		if (amount <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다");
		}
		testAccount.checkOwner(userId);
		testAccount.checkPassword(password);
		testAccount.withdraw(amount);

		// then
		assertThat(testAccount.getBalance()).isEqualTo(900L);
	}

	@Test
	public void 계좌이체_test() throws Exception {
		// given
		Long userId = 1L;
		AccountTransferReqDto accountTransferReqDto = new AccountTransferReqDto();
		accountTransferReqDto.setWithdrawNumber(1111L);
		accountTransferReqDto.setDepositNumber(2222L);
		accountTransferReqDto.setWithdrawPassword(1234L);
		accountTransferReqDto.setAmount(100L);
		accountTransferReqDto.setType("TRANSFER");

		User test1 = newMockUser(1L, "test1", "테스트1");
		User test2 = newMockUser(2L, "test2", "테스트2");
		Account withdrawAccount = newMockAccount(1L, 1111L, 1000L, test1);
		Account depositAccount = newMockAccount(2L, 2222L, 1000L, test2);

		// when
		// 출금계좌와 입금계좌가 동일하면 안됨
		if (accountTransferReqDto.getWithdrawNumber().longValue() == accountTransferReqDto.getDepositNumber()
				.longValue()) {
			throw new CustomApiException("입출금계좌가 동일할 수 없습니다");
		}

		// 0원 체크
		if (accountTransferReqDto.getAmount() <= 0L) {
			throw new CustomApiException("0원 이하의 금액을 입금할 수 없습니다");
		}

		// 출금 소유자 확인 (로그인한 사람과 동일한지)
		withdrawAccount.checkOwner(userId);

		// 출금계좌 비빌번호 확인
		withdrawAccount.checkPassword(accountTransferReqDto.getWithdrawPassword());

		// 출금계좌 잔액 확인
		withdrawAccount.checkBalance(accountTransferReqDto.getAmount());

		// 이체하기
		withdrawAccount.withdraw(accountTransferReqDto.getAmount());
		depositAccount.deposit(accountTransferReqDto.getAmount());

		// then
		assertThat(withdrawAccount.getBalance()).isEqualTo(900L);
		assertThat(depositAccount.getBalance()).isEqualTo(1100L);
	}

	// 계좌목록보기_유저별_테스트 (서비스)

	// 계좌상세보기_테스트 (서비스)

}
