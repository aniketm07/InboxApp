package io.javabrains.inbox.email;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class EmailService {
    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailListItemRepository emailListItemRepository;

    public Email findById(UUID id){
        Optional<Email> optionalEmail = emailRepository.findById(id);
        return optionalEmail.orElse(null);
    }

    public void sendEmail(String from, List<String> to, String subject, String body){
        Email email = new Email();
        email.setTo(to);
        email.setFrom(from);
        email.setSubject(subject);
        email.setBody(body);
        email.setId(Uuids.timeBased());
        emailRepository.save(email);

        to.forEach(id -> {
            createEmailListItem(to, email, id, "Inbox");
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
        emailListItem.setUnread(true);
        emailListItem.setTo(to);
        emailListItemRepository.save(emailListItem);
    }
}
