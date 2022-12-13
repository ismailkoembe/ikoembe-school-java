package com.ikoembe.school.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter @Setter @AllArgsConstructor
public class GradeResponse {
    private String name;
    private String teacher;
    private String className;
    private String lessonName;
    private String lessonCode;
    private Double aggregation;
    private Boolean result;
    private Integer examCount;
    private Double percentage;

}
