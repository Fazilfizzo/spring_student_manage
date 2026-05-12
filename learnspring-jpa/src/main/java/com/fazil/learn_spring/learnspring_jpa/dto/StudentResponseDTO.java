package com.fazil.learn_spring.learnspring_jpa.dto;

public class StudentResponseDTO {
    private String name;
    private double percentage;
    private String branch;

    public StudentResponseDTO() {

    }

    public StudentResponseDTO(String name, double percentage, String branch) {
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
}
