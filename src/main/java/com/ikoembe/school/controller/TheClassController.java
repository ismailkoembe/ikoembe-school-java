package com.ikoembe.school.controller;

import com.ikoembe.school.models.TheClass;
import com.ikoembe.school.repository.TheClassRepository;
import lombok.extern.slf4j.Slf4j;
import model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/classes")
public class TheClassController {
    @Autowired
    TheClassRepository classRepository;

    @Autowired
    private RestTemplate restTemplate;

    @PostMapping(value = "/createClass")
    public ResponseEntity<?> createClass (@RequestBody TheClass theClass){
        if (classRepository.existsByName(theClass.getName())){
            return ResponseEntity
                    .badRequest()
                    .body(("Error: This class is already added"));
        }
        //TODO validate account ID and add role

        log.info("The class is added {}", theClass.getName());
        classRepository.save(theClass);
        return ResponseEntity.ok().body(theClass);
    }

    @PostMapping(value = "/addStudent")
    public ResponseEntity<?> addStudent(@RequestHeader String className, @RequestHeader String accountId){
        if (!classRepository.existsByName(className)){
            log.error("Class is not found");
            return ResponseEntity
                    .badRequest()
                    .body(("Error: This class is not found"));
        }
        //TODO validate account ID and add role
        User user = restTemplate.getForObject("http://localhost:8080/api/user/doSomething/{accountId}",
                User.class, accountId);
        log.info("Student is found. {accountId}", user.getAccountId());

        Optional<TheClass> theClass = classRepository.findByName(className);
        theClass.get().getStudents().add(user.getAccountId());
        classRepository.save(theClass.get());
        return ResponseEntity.ok().body(theClass);
    }

    @PostMapping(value = "/addTeacher")
    public ResponseEntity<?> addTeacher(@RequestHeader String className, @RequestHeader String accountId){
        if (!classRepository.existsByName(className)){
            log.error("Class is not found");
            return ResponseEntity
                    .badRequest()
                    .body(("Error: This class is not found"));
        }
        //TODO validate account ID and add role


        Optional<TheClass> theClass = classRepository.findByName(className);
        theClass.get().getTeachers().add(accountId);
        classRepository.save(theClass.get());
        return ResponseEntity.ok().body(theClass);
    }

}



