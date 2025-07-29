package com.haloharry.AI_Resume_backend.controller;

import com.haloharry.AI_Resume_backend.ResumeRequest;
import com.haloharry.AI_Resume_backend.service.ResumeService;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/resume")
public class ResumeController {

    private ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/generate")
    public ResponseEntity<Map<String,Object>> getAIResumeData(
            @RequestBody ResumeRequest resumeRequest
            ) throws IOException {
        Map<String,Object> stringObjectMap= resumeService.generateResumeResponse(resumeRequest.userDescription());
        return new ResponseEntity<>(stringObjectMap, HttpStatus.OK);
    }




}
