package ru.gormikle.interviewapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class InterviewAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(InterviewAppApplication.class, args);
    }

}
