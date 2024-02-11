package com.ikoembe.school.controller;

import com.ikoembe.school.models.School;
import com.ikoembe.school.repository.SchoolRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CrossOrigin(origins = "*", maxAge = 3600)
@RequestMapping("/api/school")
public class SchoolController {
    @Autowired
    SchoolRepository schoolRepository;

    @GetMapping("/isLive")
    @Operation(summary = "Shows service is up and running", description = "Shows service is up and running")
    @ApiResponse(responseCode = "200", description = "Lesson updated successfully",
            content = @Content(mediaType = "application/json"))
    public ResponseEntity<?> isLive(@RequestHeader String something){
        return ResponseEntity.ok("Hello"+something);
    }

    @PostMapping("/addSchool")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Creates a school", description = "Only admin user can create a school")
    @ApiResponse(responseCode = "200", description = "The school created successfully",
            content = @Content(mediaType = "application/json", schema = @Schema(implementation = School.class)))
    @ApiResponse(responseCode = "401", description = "Unauthorized", content = @Content)
    @ApiResponse(responseCode = "403", description = "Forbidden", content = @Content)
    public ResponseEntity<?> createSchool(
//            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @RequestBody School school){
        schoolRepository.save(school);
        return ResponseEntity.ok().body(school.getName());
    }
}
