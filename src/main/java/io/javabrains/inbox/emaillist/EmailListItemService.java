package io.javabrains.inbox.emaillist;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import org.ocpsoft.prettytime.PrettyTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.UUID;

@Service
public class EmailListItemService {

    @Autowired
    private EmailListItemRepository emailListItemRepository;
    private final PrettyTime prettyTime = new PrettyTime();

    public List<EmailListItem> findAllByKey_UserIdAndKey_Label(String userId, String label) {
        List<EmailListItem> emailListItems = emailListItemRepository.findAllByKey_UserIdAndKey_Label(userId, label);
        emailListItems.forEach(email -> {
           UUID uuid = email.getKey().getTimeUUID();
           Date date =  new Date(Uuids.unixTimestamp(uuid));
           email.setAgoTimeString(prettyTime.format(date));
        });
        return emailListItems;
    }
}
