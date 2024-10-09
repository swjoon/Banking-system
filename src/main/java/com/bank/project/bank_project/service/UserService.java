package com.bank.project.bank_project.service;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.dto.user.UserReqDto.JoinReqDto;
import com.bank.project.bank_project.dto.user.UserResDto.JoinResDto;
import com.bank.project.bank_project.handler.ex.CustomApiException;
import com.bank.project.bank_project.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
	
	private final Logger log = LoggerFactory.getLogger(getClass());
	private final BCryptPasswordEncoder bCryptPasswordEncoder;
	private final UserRepository userRepository;

	
	@Transactional
	public JoinResDto 회원가입(JoinReqDto joinReqDto) {
		Optional<User> opUser = userRepository.findByUsername(joinReqDto.getUsername());
		
		if(opUser.isPresent()) {
			throw new CustomApiException("동일한 아이디가 존재합니다");
		}
		
		log.info("회원가입성공");
		
		User newUser = userRepository.save(joinReqDto.toEntity(bCryptPasswordEncoder));
		
		return new JoinResDto(newUser);
	}
	
}







