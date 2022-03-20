package io.javabrains.inbox.emaillist;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class EmailListItemService {

    @Autowired
    private EmailListItemRepository emailListItemRepository;
    private final PrettyTime prettyTime = new PrettyTime();

    public List<EmailListItem> findAllByKey_UserIdAndKey_Label(String userId, String label) {
        List<EmailListItem> emailListItems = emailListItemRepository.findAllByKey_UserIdAndKey_Label(userId, label);
        prettyTime(emailListItems);
        return emailListItems;
    }

    public EmailListItem findAllByKey(EmailListItemKey key){
        Optional<EmailListItem> emailListItems = emailListItemRepository.findById(key);
        if(emailListItems.isPresent()) {
            prettyTime(List.of(emailListItems.get()));
            return emailListItems.get();
        }
        return null;
    }

    public void deleteEmailListItem(EmailListItem item){
        emailListItemRepository.delete(item);
    }

    public void addEmailListItem(EmailListItem item){
        emailListItemRepository.save(item);
    }

    public void updateEmailListItem(EmailListItem emailListItem){
        emailListItemRepository.save(emailListItem);
    }

    /*
        Pretty the time in moments ago format
     */
    private void prettyTime(List<EmailListItem> emailListItems) {
        emailListItems.forEach(email -> {
            UUID uuid = email.getKey().getTimeUUID();
            Date date =  new Date(Uuids.unixTimestamp(uuid));
            email.setAgoTimeString(prettyTime.format(date));
        });
    }
}
