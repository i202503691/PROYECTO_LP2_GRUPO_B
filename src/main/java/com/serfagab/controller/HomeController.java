package com.serfagab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.serfagab.dto.AutenticacionFilter;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("filter", new AutenticacionFilter());
        return "login";
    }

    @GetMapping("dashboard")
    public String dashboard() {
        return "dashboard";
    }
}
