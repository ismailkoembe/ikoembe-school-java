package com.ikoembe.school.repository;

import com.ikoembe.school.models.TheClass;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TheClassRepository extends MongoRepository <TheClass, String> {
    Optional<TheClass> findById(String s);
    boolean existsById(String s);

    Boolean existsByName(String name);

    Optional<TheClass> findByName(String name);
}
