package io.javabrains.inbox.controllers;

import io.javabrains.inbox.attachments.AttachmentOfEmailService;
import io.javabrains.inbox.attachments.AttachmentsOfEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;


@Controller
public class AttachmentController {
    @Autowired
    AttachmentOfEmailService attachmentOfEmailService;
    @GetMapping("/download/{fileURL}")
    public ResponseEntity<Resource> downloadFile(@PathVariable("fileURL") UUID fileURL ){

        AttachmentsOfEmail attachmentsOfEmail = null;
        attachmentsOfEmail = attachmentOfEmailService.getAttachment(fileURL);
        System.out.println(attachmentsOfEmail.getId() + " " + attachmentsOfEmail.getDownloadURL() + " " + attachmentsOfEmail.getFileType());
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(attachmentsOfEmail.getFileType()))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + attachmentsOfEmail.getFileName() + "\"")
                .body(new ByteArrayResource(attachmentsOfEmail.getData().array()));
    }
}
