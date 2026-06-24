package com.serfagab.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.serfagab.dto.AutenticacionFilter;
import com.serfagab.service.OrdenCompraService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final OrdenCompraService ordenCompraService;

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("filter", new AutenticacionFilter());
        return "login";
    }

    @GetMapping("dashboard")
    public String dashboard(Model model) {
        model.addAttribute("lstUltimasOrdenes", ordenCompraService.getAll().stream().limit(3).toList());
        return "dashboard";
    }
}
