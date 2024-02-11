package com.ikoembe.school.controller;

import com.ikoembe.school.models.Lesson;
import com.ikoembe.school.repository.LessonService;
import com.ikoembe.school.services.LessonServiceImplementation;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ReflectionUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

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
    @Operation(summary = "Adds a lesson", description = "Creates a lesson. If the lesson is already created, \\n\" +\n" +
            "            \"it throws an error")
    @ApiResponse(responseCode = "200", description = "Lesson is created succesfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    public ResponseEntity<?> addLesson (@Valid @RequestBody Lesson lesson){
        if (lessonService.existsByName(lesson.getName().name())
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
    @Operation(summary = "Updates given lesson", description = "Updates given lesson")
    @ApiResponse(responseCode = "200", description = "Lesson updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
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
    @Operation(summary = "Returns all lessons", description = "Returns all lessons")
    @ApiResponse(responseCode = "200", description = "Lesson updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    public ResponseEntity<List<Lesson>> getAllLessons(){
        return ResponseEntity.ok().body(lessonService.findAll());

    }

    @DeleteMapping(value = "/deleteByName")
    @Operation(summary = "Deletes lesson by name and code", description = "Deletes lesson by name and code")
    @ApiResponse(responseCode = "200", description = "Lesson updated successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Lesson.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    public ResponseEntity<?> deleteByName(@Valid @RequestHeader String name,
                                          @Valid @RequestHeader String code){

        if(lessonService.findByName(name).isPresent()&&lessonService.findByCode(code).isPresent()) {
            String id = lessonService.findByName(name).get().getId();
            lessonService.deleteById(id);
        }else return ResponseEntity.status(400).body("The lesson does not exist");
        return ResponseEntity.ok().body(lessonService.findAll());

    }


}
