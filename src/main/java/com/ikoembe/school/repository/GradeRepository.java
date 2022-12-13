package com.ikoembe.school.repository;

import com.ikoembe.school.models.Grade;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GradeRepository extends MongoRepository <Grade, String> {

    Optional<List<Grade>> findByStudent(String studentId);

}
