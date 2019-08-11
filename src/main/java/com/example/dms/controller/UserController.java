package com.example.dms.controller;

import com.example.dms.domain.Role;
import com.example.dms.domain.User;
import com.example.dms.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserRepo userRepo;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());
        return "userList";
    }

    /*@GetMapping({"usr"})
    public String userEditForm(@PathVariable Long usr, Model model ) {
        User user = userRepo.findById(usr.toString());
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());
        return "userEditForm";
    }*/

    @GetMapping("{usr}")
    public String userEditForm(@PathVariable String usr, Model model) {
        User user = userRepo.findByUsername(usr);
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEditForm";
    }

    //@PostMapping("")
}
