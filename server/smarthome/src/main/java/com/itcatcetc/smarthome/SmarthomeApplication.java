package com.itcatcetc.smarthome;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableScheduling
@EnableTransactionManagement
public class SmarthomeApplication {

    /**
     * Main method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(SmarthomeApplication.class, args);
    }
}
