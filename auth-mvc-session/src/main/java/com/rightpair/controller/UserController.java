package com.rightpair.controller;

import com.rightpair.entity.User;
import com.rightpair.security.AppUserDetails;
import com.rightpair.service.UserService;
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
public class UserController {
    private final UserService userService;

    @GetMapping("/login")
    public String loginPage() {
        return "login-form";
    }

    @GetMapping("/signup")
    public String registerPage(Model model) {
        model.addAttribute("user", User.empty());
        return "signup-form";
    }

    @GetMapping("/info/me")
    public String infoPage(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        User user = userService.getById(appUserDetails.getId());
        model.addAttribute("user", user);
        return "user-info";
    }

    @GetMapping("/update")
    public String updatePage(@AuthenticationPrincipal AppUserDetails appUserDetails, Model model) {
        User user = userService.getById(appUserDetails.getId());
        model.addAttribute("user", user);
        return "user-update";
    }

    @PostMapping("/signup")
    public String signUp(@Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/users/signup";
        }
        userService.create(user);
        return "redirect:/users/login";
    }

    @PostMapping("/info/me/delete")
    public String delete(@AuthenticationPrincipal AppUserDetails appUserDetails) {
        userService.delete(appUserDetails.getId());
        return "redirect:/users/logout";
    }

    @PostMapping("/update")
    public String update(
            @AuthenticationPrincipal AppUserDetails appUserDetails,
            @Valid @ModelAttribute("user") User user, BindingResult result) {
        if (result.hasErrors()) {
            return "redirect:/users/update";
        }
        userService.updateNickName(appUserDetails.getId(), user);
        return "redirect:/users/info/me";
    }
}
