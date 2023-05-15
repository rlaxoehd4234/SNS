package com.ffsns.sns;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class FfsnsApplication {

    public static void main(String[] args) {
        SpringApplication.run(FfsnsApplication.class, args);
    }

}
