package com.ikoembe.school.models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Slf4j @ToString
@Document(value = "lessons")
public class Lesson {
    public static final String FIELD_NAME = "name";
    public static final String FIELD_CODE = "code";


    @Id
    private String id;

    @Indexed
    private Majors name;

    private boolean isActive;

    @Indexed
    private String code;

    private String description;

    private String [] availableFor ;

    @Indexed
    private boolean isMandatory;
}
