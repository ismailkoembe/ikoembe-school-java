package com.ikoembe.school.services;

import com.ikoembe.school.models.Clazz;
import lombok.extern.log4j.Log4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class ClassService {
    @Autowired
    private MongoTemplate mongoTemplate;

    //TODO it is not in use
    public Boolean existsByTeachers(String className, String accountId) {
        Query query = new Query();
        query.addCriteria(where(Clazz.FIELD_NAMES).is(className));
        query.addCriteria(where(Clazz.FIELD_TEACHERS).is(accountId));

       mongoTemplate.find(query, List.class);

        return false;
    }
}
