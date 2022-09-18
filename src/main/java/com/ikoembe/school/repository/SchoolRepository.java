package com.ikoembe.school.repository;

import com.ikoembe.school.models.School;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SchoolRepository extends MongoRepository <School, String> {
}
