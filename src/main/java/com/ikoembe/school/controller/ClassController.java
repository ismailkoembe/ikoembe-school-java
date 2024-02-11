package com.ikoembe.school.controller;

import com.ikoembe.school.models.Clazz;
import com.ikoembe.school.models.Lesson;
import com.ikoembe.school.repository.ClassRepository;
import com.ikoembe.school.services.ClassService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/classes")
public class ClassController {
    @Value("${userms.baseurl}")
    private String url;

    @Autowired
    ClassRepository classRepository;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ClassService classService;


//    public ClassController(@Value("${userms.baseurl}") String url ,RestTemplateBuilder builder) {
//    // Instead of creating bean, this constructor can also be used.
//
//        this.restTemplate = builder.rootUri(url).build();
//    }


    @GetMapping(value = "")
    @Operation(summary = "Gets all classes", description = "Gets all classes.")
    @ApiResponse(responseCode = "200", description = "All classes ",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clazz.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    public ResponseEntity<?> getClasses (){
        log.info("Get all classes");
        List<Clazz> all = classRepository.findAll();
        return ResponseEntity.ok().body(all);
    }

    @PostMapping(value = "/createClass")
    @Operation(summary = "Create a class", description = "Creates a new  class")
    @ApiResponse(responseCode = "200", description = "Class is created",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clazz.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    public ResponseEntity<?> createClass (@RequestBody Clazz clazz){
        if (classRepository.existsByName(clazz.getName())){
            return ResponseEntity
                    .badRequest()
                    .body(("Error: This class is already added"));
        }

        log.info("The class is added {}", clazz.getName());
        classRepository.save(clazz);
        return ResponseEntity.ok().body(clazz);
    }

    @PostMapping(value = "/addStudent")
    @Operation(summary = "Adds a student in a class", description = "Adds a student into given class. \n" +
            "If the class is not founds it throws an error. \n" +
            "By external call, it verifies that the given accountId is saved in userms.\n" +
            "If the accountid is not valid it throws an error.\n" +
            "if the student is already registered, it throws an error.")
    @ApiResponse(responseCode = "200", description = "Student is added successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clazz.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    public ResponseEntity<?> addStudent(@RequestHeader String className, @RequestHeader String accountId){
        if (!classRepository.existsByName(className)){
            log.error("Class is not found");
            return ResponseEntity
                    .badRequest()
                    .body(("Error: This class is not found"));
        }
        User user = restTemplate.getForObject(url+"findByAccountId/{accountId}",
                User.class, accountId);
        log.info("Student is found {}", user.getAccountId());

        if (!classRepository.existsByStudents(accountId)) {
            log.info("Student is not registered yet");
            Optional<Clazz> theClass = classRepository.findByName(className);
            theClass.get().getStudents().add(user.getAccountId());
            classRepository.save(theClass.get());
            return ResponseEntity.ok().body(theClass);

        } else return ResponseEntity
                .badRequest()
                .body(("Error: This student is already registered"));

    }

    @PostMapping(value = "/addTeacher")
    @Operation(summary = "Add a teacher in a class", description = "Adds a TEACHER into given class. \n" +
            "            \"If the class is not founds it throws an error. \\n\" +\n" +
            "            \"By external call, it verifies that the given teacher accountId is saved in userms.\\n\" +\n" +
            "            \"If the accountid is not valid it throws an error.\\n\" +\n" +
            "            \"if the teacher is already registered, it throws an error.")
    @ApiResponse(responseCode = "200", description = "A teacher is added successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clazz.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    public ResponseEntity<?> addTeacher(@RequestHeader String className,
                                        @RequestHeader String accountId){
        if (!classRepository.existsByName(className)){
            log.error("Class is not found");
            return ResponseEntity
                    .badRequest()
                    .body(("Error: This class is not found"));
        }
        Optional<Clazz> theClass =null;
        try {
            User user = restTemplate.getForObject(url+"findByAccountId/{accountId}",
                    User.class, accountId);
            log.info("The teacher is found {}", user.getAccountId());
            if(user!=null && !classRepository.existsByTeachers(accountId)){
                log.info("The teacher can be saved");
                theClass = classRepository.findByName(className);
                theClass.get().getTeachers().add(accountId);
                classRepository.save(theClass.get());
            }
        } catch (Exception e){
            log.error("Teacher is not found");
            return ResponseEntity
                    .badRequest()
                    .body(("Error: This teacher is already registered/not found"));
        }
        return ResponseEntity.ok().body(theClass);

    }

    @PostMapping(value = "/addLesson")
    @Operation(summary = "Add a lesson", description = "Adds a lesson into given class. \n" +
            "If the class is not founds it throws an error. \n" +
            "If the accountid is not valid it throws an error.\n" +
            "if the lesson is already added, it throws an error.")
    @ApiResponse(responseCode = "200", description = "The lesson is added successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Clazz.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    public ResponseEntity<?> addLesson(@RequestHeader String className,
                                        @RequestBody Lesson lesson){
        Optional<Clazz> theClass =null;
        try{
        theClass = classRepository.findByName(className);
            boolean isLessonAlreadyAdded = isLessonAlreadyAdded(lesson, theClass);
            if(!isLessonAlreadyAdded){
                theClass.get().getLessons().add(lesson);
                classRepository.save(theClass.get());
                log.info("The lesson {} with code {} is successfully added", lesson.getName(), lesson.getCode() );
            } else log.error("The lesson {} with code {} is added already", lesson.getName(), lesson.getCode() );

        } catch (Exception e){
            log.error("Error : " + e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(("Error: " + e.getMessage()));
        }
        return ResponseEntity.ok().body(theClass);

    }

    private boolean isLessonAlreadyAdded(Lesson lesson, Optional<Clazz> theClass) {
        boolean isLessonAlreadyAdded = theClass.get().getLessons().stream()
                .anyMatch(clazz -> clazz.getName().equals(lesson.getName())
                && clazz.getCode().equals(lesson.getCode()));
        return isLessonAlreadyAdded;
    }

}



