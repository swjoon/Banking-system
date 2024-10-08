package com.bank.project.bank_project.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.dto.user.UserReqDto.JoinReqDto;
import com.bank.project.bank_project.dto.user.UserResDto.JoinResDto;
import com.bank.project.bank_project.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
	
	@InjectMocks
	private UserService userService;
	
	@Mock
	private UserRepository userRepository;
	
	@Spy
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Test
	public void 회원가입_test() throws Exception{
		// given
		
		JoinReqDto joinReqDto = JoinReqDto.builder().username("testuser").password("1234").email("email@test.com").fullname("테스트").build();
		
		// stub 1
		when(userRepository.findByUsername(any())).thenReturn(Optional.empty());
		
		// stub 2
		User testuser = joinReqDto.toEntity(bCryptPasswordEncoder);
				
		when(userRepository.save(any())).thenReturn(testuser);
		
		// when
		JoinResDto joinResDto = userService.회원가입(joinReqDto);
		
		System.out.println(joinResDto.toString());
		
		// then
		assertThat(joinResDto.getUsername()).isEqualTo("testuser");
		assertThat(bCryptPasswordEncoder.matches(joinReqDto.getPassword(), testuser.getPassword())).isTrue();
		
	}
}
