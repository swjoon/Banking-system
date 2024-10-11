package com.bank.project.bank_project.dto.user;

import com.bank.project.bank_project.domain.user.User;
import com.bank.project.bank_project.util.CustomDateUtil;

import lombok.Data;

public class UserResDto {
	
	
	@Data
	public static class LoginResDto{
		
		private Long id;
		private String username;
		private String createdAt;
		
		public LoginResDto(User user) {
			this.id = user.getId();
			this.username = user.getUsername();
			this.createdAt = CustomDateUtil.toStringFormat(user.getCreatedAt());
		}
		
	}
	
	
	
	@Data
	public static class JoinResDto{
		private Long id;
		private String username;
		private String fullname;
		
		public JoinResDto(User user) {
			this.id = user.getId();
			this.username = user.getUsername();
			this.fullname = user.getFullname();
		}
		
	}
}
