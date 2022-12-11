package com.ikoembe.school.controller;

import com.ikoembe.school.models.Clazz;
import com.ikoembe.school.models.Lesson;
import com.ikoembe.school.repository.ClassRepository;
import com.ikoembe.school.services.ClassService;
import io.swagger.annotations.ApiOperation;
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

    @PostMapping(value = "/createClass")
    @ApiOperation("Creates class")
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
    @ApiOperation("Adds students into class")
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

        if(!classRepository.existsByStudents(accountId)){
            log.info("Student is not registered yet");
            Optional<Clazz> theClass = classRepository.findByName(className);
            theClass.get().getStudents().add(user.getAccountId());
            classRepository.save(theClass.get());
            return ResponseEntity.ok().body(theClass);

        }
        else return ResponseEntity
                .badRequest()
                .body(("Error: This student is already registered"));

    }

    @PostMapping(value = "/addTeacher")
    @ApiOperation("Adds teacher into class")
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
    @ApiOperation("Adds lesson into class")
    public ResponseEntity<?> addLesson(@RequestHeader String className,
                                        @RequestBody Lesson lesson){
        Optional<Clazz> theClass =null;
        try{
        theClass = classRepository.findByName(className);
                theClass.get().getLessons().add(lesson);
                classRepository.save(theClass.get());
        } catch (Exception e){
            log.error("Error : " + e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(("Error: " + e.getMessage()));
        }
        return ResponseEntity.ok().body(theClass);

    }

}



