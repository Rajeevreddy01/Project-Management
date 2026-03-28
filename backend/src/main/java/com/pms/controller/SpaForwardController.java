package com.pms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SpaForwardController {

    @GetMapping({"/", "/login", "/register", "/dashboard", "/projects", "/projects/new", "/tasks", "/users"})
    public String forwardToIndex() {
        return "forward:/index.html";
    }

    @GetMapping("/projects/{id}/edit")
    public String forwardProjectEdit() {
        return "forward:/index.html";
    }
}
