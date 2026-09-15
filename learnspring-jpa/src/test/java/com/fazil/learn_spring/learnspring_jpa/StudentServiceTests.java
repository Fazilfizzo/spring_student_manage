package com.fazil.learn_spring.learnspring_jpa;

import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import com.fazil.learn_spring.learnspring_jpa.repository.StudentRepository;
import com.fazil.learn_spring.learnspring_jpa.service.StudentService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class StudentServiceTests {

    @Mock
    private StudentRepository studentRepository;

    @InjectMocks
    private StudentService studentService;


    @Test
    void ShouldReturnStudentWhenStudentExists() {
        Student student = new Student();
        student.setRollNo(1L);

    }
}
