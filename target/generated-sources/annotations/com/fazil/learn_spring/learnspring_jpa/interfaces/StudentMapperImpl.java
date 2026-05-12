package com.fazil.learn_spring.learnspring_jpa.interfaces;

import com.fazil.learn_spring.learnspring_jpa.dto.StudentRegistrationDto;
import com.fazil.learn_spring.learnspring_jpa.dto.StudentResponseDTO;
import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-05-11T22:59:56+0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 21.0.3 (Oracle Corporation)"
)
@Component
public class StudentMapperImpl implements StudentMapper {

    @Override
    public StudentResponseDTO toDto(Student student) {
        if ( student == null ) {
            return null;
        }

        StudentResponseDTO studentResponseDTO = new StudentResponseDTO();

        studentResponseDTO.setName( student.getName() );
        studentResponseDTO.setPercentage( student.getPercentage() );
        studentResponseDTO.setBranch( student.getBranch() );

        return studentResponseDTO;
    }

    @Override
    public Student toEntity(StudentRegistrationDto studentRegistrationDto) {
        if ( studentRegistrationDto == null ) {
            return null;
        }

        Student student = new Student();

        student.setBranch( studentRegistrationDto.getBranch() );
        student.setPercentage( studentRegistrationDto.getPercentage() );
        student.setName( studentRegistrationDto.getName() );

        return student;
    }
}
