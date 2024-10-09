package com.bank.project.bank_project.dto.user;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.domain.user.UserEnum;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
		
		@NotEmpty // null & 공백 불가
		@Pattern(regexp = "^[a-zA-Z0-9]{2,20}$", message = "영문/숫자 2-20자 이내로 작성해주세요")
		private String username;
		
		@NotEmpty
		@Size(min = 8,max = 20)
		private String password;
		
		@NotEmpty
		@Pattern(regexp = "^[a-zA-Z0-9]{1,12}@[a-zA-Z]{2,6}\\.[a-zA-Z]{2,3}$", message = "email 형식으로 작성해주세요")
		private String email;
		
		@NotEmpty
		@Pattern(regexp = "^[a-zA-Z가-힣]{1,20}$", message = "한글/영문 1-20자 이내로 작성해주세요")
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
