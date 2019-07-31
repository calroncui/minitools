package com.cuiyf.minitools;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@SpringBootApplication
@EnableAutoConfiguration
@ComponentScan(value = "com.cuiyf.minitools")
public class MinitoolsApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinitoolsApplication.class, args);
	}

}
