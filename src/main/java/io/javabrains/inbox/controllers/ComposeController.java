package io.javabrains.inbox.controllers;

import io.javabrains.inbox.email.EmailService;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class ComposeController {

    @Autowired
    private InboxHelper helper;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/compose")
    public String composeEmail(@AuthenticationPrincipal OAuth2User principal, Model model, @RequestParam(required = false) String to){

        if(principal==null || !StringUtils.hasText(principal.getAttribute("login"))){
            return "index";
        }
        String userId = principal.getAttribute("login");
        model.addAttribute("userId", userId);

        // Fetching Folders for the view
        helper.getFolders(model, folderService, userId);

        // Adding recipients list for reply and replyAll
        if(StringUtils.hasText(to)) {
            List<String> replyToIds = splitToIds(to);
            model.addAttribute("replyToIds", String.join(", ", replyToIds));
        }
        return "compose-page";
    }

    /*
        Split comma separated ids to List of Id's
     */
    private List<String> splitToIds(String to) {
        if(!StringUtils.hasText(to)){
            return new ArrayList<>();
        }
        return Arrays.stream(to.split(","))
                .map(StringUtils::trimWhitespace)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/send")
    public ModelAndView sendEmail(@AuthenticationPrincipal OAuth2User principal, @RequestBody MultiValueMap<String, String> formData){
        if(principal==null || !StringUtils.hasText(principal.getAttribute("login"))){
            return new ModelAndView("redirect:/");
        }
        String userId = principal.getAttribute("login");
        String body = formData.getFirst("body");
        String subject = formData.getFirst("subject");
        List<String> ids = splitToIds(formData.getFirst("toIds"));
        emailService.sendEmail(userId, ids, subject, body);
        return new ModelAndView("redirect:/");
    }

}
