package com.haloharry.AI_Resume_backend;

import com.haloharry.AI_Resume_backend.service.ResumeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class AiResumeBackendApplicationTests {

	@Autowired
	private ResumeService resumeService;


	@Test
	void contextLoads() throws IOException {

		resumeService.generateResumeResponse("I am Harsh Loshali with " +
				"2 years of java Development Experience. I have worked on microservices based " +
				"applications");
	}

}
