package com.example.dms.controller;

import com.example.dms.domain.Document;
import com.example.dms.domain.Role;
import com.example.dms.domain.User;
import com.example.dms.repos.DocumentRepo;
import com.example.dms.repos.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;

@Controller
public class DocumentController {

    @Autowired
    private DocumentRepo documentRepo;

    @Autowired
    private UserRepo userRepo;

    @Value("${upload.path}")
    private String uploadPath;

    @GetMapping("/")
    public String greeting() {
        return "startPage";
    }

    @GetMapping("/documents")
    public String showAllDocs(Model model,
                              @AuthenticationPrincipal User user
    ) {

        Set<Role> rolesUser = user.getRoles();
        Iterable<Long> UID = Collections.singleton(user.getId());

        model.addAttribute("user", user);

        if (!rolesUser.contains(Role.ADMIN)) {
            model.addAttribute("documents", documentRepo.findAllByAuthor(user));
        }
        if (rolesUser.contains(Role.ADMIN)) {
            model.addAttribute("documents", documentRepo.findAll());
            model.addAttribute("isAdmin", true);
        }
        if (rolesUser.isEmpty()) return "kick";
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
    public String addDoc(@Valid Document document,
                         BindingResult bindingResult,
                         @RequestParam MultipartFile file,
                         @RequestParam String text,
                         @AuthenticationPrincipal User user
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            return  "createDocForm";
        }
        document = new Document(text, user);
        addFile(document, file);
        documentRepo.save(document);
        return "redirect:/documents";
    }

    @GetMapping("/documents/{doc}")
    public String updateDoc(
            @PathVariable @ModelAttribute Document doc,
            Model model
    ) {
        //Document document = documentRepo.findDocumentById(doc);
        model.addAttribute("document", doc);

        return "updateDocForm";
    }

    @PostMapping("/documents")
    public String updateDocument(
            @Valid Document document,
            BindingResult bindingResult,
            @RequestParam MultipartFile file,
            @RequestParam("text") String text,
            @RequestParam(name = "docId") Long id,
            Model model
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            return "updateDocForm";
        }
        document = documentRepo.findDocumentById(id);

        document.setText(text); //тут ошибка



        if (!file.isEmpty()) {
            addFile(document, file);
        }

        documentRepo.save(document);


        return "redirect:/documents";
    }

    private void addFile(@RequestParam Document document, @RequestParam MultipartFile file) throws IOException {

        if (!file.isEmpty()) {

            File uploadDir = new File(uploadPath);

            if (!uploadDir.exists()) {
                uploadDir.mkdir();
            }

            String uuidFile = UUID.randomUUID().toString();
            String resultFileName = uuidFile + "." + file.getOriginalFilename();

            file.transferTo(new File(uploadPath + "/" + resultFileName));

            document.setFilename(resultFileName);
        } else document.setFilename("");
    }

}
