package com.fazil.learn_spring.learnspring_jpa.interfaces;

import com.fazil.learn_spring.learnspring_jpa.dto.StudentRegistrationDto;
import com.fazil.learn_spring.learnspring_jpa.dto.StudentResponseDTO;
import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface StudentMapper {
    StudentResponseDTO toDto(Student student);

    Student toEntity(StudentRegistrationDto studentRegistrationDto);

}
