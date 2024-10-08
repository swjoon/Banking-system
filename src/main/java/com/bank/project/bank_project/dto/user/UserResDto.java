package com.bank.project.bank_project.dto.user;

import com.bank.project.bank_project.domain.user.User;

import lombok.Data;

@Data
public class UserResDto {
	
	
	
	
	
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
