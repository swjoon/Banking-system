package com.bank.project.bank_project;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

//import lombok.extern.log4j.Log4j2;

//@Log4j2
@EnableJpaAuditing
@SpringBootApplication
public class BankProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(BankProjectApplication.class, args);
//		ConfigurableApplicationContext context = 
// 		등록된 빈 리스트
//		String[] iocNames = context.getBeanDefinitionNames();
//		for(String name: iocNames) {
//			log.info("등록된 빈 이름 : "+name);
//		}
		
	}

}
