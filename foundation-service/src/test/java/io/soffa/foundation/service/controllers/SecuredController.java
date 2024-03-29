package io.soffa.foundation.service.controllers;

import io.soffa.foundation.annotations.ApplicationRequired;
import io.soffa.foundation.annotations.Authenticated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Authenticated
public class SecuredController {

    @GetMapping("/secure")
    public String ping() {
        return "Secured";
    }

    @ApplicationRequired
    @GetMapping("/secure/full")
    public String pong() {
        return "Secured";
    }

}
