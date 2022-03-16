package io.javabrains.inbox.controllers;

import io.javabrains.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.emaillist.EmailListItemService;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class InboxController {

    @Autowired
    private InboxHelper helper;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailListItemService emailListItemService;

    @GetMapping(value = "/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model, @RequestParam(required = false) String folder){
        if(principal==null || !StringUtils.hasText(principal.getAttribute("login"))){
            return "index";
        }
        String userId = principal.getAttribute("login");

        // Fetching Folders for the view
        helper.getFolders(model, folderService, userId);

        // Fetching emails for user and folder
        String folderLabel;
        if(!StringUtils.hasText(folder)){
            folderLabel = "Inbox";
        }else {
            folderLabel = folder;
        }
        List<EmailListItem> emailListItems = emailListItemService.findAllByKey_UserIdAndKey_Label(userId, folderLabel);
        model.addAttribute("emailListOfFolder", emailListItems);
        model.addAttribute("folderLabel", folderLabel);
        return "inboxpage";
    }

}
