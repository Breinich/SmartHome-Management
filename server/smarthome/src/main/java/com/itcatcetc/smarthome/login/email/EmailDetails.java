package com.itcatcetc.smarthome.login.email;


import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor

public class EmailDetails {
    @Email
    private String recipient;
    private String msgBody;
    private String subject;
    private String attachment;
}