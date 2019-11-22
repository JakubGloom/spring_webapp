package net.atos.spring_webapp.controller;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.GetMapping;

//@Controller
public class ErrorPageController implements ErrorController {
    @Override
    public String getErrorPath() {
        return "/error";
    }

    @GetMapping("/error")
    public String getErrorPage() {
        return "requestError";
    }

    @GetMapping("/login_error")
    public String getLoginErrorPage(){
        return "loginError";
    }
}