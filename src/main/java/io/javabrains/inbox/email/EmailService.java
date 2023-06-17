package io.javabrains.inbox.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.attachments.AttachmentOfEmailRepository;
import io.javabrains.inbox.attachments.AttachmentOfEmailService;
import io.javabrains.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItemRepository;
import io.javabrains.inbox.folders.UnreadEmailStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailListItemRepository emailListItemRepository;
    @Autowired
    private UnreadEmailStatsService unreadEmailStatsService;
    @Autowired
    private AttachmentOfEmailService attachmentOfEmailService;

    public Email findById(UUID id){
        Optional<Email> optionalEmail = emailRepository.findById(id);
        return optionalEmail.orElse(null);
    }

    public void sendEmail(String from, List<String> to, String subject, String body, MultipartFile file) throws Exception {
        Email email = new Email();
        email.setTo(to);
        email.setFrom(from);
        email.setSubject(subject);
        email.setBody(body);
        email.setId(Uuids.timeBased());
        if(!file.isEmpty()) {
            String fileName = attachmentOfEmailService.saveAttachment(email.getId(), file);
            List<String> attachments = new ArrayList<>();
            attachments.add(fileName);
            email.setAttachments(attachments);
        }
        emailRepository.save(email);

        to.forEach(id -> {
            createEmailListItem(to, email, id, "Inbox");
            unreadEmailStatsService.incrementUnreadCounter(id, "Inbox");
        });
        createEmailListItem(to, email, from, "Sent Items");
    }

    private void createEmailListItem(List<String> to, Email email, String id, String label) {
        EmailListItemKey key = new EmailListItemKey();
        EmailListItem emailListItem = new EmailListItem();
        key.setTimeUUID(email.getId());
        key.setLabel(label);
        key.setUserId(id);
        emailListItem.setKey(key);
        emailListItem.setSubject(email.getSubject());
        emailListItem.setUnread(!"Sent Items".equals(label));
        emailListItem.setTo(to);
        emailListItemRepository.save(emailListItem);
    }
}
