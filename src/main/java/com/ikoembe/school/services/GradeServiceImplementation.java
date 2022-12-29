package com.ikoembe.school.services;

import com.ikoembe.school.models.Grade;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service @Slf4j
public class GradeServiceImplementation {
    @Autowired
    MongoTemplate mongoTemplate;

    public List<Grade> findGradesOfLessonForStudent(String student, String lessonName, String code) {
        Query query = new Query();
        query.addCriteria(where(Grade.FIELD_STUDENT_ID).is(student));
        query.addCriteria(where(Grade.FIELD_LESSON_NAME).is(lessonName));
        query.addCriteria(where(Grade.FIELD_LESSON_CDDE).is(code));
        log.debug(query.toString());
        return mongoTemplate.find(query, Grade.class);

    }

    public List<Grade> findGradesOfAllLessonForStudent(String student) {
        Query query = new Query();
        query.addCriteria(where(Grade.FIELD_STUDENT_ID).is(student));
        log.debug(query.toString());
        return mongoTemplate.find(query, Grade.class);

    }
}
