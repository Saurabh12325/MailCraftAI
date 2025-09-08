package com.mail.mailCraft.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mail.mailCraft.Entity.EmailEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EmailService {
    private final WebClient webClient;
    private final String ApiKey;

    public EmailService(WebClient.Builder webClientBuilder, @Value("${gemini.api.url}") String baseUrl,
                        @Value("${gemini.api.key}") String apiKey) {
        this.ApiKey = apiKey;
        this.webClient = webClientBuilder.baseUrl(baseUrl).build();



    }

    public String generateEmailReply(EmailEntity emailEntity) {
        //Preparing the prompt
       String prompt = buildPrompt(emailEntity);

        //Preparing the json body
        String requestBody = String.format(
                """
                {
                    "contents": [
                      {
                        "parts": [
                          {
                            "text": "%s"
                          }
                        ]
                      }
                    ]
                }
                """, prompt);

        String response = webClient.post()
                .uri(uriBuilder -> uriBuilder
                        .path("/v1beta/models/gemini-2.0-flash:generateContent")
                         .build())
                .header("X-goog-api-key",ApiKey)
                .header("Content-Type", "application/json")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        return extractResponse(response);

    }

    private String extractResponse(String response) {

        try {
            ObjectMapper objectMapper = new  ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);
          return jsonNode.path("candidates")
                    .get(0).path("content")
                    .path("parts").get(0)
                    .path("text")
                    .asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

    }

    private String buildPrompt(EmailEntity emailEntity) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a Professional Email reply for the following email : ");
        if(emailEntity.getTone()!=null && !emailEntity.getTone().isEmpty()){
             prompt.append("use a ").append(emailEntity.getTone()).append(" tone");
             //use a professional tone
        }
        prompt.append("Original Email: \n").append(emailEntity.getEmailContent());
        return  prompt.toString();
    }
}
