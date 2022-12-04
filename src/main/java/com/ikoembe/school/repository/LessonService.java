package com.ikoembe.school.repository;

import com.ikoembe.school.models.Lesson;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LessonService extends MongoRepository <Lesson, String> {
    Optional<Lesson> findByName (String name);
    Boolean existsByName(String name);
}
