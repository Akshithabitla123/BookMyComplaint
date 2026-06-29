package com.example.complaintEscalation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ComplaintEscalationApplication {

	public static void main(String[] args) {
		SpringApplication.run(ComplaintEscalationApplication.class, args);
	}

}
