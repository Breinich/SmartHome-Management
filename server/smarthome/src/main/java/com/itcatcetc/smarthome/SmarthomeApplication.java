package com.itcatcetc.smarthome;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;


@SpringBootApplication
@EnableScheduling
public class SmarthomeApplication {

	public static void main(String[] args) {

		SpringApplication.run(SmarthomeApplication.class, args);
	}
}
