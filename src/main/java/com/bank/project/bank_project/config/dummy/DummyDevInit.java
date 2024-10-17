package com.bank.project.bank_project.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.transaction.Transaction;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.UserRepository;
import com.bank.project.bank_project.repository.transaction.TransactionRepository;

@Configuration
public class DummyDevInit extends DummyObject {

    @Profile("dev") // prod 모드에서는 실행되면 안된다.
    @Bean
    CommandLineRunner init(UserRepository userRepository, AccountRepository accountRepository, TransactionRepository transactionRepository) {
        return (args) -> {
            // 서버 실행시에 무조건 실행된다.
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
        };
    }
}