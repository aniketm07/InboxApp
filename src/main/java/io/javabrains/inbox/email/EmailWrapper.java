package io.javabrains.inbox.email;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EmailWrapper {

    private String toIds;

    private String from;

    @NotNull(message = "Subject should not be null")
    @NotBlank(message = "Subject should not be blank")
    private String subject;

    private String body;

    private MultipartFile attachments;
}
