package com.ikoembe.school.models;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "schools")
@Data
public class School {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private List<Clazz> classes;
}
