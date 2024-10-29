package com.apps.trader.controller;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/home")
public class HomeController {


    @GetMapping
    public String home() {
        return "welcome to trading platform";
    }
    
    
}
