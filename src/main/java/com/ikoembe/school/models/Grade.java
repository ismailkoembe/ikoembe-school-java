package com.ikoembe.school.models;

import com.mongodb.lang.Nullable;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.Map;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Slf4j @ToString
@Document(value = "grades")
public class Grade {
    public static final String FIELD_ID = "id";
    public static final String FIELD_STUDENT_ID = "student";
    public static final String FIELD_TEACHER_ID = "teacher";
    public static final String FIELD_CLASS_NAME = "className";
    public static final String FIELD_LESSON_NAME = "lessonMap.name";
    public static final String FIELD_LESSON_CDDE = "lessonMap.code";
    public static final String FIELD_GRADE = "lessonMap.grade";
    public static final String FIELD_CORRECT = "correctAnswer";
    public static final String FIELD_WRONG = "wrongAnswer";
    public static final String FIELD_TOTAL = "totalQuestion";
    public static final String FIELD_COMMENT = "comment";
    public static final String FIELD_EXAM_DATE = "examDate";
    public static final String FIELD_CREATED_DATE = "createdDate";
    public static final String FIELD_UPDATED_DATE = "updatedDate";


    @Id
    private String id;

    @Indexed
    private String student;

    @Indexed
    private String teacher;

    @Indexed
    private String className;

    @Indexed
    private Map<String, String> lessonMap;

    private Double grade;

    @Nullable
    private Double efficiency;

    @Nullable
    private Integer correctAnswer;
    @Nullable
    private Integer wrongAnswer;
    @Nullable
    private Integer totalQuestion;
    @Nullable
    private String comment;
    private LocalDateTime examDate;
    private LocalDateTime createdTime;
    private LocalDateTime updateTime;



}
