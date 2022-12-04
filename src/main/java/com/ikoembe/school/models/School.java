package com.ikoembe.school.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(value = "schools")
@Slf4j
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class School {
    @Id
    private String id;

    @Indexed(unique = true)
    private String name;

    private List<Clazz> classes;
}
