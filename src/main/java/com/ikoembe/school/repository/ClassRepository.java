package com.ikoembe.school.repository;

import com.ikoembe.school.models.Clazz;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ClassRepository extends MongoRepository <Clazz, String> {
    Optional<Clazz> findById(String s);

    boolean existsById(String s);

    Boolean existsByName(String name);

    Optional<Clazz> findByName(String name);

    Boolean existsByStudents(String accountId);

    Boolean existsByTeachers(String accountId);

}
