package com.jluslda.edu.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

@Controller
public class AdminController {
    @RequestMapping("/admin")
    String index(Model model) {
        return "admin/main";
    }

    @RequestMapping("/admin/community")
    String community(Model model) {
        return "admin/community";
    }

    @RequestMapping("/admin/article")
    String article(Model model) {
        return "admin/article";
    }

    @RequestMapping("/admin/video")
    String video(Model model) {
        return "admin/video";
    }

    @RequestMapping("/admin/document")
    String document(Model model) {
        return "admin/document";
    }

    @RequestMapping("/admin/user")
    String user(Model model) {
        return "admin/user";
    }

}
