package com.fazil.learn_spring.learnspring_jpa.repository;

import com.fazil.learn_spring.learnspring_jpa.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {
       Optional<Student> findByRollNo(int rollNo);

       @Query("SELECT s from Student s WHERE s.name = :name")
       Optional<Student> findByStudentName(@Param("name") String name);

       List<Student> findByName(String name);
       List<Student> findByPercentage(float percentage);

       List<Student> findByNameOrPercentage(String name, double percentage);

//       Optional<Student> findByNameAndPercentage(String name, double percentage);
       List<Student> findByNameAndPercentage(String name, double percentage);

       List<Student> findByPercentageGreaterThan(double percentage);
}
