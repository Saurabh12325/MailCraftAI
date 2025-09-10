package com.mail.mailCraft.Controller;

import com.mail.mailCraft.Entity.EmailEntity;
import com.mail.mailCraft.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/email")
@RequiredArgsConstructor
@CrossOrigin("http://localhost:5174")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/generate")
    public ResponseEntity<String> generateEmail(@RequestBody EmailEntity emailEntity){
        String response = emailService.generateEmailReply(emailEntity);
        return ResponseEntity.status(HttpStatus.OK).body(response);

    }
}
