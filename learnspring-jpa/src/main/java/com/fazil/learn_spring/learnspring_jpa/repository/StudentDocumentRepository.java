package com.fazil.learn_spring.learnspring_jpa.repository;

import com.fazil.learn_spring.learnspring_jpa.entity.StudentDocument;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface StudentDocumentRepository extends MongoRepository<StudentDocument, String> {
    List<StudentDocument> findByStudentId(Long studentId);
}
