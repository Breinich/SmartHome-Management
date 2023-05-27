package com.itcatcetc.smarthome.login.email;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * a controller class for email
 */
@RestController
@RequestMapping(path = "api/v1/smarthome/email")
public class EmailController {

    @Autowired
    private EmailService emailService;

    // Sending a simple Email
    @PostMapping
    @PreAuthorize("hasRole('GUEST') or hasRole('HOMIE')")
    public String sendMail(@Valid @RequestBody EmailDetails details) {
        return emailService.sendSimpleMail(details);
    }
}