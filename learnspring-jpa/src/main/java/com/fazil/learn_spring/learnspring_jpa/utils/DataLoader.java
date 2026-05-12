package com.fazil.learn_spring.learnspring_jpa.utils;

import com.fazil.learn_spring.learnspring_jpa.repository.StudentRepository;
import com.fazil.learn_spring.learnspring_jpa.service.CSVImportService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    private final CSVImportService csvImportService;
    private final StudentRepository studentRepository;

    public DataLoader(CSVImportService csvImportService, StudentRepository studentRepository) {
        this.csvImportService = csvImportService;
        this.studentRepository = studentRepository;
    }

    @Override
    public void run(String... args) {
        if (studentRepository.count() == 0) {
            csvImportService.importStudents();
        }
    }
}
