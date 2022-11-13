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
    @Id
    private String id;
    @Indexed
    private String name;

    private boolean isActive;

    @Indexed
    private String code;
}
