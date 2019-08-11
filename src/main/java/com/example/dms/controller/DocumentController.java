package com.example.dms.controller;

import com.example.dms.domain.Document;
import com.example.dms.domain.User;
import com.example.dms.repos.DocumentRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.print.Doc;
import javax.validation.Valid;
import java.util.Date;

@Controller
public class DocumentController {

    @Autowired
    private DocumentRepo documentRepo;


    @GetMapping("/")
    public String greeting() {
        return "startPage";
    }

    @GetMapping("/documents")
    public String showAllDocs(Model model) {
        model.addAttribute("documents", documentRepo.findAll());
        return "home";
    }

    @GetMapping("/create")
    public String addDocForm(
            @ModelAttribute Document document,
            Model model
    ) {
        return "createDocForm";
    }

    @PostMapping("/create")
    public String addDoc(Model model,
                         @RequestParam String text,
                         @AuthenticationPrincipal User user
                         ) {

        Document document = new Document(text, user);
        documentRepo.save(document);
        return "redirect:/documents";
    }

}
