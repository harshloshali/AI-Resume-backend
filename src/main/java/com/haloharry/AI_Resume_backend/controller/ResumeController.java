package com.haloharry.AI_Resume_backend.controller;

import com.haloharry.AI_Resume_backend.service.ResumeService;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin("*")
@RequestMapping("/api/v1/resume")
public class ResumeController {

    private ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/generate")
    public ResponseEntity<JSONObject> getAIResumeData(
            @RequestBody

            ){

    }




}
