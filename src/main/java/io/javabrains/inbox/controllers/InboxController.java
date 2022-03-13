package io.javabrains.inbox.controllers;

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

import java.util.List;

@Controller
public class InboxController {

    @Autowired
    private FolderRepository folderRepository;
    @Autowired
    private FolderService folderService;

    @GetMapping(value = "/")
    public String homePage(@AuthenticationPrincipal OAuth2User principal, Model model){
        if(principal==null || !StringUtils.hasText(principal.getAttribute("login"))){
            return "index";
        }
        String userId = principal.getAttribute("login");
        // Fetching default folders for the view
        List<Folder> defaultFolders =  folderService.fetchDefaultFolder(userId);

        // Fetching user specific folders for the view
        List<Folder> userFolders =  folderRepository.findAllByUserId(userId);
        if(userFolders!=null) {
            defaultFolders.addAll(userFolders);
        }
        model.addAttribute("defaultFolders", defaultFolders);
        return "inboxpage";
    }
}
