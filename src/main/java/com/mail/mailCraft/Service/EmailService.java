package com.mail.mailCraft.Service;

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
        )
           """ , prompt);

    }

    private String buildPrompt(EmailEntity emailEntity) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate a Professional Email for the following email : ");
        if(emailEntity.getTone()!=null && !emailEntity.getTone().isEmpty()){
             prompt.append("use a ").append(emailEntity.getTone()).append(" tone");
             //use a professional tone
        }
        prompt.append("Original Email: \n").append(emailEntity.getEmailContent());
        return  prompt.toString();
    }
}
