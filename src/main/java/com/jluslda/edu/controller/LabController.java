package com.jluslda.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class LabController {
    @RequestMapping("/lab/console")
    String console() {
        return "lab/console";
    }
}
