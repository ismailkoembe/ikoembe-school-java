package com.ikoembe.school.services;


import com.ikoembe.school.models.Clazz;
import com.ikoembe.school.models.Lesson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service @Slf4j
public class LessonServiceImplementation {
    @Autowired
    private MongoTemplate mongoTemplate;

    public Lesson findByNameAndCode(String name, String code) {
        Query query = new Query();
        query.addCriteria(where(Lesson.FIELD_NAME).is(name));
        query.addCriteria(where(Lesson.FIELD_CODE).is(code));
        log.debug(query.toString());
         return mongoTemplate.findOne(query, Lesson.class);

    }


}
