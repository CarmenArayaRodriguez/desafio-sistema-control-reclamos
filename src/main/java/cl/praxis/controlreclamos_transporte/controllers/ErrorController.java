package cl.praxis.controlreclamos_transporte.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ErrorController implements org.springframework.boot.web.servlet.error.ErrorController {

    @RequestMapping("/error")
    public String handleError() {

        return "error";
    }

    public String getErrorPath() {
        return "/error";
    }
}



