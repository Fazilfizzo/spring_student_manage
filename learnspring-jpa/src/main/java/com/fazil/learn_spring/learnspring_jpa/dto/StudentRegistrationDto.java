package com.fazil.learn_spring.learnspring_jpa.dto;

import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import jakarta.validation.constraints.*;

public class StudentRegistrationDto {

    @NotBlank(message = "Name must not be blank")
    private String name;

    @NotNull
    @Min(value = 0, message = "Percentage must be greater than zero")
    @Max(value = 100, message = "Percentage must be less than or equal to 100")
    private double percentage;

    @NotBlank(message = "Branch must not be blank")
    private String branch;

    public StudentRegistrationDto(String name, double percentage, String branch) {
        this.name = name;
        this.percentage = percentage;
        this.branch = branch;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

//    public Student getStudentFromDto() {
//        Student student = new Student();
//        student.setName(name);
//        student.setPercentage(percentage);
//        student.setBranch(branch);
//        return student;
//    }
}
