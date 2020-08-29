package com.cricket.wsipl2020;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.annotation.PostConstruct;
import java.util.Date;
import java.util.TimeZone;

@SpringBootApplication
public class WsIpl2020Application {

	@PostConstruct
	public void init(){
		TimeZone.setDefault(TimeZone.getTimeZone("IST"));   // It will set IST timezone
		System.out.println("Spring boot application running in IST timezone :"+new Date());   // It will print IST timezone
	}

	public static void main(String[] args) {
		SpringApplication.run(WsIpl2020Application.class, args);
	}

}
