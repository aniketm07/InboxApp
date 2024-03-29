package io.javabrains.inbox.controllers;

import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailService;
import io.javabrains.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItemService;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.UUID;

@Controller
public class EmailController {

    @Autowired
    private InboxHelper helper;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private EmailListItemService emailListItemService;

    @GetMapping(value = "/emails/{id}")
    public String emailView(@AuthenticationPrincipal OAuth2User principal, Model model, @PathVariable("id") UUID id, @RequestParam(required = false) String folder) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }
        String userId = principal.getAttribute("login");
        model.addAttribute("userId", userId);

        Email email = emailService.findById(id);
        if (email == null) {
            return "inbox-page";
        }

        helper.markEmailRead(email.getId(), userId, folder);
        // Fetching Folders for the view
        helper.getFolders(model, folderService, userId);

        model.addAttribute("email", email);
        String toIds = String.join(", ", email.getTo());
        model.addAttribute("toIds", toIds);
        model.addAttribute("folderName", folder);
        return "email-page";
    }

    @RequestMapping("moveEmail/{id}")
    public ModelAndView moveEmail(HttpServletRequest request, @AuthenticationPrincipal OAuth2User principal, Model model, @PathVariable("id") UUID id, @RequestParam String folder, @RequestParam String curfolder) {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return new ModelAndView("redirect:/");
        }
        String userId = principal.getAttribute("login");
        Email email = emailService.findById(id);
        if (email == null) {
            return new ModelAndView("redirect:/");
        }

        if (StringUtils.hasText(curfolder)) {
            EmailListItem emailListItem = emailListItemService.findAllByKey(new EmailListItemKey(userId, curfolder, id));

            if (emailListItem != null && !curfolder.equals("Sent Items")) {
                emailListItemService.deleteEmailListItem(emailListItem);
                emailListItem.getKey().setLabel(folder);
                emailListItemService.addEmailListItem(emailListItem);
            }
        }
        return new ModelAndView("redirect:/");
    }
}
