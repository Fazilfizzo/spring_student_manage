package com.fazil.learn_spring.learnspring_jpa.service;

import com.fazil.learn_spring.learnspring_jpa.dto.PageResponse;
import com.fazil.learn_spring.learnspring_jpa.dto.StudentRegistrationDto;
import com.fazil.learn_spring.learnspring_jpa.dto.StudentResponseDTO;
import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import com.fazil.learn_spring.learnspring_jpa.exception.StudentException;

import com.fazil.learn_spring.learnspring_jpa.mapper.StudentMapper;
import com.fazil.learn_spring.learnspring_jpa.repository.StudentRepository;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
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
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class StudentService {
    private final StudentRepository studentRepository;
    private final String UPLOAD_DIR = "uploads/";

    private final MeterRegistry meterRegistry;

    public StudentService(StudentRepository studentRepository, MeterRegistry meterRegistry) {
        this.studentRepository = studentRepository;
        this.meterRegistry = meterRegistry;

        Gauge.builder("students.all_students",
                studentRepository,
                StudentRepository::count)
                .description("Current number of all students in the system")
                .register(meterRegistry);
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

    @Cacheable(
            value = "studentByRollNo",
            key = "#rollNo"
    )
   public StudentResponseDTO getStudent(int rollNo) {
        Student student;
       student = studentRepository.findByRollNo(rollNo).orElseThrow(() -> new StudentException("Student with this roll no doesn't exist"));
        return StudentMapper.toStudentResponseDto(student);
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

    @Caching(
            put = {
                    @CachePut(
                            value = "studentByRollNo",
                            key = "#rollNo"
                    )
            },
            evict = {
                    @CacheEvict(
                            value = "studentsByGrade",
                            key = "#category",
                            allEntries = true
                    )
            }
    )
   public StudentResponseDTO updateStudent(int rollNo, StudentRegistrationDto studentRegistrationDto) {
      Student student = studentRepository.findById(rollNo).orElseThrow(() -> new StudentException("Student does not exist"));

        student.setName(studentRegistrationDto.getName());
        student.setBranch(studentRegistrationDto.getBranch());
        student.setPercentage(studentRegistrationDto.getPercentage());
//      student.setName(studentRegistrationDto.getName());
//      student.setPercentage(studentRegistrationDto.getPercentage());
//      student.setBranch(studentRegistrationDto.getBranch());

        studentRepository.save(student);

        return StudentMapper.toStudentResponseDto(student);
    }

   @Cacheable(
           value = "studentPages",
           key = "#page + ':' + #size"
   )
   public PageResponse<Student> getAllProducts(int page, int size, String sortBy, String sortBy1) {
//       Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Sort sort = Sort.by(
                Sort.Order.asc(sortBy),
                Sort.Order.desc(sortBy1)
        );

       Pageable pageable = PageRequest.of(page, size, sort);

       Page<Student> studentPage = studentRepository.findAll(pageable);

       return new PageResponse<>(
               studentPage.getContent(),
               studentPage.getNumber(),
               studentPage.getSize(),
               studentPage.getTotalElements(),
               studentPage.getTotalPages(),
               studentPage.isLast()
       );
   }

   @Cacheable(
           value = "studentsByGrade",
           key = "#category"
   )
   public List<StudentResponseDTO> getStudentByStudentGrade(String category) {
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
                   .map(StudentMapper::toStudentResponseDto)
                   .collect(Collectors.toList());
           case "B" -> studentList
                   .stream()
                   .filter(isGradeB)
                   .map(StudentMapper::toStudentResponseDto)
                   .collect(Collectors.toList());
           case "C" -> studentList
                   .stream()
                   .filter(isGradeC)
                   .map(StudentMapper::toStudentResponseDto)
                   .collect(Collectors.toList());
           case "D" -> studentList
                   .stream()
                   .filter(isGradeD)
                   .map(StudentMapper::toStudentResponseDto)
                   .collect(Collectors.toList());
           case "F" -> studentList
                   .stream()
                   .filter(isGradeF)
                   .map(StudentMapper::toStudentResponseDto)
                   .collect(Collectors.toList());
           default -> studentList.stream().map(StudentMapper::toStudentResponseDto).toList();
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
