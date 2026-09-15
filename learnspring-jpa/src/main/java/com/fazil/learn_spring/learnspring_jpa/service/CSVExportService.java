package com.fazil.learn_spring.learnspring_jpa.service;

import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import com.fazil.learn_spring.learnspring_jpa.repository.StudentRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@Service
public class CSVExportService {

    private static final Logger logger = LoggerFactory.getLogger(CSVExportService.class);

    private final StudentRepository studentRepository;
    private final StudentDocumentService studentDocumentService;

    private final MeterRegistry meterRegistry;
    private final Counter csvExported;
    private final Timer csvDuration;

    public CSVExportService(StudentRepository studentRepository, StudentDocumentService studentDocumentService, MeterRegistry meterRegistry) {
        this.studentRepository = studentRepository;
        this.studentDocumentService = studentDocumentService;
        this.meterRegistry = meterRegistry;

        csvExported = Counter.builder("csv.exported")
                .description("Number of csv exported")
                .register(meterRegistry);

        csvDuration = Timer.builder("csv.export.duration")
                .description("csv export duration")
                .register(meterRegistry);
    }

    public ByteArrayInputStream processCsv() throws Exception {
        csvExported.increment();

       return csvDuration.recordCallable(() -> {
           return exportStudentsToCsv();
        });
    }

    @Retry(name = "csvExportService")
    @CircuitBreaker(name = "csvExportService")
    public ByteArrayInputStream exportStudentsToCsv() throws IOException {

        logger.info("Starting to transform from entity model to csv");

        List<Student> students = studentRepository.findAll();

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        String [] HEADERS = { "rollNo", "name", "percentage", "branch" };

        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader(HEADERS)
                .build();

        try (CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), csvFormat)) {

            for (Student student : students) {
                csvPrinter.printRecord(
                        student.getRollNo(),
                        student.getName(),
                        student.getPercentage(),
                        student.getBranch()
                );
            }
            csvPrinter.flush();
        }

        logger.info("Done exporting to csv");

        studentDocumentService.save(out.toByteArray());

        return new ByteArrayInputStream(out.toByteArray());
    }
}
