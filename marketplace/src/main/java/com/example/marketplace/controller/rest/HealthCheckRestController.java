package com.example.marketplace.controller.rest;

import com.example.marketplace.model.ResponseModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class HealthCheckRestController {

    @GetMapping("/app/healthcheck")
    public ResponseModel<String> getHealthCheckResponse(){
        ResponseModel<String> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        result.setData("Healthy.");
        return result;
    }

    @GetMapping("/test")
    public ResponseModel<String> getGreeting(){
        ResponseModel<String> result = new ResponseModel<>();
        result.setStatus(200);
        result.setDescription("ok");
        result.setData("API is called");
        return result;
    }
}