package com.bank.project.bank_project.config.dummy;

import java.time.LocalDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.project.bank_project.domain.account.Account;
import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.domain.user.UserEnum;

public class DummyObject {
	
	protected static User newUser(String username, String fullname) {
		
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        
        String encPassword = passwordEncoder.encode("12345678");
        
        return User.builder()
                .username(username)
                .password(encPassword)
                .email(username + "@nate.com")
                .fullname(fullname)
                .role(UserEnum.CUSTOMER)
                .build();
    }
	
	protected User newMockUser(Long id, String username, String fullname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("12345678");
        return User.builder()
                .id(id)
                .username(username)
                .password(encPassword)
                .email(username + "@nate.com")
                .fullname(fullname)
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
	
	protected User newMockAc(Long id, String username, String fullname) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encPassword = passwordEncoder.encode("12345678");
        return User.builder()
                .id(id)
                .username(username)
                .password(encPassword)
                .email(username + "@nate.com")
                .fullname(fullname)
                .role(UserEnum.CUSTOMER)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
    }
	
	 protected Account newAccount(Long number, User user) {
	        return Account.builder()
	                .number(number)
	                .password(1234L)
	                .balance(1000L)
	                .user(user)
	                .build();
	    }
	 
	 protected Account newMockAccount(Long id, Long number, Long balance, User user) {
	        return Account.builder()
	                .id(id)
	                .number(number)
	                .password(1234L)
	                .balance(balance)
	                .user(user)
	                .createdAt(LocalDateTime.now())
	                .updatedAt(LocalDateTime.now())
	                .build();
	    }
}
