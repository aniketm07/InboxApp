package io.javabrains.inbox.controllers;

import io.javabrains.inbox.email.EmailService;
import io.javabrains.inbox.email.EmailWrapper;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.apache.tomcat.util.http.fileupload.FileUploadBase.MULTIPART_FORM_DATA;

@Controller
public class ComposeController {

    @Autowired
    private InboxHelper helper;
    @Autowired
    private FolderService folderService;
    @Autowired
    private EmailService emailService;

    @GetMapping(value = "/compose")
    public String composeEmail(@AuthenticationPrincipal OAuth2User principal, Model model, @RequestParam(required = false) String to) {

        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return "index";
        }
        String userId = principal.getAttribute("login");
        model.addAttribute("userId", userId);

        // Fetching Folders for the view
        helper.getFolders(model, folderService, userId);

        // Adding recipients list for reply and replyAll
        if (StringUtils.hasText(to)) {
            List<String> replyToIds = splitToIds(to);
            model.addAttribute("replyToIds", String.join(", ", replyToIds));
        }
        return "compose-page";
    }

    /*
        Split comma separated ids to List of Ids
     */
    private List<String> splitToIds(String to) {
        if (!StringUtils.hasText(to)) {
            return new ArrayList<>();
        }
        return Arrays.stream(to.split(","))
                .map(StringUtils::trimWhitespace)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @PostMapping(value = "/send", consumes = {MULTIPART_FORM_DATA})
    public ModelAndView sendEmail(@AuthenticationPrincipal OAuth2User principal,
                                  @Valid @ModelAttribute EmailWrapper formData) throws Exception {
        if (principal == null || !StringUtils.hasText(principal.getAttribute("login"))) {
            return new ModelAndView("redirect:/");
        }
        String userId = principal.getAttribute("login");
        String body = formData.getBody();
        String subject = formData.getSubject();
        List<String> ids = splitToIds(formData.getToIds());
        emailService.sendEmail(userId, ids, subject, body, formData.getAttachments());
        return new ModelAndView("redirect:/");
    }

}
