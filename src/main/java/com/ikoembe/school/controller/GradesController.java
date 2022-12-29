package com.ikoembe.school.controller;

import com.ikoembe.school.dto.GradeRequest;
import com.ikoembe.school.dto.GradeResponse;
import com.ikoembe.school.models.Grade;
import com.ikoembe.school.repository.ClassRepository;
import com.ikoembe.school.repository.GradeRepository;
import com.ikoembe.school.services.GradeServiceImplementation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/grades")
public class GradesController {
    @Autowired
    GradeRepository gradeRepository;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    GradeServiceImplementation gradeServiceImplementation;

    @Value("${threshold}")
    private Double threshold;

    @PostMapping(value = "/addGrade")
    @ApiOperation("Teacher can add grade for single student")
    public ResponseEntity<?> addGrade(@Valid @RequestBody Grade grade) {
        if (classRepository.existsByName(grade.getClassName()) &&
                classRepository.existsByTeachers(grade.getTeacher()) &&
                classRepository.existsByStudents(grade.getStudent())) {
            gradeRepository.save(grade);

        } else {
            return ResponseEntity
                    .badRequest()
                    .body(("Error: The given class, student or teacher information is not valid"));
        }
        return ResponseEntity.ok().body(grade);
    }


    @PostMapping(value = "/allGrades")
    @ApiOperation("Get all grades for a student")
    public ResponseEntity<?> getAllGrades(@Valid @RequestBody GradeRequest request) {
        List<Grade> grades = new ArrayList<>();
        if (classRepository.existsByName(request.getClassName()) &&
                classRepository.existsByStudents(request.getStudentId())) {

            grades = gradeRepository.findByStudent(request.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("There is no record for this student"));

        } else {
            return ResponseEntity
                    .badRequest()
                    .body(("Error: The given class or student information is not valid"));
        }
        return ResponseEntity.ok().body(grades);
    }

    @PostMapping(value = "/allGradesByLesson")
    @ApiOperation("Get all grades by lesson name and code for a student")
    public ResponseEntity<?> getAllGradesByLesson(@Valid @RequestBody GradeRequest request) {
        List<Grade> grades = null;
        if (classRepository.existsByName(request.getClassName()) &&
                classRepository.existsByStudents(request.getStudentId())) {

            grades = gradeRepository.findByStudent(request.getStudentId())
                    .orElseThrow(() -> new IllegalArgumentException("There is no record for this student"));

            return ResponseEntity.ok().body(grades.stream()
                    .filter(x -> x.getLessonMap().get("name").equals(request.getLessonMap().get("name")))
                    .filter(x -> x.getLessonMap().get("code").equals(request.getLessonMap().get("code")))
                    .collect(Collectors.toList()));
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(("Error: The given class or student information is not valid"));
        }
    }

    @PostMapping(value = "/calculate")
    @ApiOperation("Calculates the given lesson and returns the aggregation")
    public ResponseEntity<?> calculate(
            @RequestHeader String student,
            @RequestHeader String lesson,
            @RequestHeader String code) {
        List<Grade> grades = gradeServiceImplementation.findGradesOfLessonForStudent(student, lesson, code);
        double sum = calculateTheGrade(grades);

        return ResponseEntity.ok().body(
                new GradeResponse(
                        grades.get(0).getStudent(),
                        grades.get(0).getTeacher(),
                        grades.get(0).getClassName(),
                        grades.get(0).getLessonMap().get("name"),
                        grades.get(0).getLessonMap().get("code"),
                        sum,
                        sum > threshold,
                        grades.size(),
                        grades.stream().mapToDouble(x -> x.getEfficiency()).sum()
                )
        );

    }

    private double calculateTheGrade(List<Grade> grades) {
        double sum = grades.stream()
                .map(x -> (x.getGrade() * x.getEfficiency()) / 100)
                .mapToDouble(x -> x)
                .sum();
        return sum;
    }

    @PostMapping(value = "/calculateAll")
    @ApiOperation("Calculates the given lesson and returns the aggregation")
    public ResponseEntity<?> calculateAllGradesForSingleUser(
            @RequestHeader String student) {
        List<Grade> grades = gradeServiceImplementation.findGradesOfAllLessonForStudent(student);
        Map<String, Double> lessonGradeMap = grades.stream()
                .collect(Collectors.groupingBy(
                        // Function that maps a Grade object to a String key
                        grade -> grade.getLessonMap().get("name"),
                        // Collector that calculates the sum of the calculated values for each group
                        Collectors.summingDouble(grade -> (grade.getGrade() * grade.getEfficiency()) / 100)
                ));
        return ResponseEntity.ok().body(
                grades.stream()
                        .collect(Collectors.groupingBy(Grade::getLessonMap))
                        .values()
                        .stream()
                        .map(group -> group.stream()
                                .findFirst()
                                .map(z -> new GradeResponse(
                                        z.getStudent(),
                                        z.getTeacher(),
                                        z.getClassName(),
                                        z.getLessonMap().get("name"),
                                        z.getLessonMap().get("code"),
                                        group.stream()
                                                .map(x -> (x.getGrade() * x.getEfficiency()) / 100)
                                                .mapToDouble(x -> x)
                                                .sum(),
                                        group.stream()
                                                .map(x -> (x.getGrade() * x.getEfficiency()) / 100)
                                                .mapToDouble(x -> x)
                                                .sum() > threshold,
                                        group.size(),
                                        group.stream()
                                                .mapToDouble(x -> x.getEfficiency())
                                                .sum()
                                )))
                        .collect(Collectors.toList()));

    }


}
