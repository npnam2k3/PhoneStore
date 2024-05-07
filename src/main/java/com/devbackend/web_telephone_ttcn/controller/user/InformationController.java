package com.devbackend.web_telephone_ttcn.controller.user;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
public class InformationController {
    @ModelAttribute
    public void addLoggedInUser(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        model.addAttribute("username", username);
    }

    @GetMapping("warrantyPolicy")
    public String warrantyPolicy(){
        return "user/warrantyPolicy";
    }

    @GetMapping("/aboutMe")
    public String aboutMe(){
        return "user/aboutMe";
    }

    @GetMapping("/contact")
    public String contact(){
        return "user/contact";
    }
}
