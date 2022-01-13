package com.backend.hallodos.controller;

import com.backend.hallodos.DTO.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

@Controller
public class RegistrationController {

    @GetMapping("/user/register")
    @ResponseBody
    public String registrationForm(WebRequest request, Model model) {
        User users = new User();
        model.addAttribute("user", users);
        return "register";
    }

    public ModelAndView registerUserAccount(@ModelAttribute("user") @Validated User user, HttpServletRequest request, Errors errors) {
        return null;
    }
}
