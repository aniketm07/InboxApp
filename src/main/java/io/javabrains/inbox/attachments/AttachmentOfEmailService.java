package io.javabrains.inbox.attachments;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Optional;
import java.util.UUID;

@Service
public class AttachmentOfEmailService {

    @Autowired
    AttachmentOfEmailRepository repository;
    public String saveAttachment(UUID id ,MultipartFile file) throws Exception {

        AttachmentsOfEmail attachments = new AttachmentsOfEmail();
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if(fileName.contains("..")){
                throw new Exception("File name contains invalid path sequence");
            }
            attachments.setData(ByteBuffer.wrap(file.getBytes()));
            attachments.setId(id);
            attachments.setFileName(fileName);
            String downloadURL = ServletUriComponentsBuilder.fromCurrentContextPath().path("/download/").path(attachments.getId().toString()).toUriString();
            attachments.setDownloadURL(downloadURL);
            attachments.setFileType(file.getContentType());
            repository.save(attachments);
            return fileName;
        } catch (IOException e) {
            throw new Exception("Could not upload file");
        }
    }

    public AttachmentsOfEmail getAttachment(UUID fileURL) {
        Optional<AttachmentsOfEmail> optionalAttachmentsOfEmail = repository.findById(fileURL);
        return optionalAttachmentsOfEmail.orElse(null);
    }
}
