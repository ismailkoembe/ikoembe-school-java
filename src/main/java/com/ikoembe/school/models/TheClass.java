package com.ikoembe.school.models;

import com.mongodb.lang.Nullable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;


@Slf4j
@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Document(value = "classes")
public class TheClass {
    @Id
    private String id;

//    @Indexed
    private String name;

    private List<String> students;

    private List<String> teachers;

    @Nullable
    private List<Lesson> lessons;







}
