package com.fazil.learn_spring.learnspring_jpa.mapper;

import com.fazil.learn_spring.learnspring_jpa.dto.StudentResponseDTO;
import com.fazil.learn_spring.learnspring_jpa.entity.Student;

public class StudentMapper {

    public static StudentResponseDTO toStudentResponseDto(Student student) {
         StudentResponseDTO studentResponseDTO = new StudentResponseDTO();
         studentResponseDTO.setName(student.getName());
         studentResponseDTO.setBranch(student.getBranch());
         studentResponseDTO.setPercentage(student.getPercentage());

         return studentResponseDTO;
    }
}
