package com.rightpair.controller;

import com.rightpair.entity.Users;
import com.rightpair.security.AppUserDetails;
import com.rightpair.service.UsersService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

    @GetMapping("/info/me")
    public String infoPage(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        Users users = usersService.getById(appUserDetails.getId());
        model.addAttribute("user", users);
        return "user-info";
    }

    @GetMapping("/update")
    public String updatePage(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        Users users = usersService.getById(appUserDetails.getId());
        model.addAttribute("user", users);
        return "user-update";
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute("user")Users users, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/users/signup";
        }
        usersService.create(users);
        return "redirect:/users/login";
    }

    @PostMapping("/info/me/delete")
    public String delete(@AuthenticationPrincipal AppUserDetails appUserDetails) {
        usersService.delete(appUserDetails.getId());
        return "redirect:/users/logout";
    }

    @PostMapping("/update")
    public String update(
            @AuthenticationPrincipal AppUserDetails appUserDetails,
            @Valid @ModelAttribute("user")Users users, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/users/update";
        }
        usersService.updateNickName(appUserDetails.getId(), users);
        return "redirect:/users/info/me";
    }
}
