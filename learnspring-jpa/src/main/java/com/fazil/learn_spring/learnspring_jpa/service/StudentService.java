package com.fazil.learn_spring.learnspring_jpa.service;

import com.fazil.learn_spring.learnspring_jpa.TimeMonitor;
import com.fazil.learn_spring.learnspring_jpa.dto.StudentRegistrationDto;
import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import com.fazil.learn_spring.learnspring_jpa.exception.StudentException;
import com.fazil.learn_spring.learnspring_jpa.interfaces.StudentMapper;
import com.fazil.learn_spring.learnspring_jpa.repository.StudentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final String UPLOAD_DIR = "uploads/";
    private final StudentMapper studentMapper;


    public StudentService(StudentRepository studentRepository, StudentMapper studentMapper) {
        this.studentRepository = studentRepository;
        this.studentMapper = studentMapper;
    }

   public List<Student> findStudentBasedOnPercentage(double percentage) {
        Predicate<Student> isPercentageGreater = user -> user.getPercentage() > percentage;
        List<Student> studentList = studentRepository.findAll();
        return studentList.stream()
                .filter(isPercentageGreater)
                .toList();
   }


   public Student getStudentByRollNo(int rollNo) {
        Optional<Student> studentOptional = studentRepository.findByRollNo(rollNo);
        Student student = studentOptional.orElse(new Student("Default", 78.99, "CANADA"));
        return student;
   }

   public Student createStudent(StudentRegistrationDto studentRegistrationDto) {
        if(!studentRepository.findByName(studentRegistrationDto.getName()).isEmpty()) {
            studentRegistrationDto.setName("PAUL");
        }

        Student savedStudent = new Student();
        savedStudent.setName(studentRegistrationDto.getName());
        savedStudent.setBranch(studentRegistrationDto.getBranch());
        savedStudent.setPercentage(studentRegistrationDto.getPercentage());
//       Student savedStudent = studentRegistrationDto.getStudentFromDto();
        return studentRepository.save(savedStudent);
   }

   public Student updateStudent(int id, StudentRegistrationDto studentRegistrationDto) {
      Student student = studentRepository.findById(id).orElseThrow(() -> new StudentException("Student does not exist"));

        student = studentMapper.toEntity(studentRegistrationDto);
//      student.setName(studentRegistrationDto.getName());
//      student.setPercentage(studentRegistrationDto.getPercentage());
//      student.setBranch(studentRegistrationDto.getBranch());

      return studentRepository.save(student);
   }


   public Page<Student> getAllProducts(int page, int size, String sortBy, String sortBy1) {
//       Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Sort sort = Sort.by(
                Sort.Order.asc(sortBy),
                Sort.Order.desc(sortBy1)
        );

       Pageable pageable = PageRequest.of(page, size, sort);

       return studentRepository.findAll(pageable);
   }

   public List<Student> getStudentByStudentGrade(String category) {
        List<Student> studentList = studentRepository.findAll();
        Predicate<Student> isGradeA = student -> ((student.getPercentage() >= 80));
        Predicate<Student> isGradeB = student -> ((student.getPercentage() >= 60) && (student.getPercentage() < 80));
        Predicate<Student> isGradeC = student -> ((student.getPercentage() >= 40) && (student.getPercentage() < 60));
        Predicate<Student> isGradeD = student -> ((student.getPercentage() >= 20) && (student.getPercentage() < 40));
        Predicate<Student> isGradeF = student -> ((student.getPercentage() > 0) && (student.getPercentage() < 20));
       return switch (category) {
           case "A" -> studentList
                   .stream()
                   .filter(isGradeA)
                   .collect(Collectors.toList());
           case "B" -> studentList
                   .stream()
                   .filter(isGradeB)
                   .collect(Collectors.toList());
           case "C" -> studentList
                   .stream()
                   .filter(isGradeC)
                   .collect(Collectors.toList());
           case "D" -> studentList
                   .stream()
                   .filter(isGradeD)
                   .collect(Collectors.toList());
           case "F" -> studentList
                   .stream()
                   .filter(isGradeF)
                   .collect(Collectors.toList());
           default -> studentList;
       };
   }

   public void upload(MultipartFile file) {
        try {
            File directory = new File(UPLOAD_DIR);
            if(!directory.exists()) {
                directory.mkdirs();
            }

            Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
            Files.write(path, file.getBytes());
        } catch (IOException e) {
            throw new RuntimeException("UploadFailed!!!!!!!");
        }
   }

}
