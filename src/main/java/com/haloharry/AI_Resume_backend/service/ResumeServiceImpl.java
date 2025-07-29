package com.haloharry.AI_Resume_backend.service;


import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

@Service
public class ResumeServiceImpl implements ResumeService{

    private ChatClient chatClient;

    public ResumeServiceImpl(ChatClient.Builder builder){
        this.chatClient= builder.build();
    }


    @Override
    public Map<String, Object> generateResumeResponse(String userResumeDescription) throws IOException {

        String systemPromptString=this.generatePromptFromFile("ai_resume_prompt.txt");
        String finalPrompt=this.putvaluesToTemplate(systemPromptString,Map.of(
                "userDescription",userResumeDescription
        ));

        Prompt prompt=new Prompt(finalPrompt);


        String response = chatClient.prompt(prompt).call().content();

        Map<String, Object> stringObjectMap = parseMultipleResponses(response);
        //modify :
        return stringObjectMap;
    }

    String generatePromptFromFile(String filename) throws IOException {
        Path path= new ClassPathResource(filename).getFile().toPath();
        return Files.readString(path);
    }
    String putvaluesToTemplate(String template, Map<String,String> values){
        for(Map.Entry<String,String> entry: values.entrySet()){
            template=template.replace("{{"+ entry.getKey()+"}}",entry.getValue());


        }
        return template;
    }

//    public static Map<String, Object> parseMultipleResponses(String response) {
//        Map<String, Object> jsonResponse = new HashMap<>();
//
//        // Extract content inside <think> tags
//        int thinkStart = response.indexOf("<think>") + 7;
//        int thinkEnd = response.indexOf("</think>");
//        if (thinkStart != -1 && thinkEnd != -1) {
//            String thinkContent = response.substring(thinkStart, thinkEnd).trim();
//            jsonResponse.put("think", thinkContent);
//        } else {
//            jsonResponse.put("think", null); // Handle missing <think> tags
//        }
//
//        // Extract content that is in JSON format
//        int jsonStart = response.indexOf("```json") + 7; // Start after ```json
//        int jsonEnd = response.lastIndexOf("```");       // End before ```
//        if (jsonStart != -1 && jsonEnd != -1 && jsonStart < jsonEnd) {
//            String jsonContent = response.substring(jsonStart, jsonEnd).trim();
//            try {
//                // Convert JSON string to Map using Jackson ObjectMapper
//                ObjectMapper objectMapper = new ObjectMapper();
//                Map<String, Object> dataContent = objectMapper.readValue(jsonContent, Map.class);
//                jsonResponse.put("data", dataContent);
//            } catch (Exception e) {
//                jsonResponse.put("data", null); // Handle invalid JSON
//                System.err.println("Invalid JSON format in the response: " + e.getMessage());
//            }
//        } else {
//            jsonResponse.put("data", null); // Handle missing JSON
//        }
//
//        return jsonResponse;
//    }
    public static Map<String, Object> parseMultipleResponses(String response) {
        Map<String, Object> jsonResponse = new HashMap<>();



        int thinkStart = response.indexOf("<think>") + 7;
        int thinkEnd = response.indexOf("</think>");
        if (thinkStart != -1 + 7 && thinkEnd != -1) {
           String thinkContent = response.substring(thinkStart, thinkEnd).trim();
           jsonResponse.put("think", thinkContent);
        } else {
            jsonResponse.put("think", null);
        }

        int braceCount = 0;
        int startIndex = -1;
        int endIndex = -1;

        for (int i = 0; i < response.length(); i++) {
            char c = response.charAt(i);
            if (c == '{') {
                if (braceCount == 0) {
                    startIndex = i;
             }
                braceCount++;
            } else if (c == '}') {
                braceCount--;
                if (braceCount == 0 && startIndex != -1) {
                    endIndex = i;
                break;
               }
            }
        }

        if (startIndex != -1 && endIndex != -1) {
            String jsonBlock = cleanJson(response.substring(startIndex, endIndex + 1));
            System.out.println(jsonBlock);
            try {
                ObjectMapper mapper = new ObjectMapper();
                Map<String, Object> data = mapper.readValue(jsonBlock, Map.class);
                jsonResponse.put("data", data);
            } catch (Exception e) {
                jsonResponse.put("data", null);
                System.err.println("Failed to parse JSON block: " + e.getMessage());
            }
        } else {
            jsonResponse.put("data", null);
        }

        return jsonResponse;
    }

    private static String cleanJson(String input) {
        // Remove trailing commas before closing array or object
        return input.replaceAll(",(\\s*[}\\]])", "$1");
    }
 }
