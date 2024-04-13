package com.devbackend.web_telephone_ttcn.controller.auth;

import com.devbackend.web_telephone_ttcn.dto.UserDto;
import com.devbackend.web_telephone_ttcn.entity.User;
import com.devbackend.web_telephone_ttcn.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class AuthController {
    @Autowired
    private UserService userService;



    @GetMapping("/register")
    public String showRegistrationform(Model model){
        UserDto userDto = new UserDto();
        model.addAttribute("user", userDto);
        return "auth/signup";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model){
        // kiem tra username co ton tai trong db hay khong
        User existingUser = userService.findUserByUsername(userDto.getUsername());
        if(existingUser != null && existingUser.getUsername() != null && !existingUser.getUsername().isEmpty()){
            result.rejectValue("username", null, "Username đã tồn tại");
        }

        // kiem tra email co ton tai trong db hay khong
        User existingUserEmail = userService.findUserByEmail(userDto.getEmail());
        if(existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()){
            result.rejectValue("email", null, "Email đã tồn tại");
        }
        if(result.hasErrors()){
            model.addAttribute("user", userDto);
            return "auth/signup";
        }
        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/login")
    public String login(){
        return "auth/login";
    }

    @GetMapping("/access-denied")
    public String accessDenied(){
        return "auth/access-denied";
    }
}
