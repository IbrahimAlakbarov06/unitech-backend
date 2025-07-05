package org.unitech.msuser;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MsUserApplication {

    public static void main(String[] args) {
        System.out.println("MsUserApplication started");
        SpringApplication.run(MsUserApplication.class, args);
    }

}
