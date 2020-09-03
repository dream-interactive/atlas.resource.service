package com.atlas.core;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class AtlasApplication{
    public static void main(String[] args) {
        SpringApplication.run(AtlasApplication.class, args);
    }
}

@RestController

class Test{
    @GetMapping("TEST")
    public String test(){
        return "Its work";
    }
}