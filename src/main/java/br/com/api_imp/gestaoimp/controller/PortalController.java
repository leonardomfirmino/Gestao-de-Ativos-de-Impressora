package br.com.api_imp.gestaoimp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PortalController {
    @GetMapping("/")
    public String portal() {
        return "index";
    }
}
