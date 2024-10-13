package com.bank.project.bank_project.config.dummy;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.repository.AccountRepository;
import com.bank.project.bank_project.repository.UserRepository;

@Configuration
public class DummyDevInit extends DummyObject {

    @Profile("dev") // prod 모드에서는 실행되면 안된다.
    @Bean
    CommandLineRunner init(UserRepository userRepository, AccountRepository accountRepository) {
        return (args) -> {
            // 서버 실행시에 무조건 실행된다.
            User ssar = userRepository.save(newUser("test", "테스트"));
            User admin = userRepository.save(newUser("admin", "관리자"));
 
        };
    }
}