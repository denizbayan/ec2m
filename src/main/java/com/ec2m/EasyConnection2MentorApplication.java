package com.ec2m;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;


@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class EasyConnection2MentorApplication {

	public static void main(String[] args) {
		SpringApplication.run(EasyConnection2MentorApplication.class, args);
	}

}
