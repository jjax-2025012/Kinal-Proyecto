package com.josethjax.kinalapp.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {

    @GetMapping("/")
    public String root() {
        return "redirect:/index";
    }

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @Controller
    public class ErrorController {
        @GetMapping("/403")
        public String accessDenied(Model model, HttpServletRequest request) {
            // Por si queremos pasar un mensaje dinámico
            model.addAttribute("mensaje", "Solo los administradores pueden realizar esta acción");
            return "error/403";
        }
    }

}