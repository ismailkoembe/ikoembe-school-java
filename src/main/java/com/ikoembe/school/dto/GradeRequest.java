package com.ikoembe.school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor @Getter @Setter @NoArgsConstructor
public class GradeRequest {
    private String className;
    private String studentId;
    private Map<String, String> lessonMap;
}
