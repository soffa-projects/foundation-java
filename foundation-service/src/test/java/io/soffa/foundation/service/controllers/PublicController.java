package io.soffa.foundation.service.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PublicController {

    @GetMapping("/public")
    public String hello() {
        return "Public";
    }

}
