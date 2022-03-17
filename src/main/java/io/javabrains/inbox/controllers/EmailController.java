package io.javabrains.inbox.controllers;

import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailService;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@Controller
public class EmailController {

    @Autowired
    private InboxHelper helper;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailService emailService;


    @GetMapping(value = "/emails/{id}")
    public String emailView(@AuthenticationPrincipal OAuth2User principal, Model model, @PathVariable("id") UUID id) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }
        String userId = principal.getAttribute("login");
        model.addAttribute("userId", userId);
        // Fetching Folders for the view
        helper.getFolders(model, folderService, userId);

        Email email = emailService.findById(id);
        if(email==null){
            return "inbox-page";
        }
        model.addAttribute("email", email);
        String toIds = String.join(", ", email.getTo());
        model.addAttribute("toIds", toIds);
        return "email-page";
    }
}
