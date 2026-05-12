package com.fazil.learn_spring.learnspring_jpa.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;

@Entity
@Table(name = "student")
@SQLDelete(sql = "UPDATE student SET deleted = true WHERE roll_no = ?")
public class Student extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long rollNo;

    @Column(name = "student_name")
    private String name;

    @Column(name = "student_percentage")
    private double percentage;

    @Column(name = "student_branch")
    private String branch;

    public Student() {
    }

    public Student(String name, double percentage, String branch) {
        this.name = name;
        this.percentage = percentage;
        this.branch = branch;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getRollNo() {
        return rollNo;
    }

    public void setRollNo(Long rollNo) {
        this.rollNo = rollNo;
    }

    @Override
    public String toString() {
        return "Student{" +
                "rollNo=" + rollNo +
                ", name='" + name + '\'' +
                ", percentage=" + percentage + '\'' +
                ", branch='" + branch + '\'' +
                '}';
    }
}
