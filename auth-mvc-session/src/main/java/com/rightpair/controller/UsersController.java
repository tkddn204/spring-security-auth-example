package com.rightpair.controller;

import com.rightpair.entity.Users;
import com.rightpair.security.AppUserDetails;
import com.rightpair.service.UsersService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/users")
public class UsersController {
    private final UsersService usersService;

    @GetMapping("/login")
    public String loginPage() {
        return "login-form";
    }

    @GetMapping("/signup")
    public String registerPage(Model model) {
        model.addAttribute("user", Users.empty());
        return "signup-form";
    }

    @PostMapping("/signup")
    public String signUp(@ModelAttribute("user")Users users, BindingResult result) {
        if (result.hasErrors()) {
            return "signup-form";
        }
        usersService.create(users);
        return "redirect:/users/login";
    }

    @GetMapping("/info/me")
    public String read(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        Users users = usersService.getById(appUserDetails.getId());
        model.addAttribute("user", users);
        return "user-info";
    }

    @PostMapping("/info/me/delete")
    public String delete(@AuthenticationPrincipal AppUserDetails appUserDetails) {
        usersService.delete(appUserDetails.getId());
        return "redirect:/logout";
    }
}
