package com.ikoembe.school.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mongodb.lang.Nullable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.Map;


@Slf4j
@Data
@Document(value = "classes")
public class Clazz {
    public static final String FIELD_TEACHERS = "teachers.accountId";
    public static final String FIELD_NAMES = "name";


    @Id
    private String id;

//    @Indexed
    private String name;

    private List<String> students;

    @JsonProperty
    private List<String> teachers;

    @Nullable
    private List<Lesson> lessons;







}
