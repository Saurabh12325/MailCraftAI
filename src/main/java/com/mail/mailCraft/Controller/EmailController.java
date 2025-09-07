package com.mail.mailCraft.Controller;

import com.mail.mailCraft.Entity.EmailEntity;
import com.mail.mailCraft.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailEntity emailEntity){
        String response = emailService.generateEmail(emailEntity);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
