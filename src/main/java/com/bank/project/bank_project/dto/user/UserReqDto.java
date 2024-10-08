package com.bank.project.bank_project.dto.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.domain.user.UserEnum;

import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


public class UserReqDto {
	
	
	
	
	@Data
	@Builder
	@NoArgsConstructor
	@AllArgsConstructor
	public static class JoinReqDto{
		
		@NotEmpty
		private String username;
		@NotEmpty
		private String password;
		@NotEmpty
		private String email;
		@NotEmpty
		private String fullname;
		
		public User toEntity(BCryptPasswordEncoder bCryptPasswordEncoder) {
			return User.builder()
					.username(username)
					.password(bCryptPasswordEncoder.encode(password))
					.email(email)
					.fullname(fullname)
					.role(UserEnum.CUSTOMER)
					.build();
		}
		
	}
}
