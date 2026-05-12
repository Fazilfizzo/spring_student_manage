package com.fazil.learn_spring.learnspring_jpa.service;

import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import com.fazil.learn_spring.learnspring_jpa.repository.StudentRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVImportService {

    private final StudentRepository studentRepository;

    public CSVImportService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    String [] HEADERS = { "rollNo", "name", "percentage", "branch" };

    public void importStudents() {
        try (Reader reader = new InputStreamReader(new ClassPathResource("students.csv").getInputStream());
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                     .setHeader(HEADERS)
                     .setSkipHeaderRecord(true)
                     .build())) {
            List<Student> students = new ArrayList<>();

            for (CSVRecord record : csvParser) {
                Student student =new Student();

                student.setRollNo(Long.parseLong(record.get("rollNo")));

                student.setName(record.get("name"));

                student.setPercentage(Double.parseDouble(record.get("percentage")));

                student.setBranch(record.get("branch"));

                students.add(student);
            }
            studentRepository.saveAll(students);

            System.out.println("Students imported successfully........");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
