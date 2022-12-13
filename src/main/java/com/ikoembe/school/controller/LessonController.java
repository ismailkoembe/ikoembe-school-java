package com.ikoembe.school.controller;

import com.ikoembe.school.models.Lesson;
import com.ikoembe.school.repository.LessonService;
import com.ikoembe.school.services.LessonServiceImplementation;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    LessonService lessonService;

    @Autowired
    LessonServiceImplementation lessonServiceImplementation;

    @PostMapping(value = "/add")
    @ApiOperation("Creates a lesson")
    public ResponseEntity<?> addLesson (@Valid @RequestBody Lesson lesson){
        if (lessonService.existsByName(lesson.getName().getName())
                &&lessonService.existsByCode(lesson.getCode())){
            return ResponseEntity
                    .badRequest()
                    .body(("Error: This lesson is already added"));
        }
        lessonService.save(lesson);
        log.info("A new lesson {}, {} added", lesson.getName(), lesson.getCode());
        return ResponseEntity.ok().body(lesson);

    }

    @PatchMapping(value = "/update")
    @ApiOperation("Update a lesson")
    public ResponseEntity<?> update ( @RequestHeader String code, @RequestHeader String name,
                                      @RequestBody Map<String, Object> patches){
        try {
            Lesson lesson = lessonServiceImplementation.findByNameAndCode(name, code);
            patches.forEach((k, v) -> {
                Field field = ReflectionUtils.findField(Lesson.class, k);
                field.setAccessible(true);
                ReflectionUtils.setField(field, lesson, v);
            });
            this.lessonService.save(lesson);
            return ResponseEntity.ok(lesson);
        } catch (Exception e) {
            log.error("Lesson is not found");
            return ResponseEntity.badRequest().body("Lesson is not found by code and name");
        }
    }

    @GetMapping(value = "/all")
    @ApiOperation("Returns all lessons")
    public ResponseEntity<List<Lesson>> getAllLessons(){
        return ResponseEntity.ok().body(lessonService.findAll());

    }

    @DeleteMapping(value = "/deleteByName")
    @ApiOperation("Deletes lesson by name and code")
    public ResponseEntity<?> deleteByName(@Valid @RequestHeader String name,
                                          @Valid @RequestHeader String code){

        if(lessonService.findByName(name).isPresent()&&lessonService.findByCode(code).isPresent()) {
            String id = lessonService.findByName(name).get().getId();
            lessonService.deleteById(id);
        }else return ResponseEntity.status(400).body("The lesson does not exist");
        return ResponseEntity.ok().body(lessonService.findAll());

    }


}
