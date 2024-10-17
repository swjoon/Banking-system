package com.bank.project.bank_project.domain.transaction;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import com.bank.project.bank_project.config.dummy.DummyObject;
import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.UserRepository;
import com.bank.project.bank_project.repository.transaction.TransactionRepository;

import jakarta.persistence.EntityManager;

@ActiveProfiles("test")
@DataJpaTest // DB 관련된 Bean이 다 올라온다.
public class TransactionRepositoryImplTest extends DummyObject {

	@Autowired
	private EntityManager em;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private TransactionRepository transactionRepository;

	private void autoIncrementReset() {
		em.createNativeQuery("ALTER TABLE user_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
		em.createNativeQuery("ALTER TABLE account_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
		em.createNativeQuery("ALTER TABLE transaction_tb ALTER COLUMN id RESTART WITH 1").executeUpdate();
	}

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
		autoIncrementReset();
		dataSetting();
		em.clear(); // 레포테스트에서 필수
	}

	@Test
	public void dataJpa_test1() {
		List<Transaction> transactionList = transactionRepository.findAll();
		transactionList.forEach((transaction) -> {
			System.out.println("테스트 : " + transaction.getId());
			System.out.println("테스트 : " + transaction.getSender());
			System.out.println("테스트 : " + transaction.getReceiver());
			System.out.println("테스트 : " + transaction.getType());
			System.out.println("테스트 : ========================");
		});
	}

	@Test
	public void dataJpa_test2() {
		List<Transaction> transactionList = transactionRepository.findAll();
		transactionList.forEach((transaction) -> {
			System.out.println("테스트 : " + transaction.getId());
			System.out.println("테스트 : " + transaction.getSender());
			System.out.println("테스트 : " + transaction.getReceiver());
			System.out.println("테스트 : " + transaction.getType());
			System.out.println("테스트 : ========================");
		});
	}

	@Test
	public void findTransactionList_all_test() throws Exception {
		// given
		Long accountId = 1L;

		// when
		List<Transaction> transactionListPS = transactionRepository.findTransactionList(accountId, "WITHDRAW", 0);
		transactionListPS.forEach((t) -> {
			System.out.println("테스트 : id : " + t.getId());
			System.out.println("테스트 : amount : " + t.getAmount());
			System.out.println("테스트 : sender : " + t.getSender());
			System.out.println("테스트 : reciver : " + t.getReceiver());
			System.out.println("테스트 : withdrawAccount잔액 : " + t.getWithdrawAccountBalance());
			System.out.println("테스트 : depositAccount잔액 : " + t.getDepositAccountBalance());
			System.out.println("테스트 : 잔액 : " + t.getWithdrawAccount().getBalance());
//			System.out.println("테스트 : fullname : " + t.getWithdrawAccount().getUser().getFullname());
			System.out.println("테스트 : ======================================");
		});

		// then
//		assertThat(transactionListPS.get(3).getDepositAccountBalance()).isEqualTo(800L);

	}
}
