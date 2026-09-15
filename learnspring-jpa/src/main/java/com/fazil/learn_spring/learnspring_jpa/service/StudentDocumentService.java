package com.fazil.learn_spring.learnspring_jpa.service;

import com.fazil.learn_spring.learnspring_jpa.controller.StudentController;
import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import com.fazil.learn_spring.learnspring_jpa.entity.StudentDocument;
import com.fazil.learn_spring.learnspring_jpa.repository.StudentDocumentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class StudentDocumentService {

    private static final Logger logger = LoggerFactory.getLogger(StudentDocumentService.class);

    private final StudentDocumentRepository studentDocumentRepository;

    public StudentDocumentService(StudentDocumentRepository studentDocumentRepository) {
        this.studentDocumentRepository = studentDocumentRepository;
    }

    public void save(byte[] csvBytes) {
        logger.info("Starting to save to MongoDB");
        StudentDocument studentDocument = new StudentDocument();
        studentDocument.setStudentId("csrt765");
        studentDocument.setDocumentType(".csv");
        studentDocument.setFilename("students.csv");
        studentDocument.setFileSize((long) csvBytes.length);
        studentDocument.setMimeType("text/csv");
        studentDocument.setUploadedAt(LocalDateTime.now());
        studentDocument.setStatus("COMPLETED");

        studentDocumentRepository.save(studentDocument);

        logger.info("Saved to mongodb.....");
    }
}
