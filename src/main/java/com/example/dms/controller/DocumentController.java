package com.example.dms.controller;

import com.example.dms.domain.Document;
import com.example.dms.domain.Role;
import com.example.dms.domain.User;
import com.example.dms.repos.DocumentRepo;
import com.example.dms.repos.UserRepo;
import com.example.dms.service.FinishComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.print.Doc;
import javax.validation.Valid;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

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
    public String showAllDocs(
            Integer curPage,
            @PageableDefault(size = 5,sort = {"id"}, direction = Sort.Direction.DESC) Pageable pageable,
            Model model,
            @AuthenticationPrincipal User user
    ) {

        Page<Document> page = null;
        curPage = 0;

        Set<Role> rolesUser = user.getRoles();
        Iterable<Long> UID = Collections.singleton(user.getId());

        model.addAttribute("user", user);

        if (!rolesUser.contains(Role.ADMIN)) {
            //page = documentRepo.findAllByAuthor( user, pageable);
            page = documentRepo.findAll(pageable);
            model.addAttribute("isUser", true);
        }
        if (rolesUser.contains(Role.ADMIN)) {
            page = documentRepo.findAll(pageable);
            model.addAttribute("isAdmin", true);
        }

        if (rolesUser.isEmpty()) return "kick";

        int totalPages = page.getTotalPages();
        if (totalPages > 0) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }

        model.addAttribute("page", page);
        model.addAttribute("currentPage", curPage );

        return "home";
    }

    @GetMapping("/create")
    public String addDocForm(
            @ModelAttribute Document document,
            Model model,
            @AuthenticationPrincipal User user
    ) {
        Set<Role> rolesUser = user.getRoles();
        if (!rolesUser.contains(Role.ADMIN)) {
            //page = documentRepo.findAllByAuthor( user, pageable);
            model.addAttribute("isUser", true);
        }
        if (rolesUser.contains(Role.ADMIN)) {
            model.addAttribute("isAdmin", true);
        }
        return "createDocForm";
    }

    @PostMapping("/create")
    public String addDoc(@Valid Document document,
                         BindingResult bindingResult,
                         @RequestParam String text,
                         @RequestParam String VUZ,
                         @RequestParam(required = false) Double finish,
                         @AuthenticationPrincipal User user
    ) throws IOException {
        if (bindingResult.hasErrors()) {
            return  "createDocForm";
        }
        document = new Document(text, user, VUZ, finish);
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
            @RequestParam("text") String text,
            @RequestParam("VUZ") String VUZ,
            @RequestParam("finish") Double finish,
            @RequestParam(name = "docId") Long id,
            Model model
    ) throws IOException {

        document = documentRepo.findDocumentById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("error", "Неверное имя документа");
            model.addAttribute("document", document);
            return "updateDocForm";
        }
        if (!text.isEmpty()) {
            document.setText(text);
        }
        if (!VUZ.isEmpty()) {
            document.setVUZ(VUZ);
        }
        if (finish != null) {
            document.setFinish(finish);
        }

        documentRepo.save(document);


        return "redirect:/documents";
    }

    @GetMapping("/sortAllDocs")
    private String compareResults(
            @AuthenticationPrincipal User user,
            Model model
    ) {

        ArrayList<Document> docs = documentRepo.findAll();
        FinishComparator fcomp = new FinishComparator();
        Collections.sort(docs, fcomp);
        model.addAttribute("docs", docs);
        model.addAttribute("user", user);
        return "result";
    }
}
