package com.norbertkoziana.Session.Authentication.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
public class MainController {
    @GetMapping("/")
    public String homePage(){
        return "Hello World!";
    }

    @GetMapping("/private")
    public String test(){
        return "Hello logged in users!";
    }

}
