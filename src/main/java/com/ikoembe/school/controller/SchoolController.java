package com.ikoembe.school.controller;

import com.ikoembe.school.models.School;
import com.ikoembe.school.repository.SchoolRepository;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/school")
public class SchoolController {
    @Autowired
    SchoolRepository schoolRepository;

    @GetMapping("/isLive")
    @ApiOperation("Shows service is up and running")
    public ResponseEntity<?> isLive(@RequestHeader String something){
        return ResponseEntity.ok("Hello"+something);
    }

    @PostMapping("/addSchool")
    @PreAuthorize("hasRole('ADMIN')")
    @ApiOperation("Creates a school")
    public ResponseEntity<?> createSchool(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody School school){
        schoolRepository.save(school);
        return ResponseEntity.ok().body(school.getName());
    }
}
