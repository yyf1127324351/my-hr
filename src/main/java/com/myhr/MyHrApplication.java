package com.myhr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class MyHrApplication {

	public static void main(String[] args) {
		SpringApplication.run(MyHrApplication.class, args);
	}

}
