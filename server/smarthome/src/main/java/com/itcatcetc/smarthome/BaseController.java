package com.itcatcetc.smarthome;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path="")
public class BaseController {

    @GetMapping
    public ResponseEntity<String> getBase(){
        return ResponseEntity.ok("Welcome to SmartHome API");
    }
}
