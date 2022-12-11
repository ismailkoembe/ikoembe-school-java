package com.ikoembe.school.controller;

import com.ikoembe.school.models.Lesson;
import com.ikoembe.school.repository.LessonService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/lessons")
public class LessonController {

    @Autowired
    LessonService lessonService;

    @PostMapping(value = "/add")
    @ApiOperation("Creates a lesson")
    public ResponseEntity<?> addLesson (@Valid @RequestBody Lesson lesson){
        if (lessonService.existsByName(lesson.getName())
                &&lessonService.existsByCode(lesson.getCode())){
            return ResponseEntity
                    .badRequest()
                    .body(("Error: This lesson is already added"));
        }
        lessonService.save(lesson);
        log.info("A new lesson {}, {} added", lesson.getName(), lesson.getCode());
        return ResponseEntity.ok().body(lesson);

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
