package com.example.dms.controller;

import com.example.dms.domain.Role;
import com.example.dms.domain.User;
import com.example.dms.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

    @GetMapping("{usr}")
    public String userEditForm(@PathVariable String usr, Model model) {
        User user = userRepo.findByUsername(usr);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEditForm";
    }

    @PostMapping
    public String updateUser(@RequestParam("userId") User user,
                             @RequestParam(value = "roles", required = false) Set<String> form,
                             @RequestParam String username
                             ) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                                    .map(Role::name)
                                    .collect(Collectors.toSet());
        user.getRoles().clear();

        if (form != null) {
            for (String key : form) {
                if (roles.contains(key)) {
                    user.getRoles().add(Role.valueOf(key));
                }
            }
        }

        userRepo.save(user);
        return "redirect:/user";
    }
}
