package io.javabrains.inbox.controllers;

import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;

@Controller
public class FolderController {

    @Autowired
    private FolderService folderService;

    @PostMapping("/save-folder")
    public ModelAndView saveFolder(@AuthenticationPrincipal OAuth2User principal, @RequestBody MultiValueMap<String, String> formData){
        if(principal==null || !StringUtils.hasText(principal.getAttribute("login"))){
            return new ModelAndView("redirect:/");
        }
        String userId = principal.getAttribute("login");
        String folderName = formData.getFirst("folderName");
        String folderColor = formData.getFirst("folderColor");
        Folder folder  = new Folder();
        folder.setUserId(userId);
        folder.setLabel(folderName);
        folder.setColor(folderColor.toLowerCase());
        folderService.saveFolder(folder);
        return new ModelAndView("redirect:/");
    }
}
