package net.atos.spring_webapp.controller;

import net.atos.spring_webapp.model.User;
import net.atos.spring_webapp.service.AutoMailingService;
import net.atos.spring_webapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.security.Principal;

@Controller
public class UserController {

    private UserService userService;
    private AutoMailingService autoMailingService;

    @Autowired
    public UserController(UserService userService, AutoMailingService autoMailingService) {
        this.userService = userService;
        this.autoMailingService = autoMailingService;
    }
    @GetMapping("/register")
    public String registerUser(Model model){
        model.addAttribute("user", new User());
        return "register";
    }
    @PostMapping("/register")
    public String registerUser(@ModelAttribute @Valid User user, BindingResult bindingResult){
        if(bindingResult.hasErrors()){
            return "register";
        }
        userService.registerUser(user); // automatycznie przypisujemy w metodzie uprawnienia ROLE_USER
        // wysylamy meila ja jest jus zapisany nowy uzytkownik w bazie danych
        autoMailingService.sendEmail(user.getEmail(),"dupa","dupa");
        return "redirect:/login";
    }
    @GetMapping("/login")
    public String login(){
        return "login";
    }

    @ResponseBody
    @GetMapping("/user")
    public Principal loggedUser(Principal principal){
        return principal;
    }
}
