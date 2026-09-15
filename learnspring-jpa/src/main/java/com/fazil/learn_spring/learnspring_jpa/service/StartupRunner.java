package com.fazil.learn_spring.learnspring_jpa.service;

import com.fazil.learn_spring.learnspring_jpa.repository.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StartupRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger("app");

    private final StudentRepository studentRepository;

    public StartupRunner(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String ...args) {
        logger.info("Starting app........");
    }

    @Scheduled(initialDelay = 1000, fixedRate = 10000)
    public void run() throws Exception{
        logger.info("Number of books: {}", studentRepository.count());
    }
}
