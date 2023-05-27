package com.itcatcetc.smarthome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class SmarthomeApplication {

    public static void main(String[] args) {
        /**
         * run the application
         */
        SpringApplication.run(SmarthomeApplication.class, args);
    }
}
