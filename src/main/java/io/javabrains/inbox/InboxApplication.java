package io.javabrains.inbox;

import com.datastax.oss.driver.api.core.uuid.Uuids;
import io.javabrains.inbox.email.Email;
import io.javabrains.inbox.email.EmailRepository;
import io.javabrains.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItemRepository;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderRepository;
import io.javabrains.inbox.folders.UnreadEmailStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.cassandra.CqlSessionBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;

@SpringBootApplication
@RestController
public class InboxApplication {

    @Autowired
    UnreadEmailStatsRepository unreadEmailStatsRepository;
    @Autowired
    EmailListItemRepository emailListItemRepository;
    @Autowired
    EmailRepository emailRepository;


    public static void main(String[] args) {
        SpringApplication.run(InboxApplication.class, args);
    }

//	@RequestMapping("/user")
//	public String user(@AuthenticationPrincipal OAuth2User principal) {
//		System.out.println(principal);
//		return principal.getAttribute("name");
//	}

//    @PostConstruct
//    public void init() {
//        for (int i = 0; i < 10; i++) {
//            EmailListItem emailListItem  = new EmailListItem(new EmailListItemKey("aniketm07", "Inbox", Uuids.timeBased()),
//                    Arrays.asList("aniketm07","riasingh"), "TestDemo" + i, true,"");
//            emailListItemRepository.save(emailListItem);
//            emailRepository.save(new Email(emailListItem.getKey().getTimeUUID(),emailListItem.getTo(),
//                    "aniketm07", emailListItem.getSubject(), "BODY"+i));
//            unreadEmailStatsRepository.incrementUnreadCounter("aniketm07", "Inbox");
//        }
//    }

    @Bean
    public CqlSessionBuilderCustomizer sessionBuilderCustomizer(DataStaxAstraProperties astraProperties) {
        Path bundle = astraProperties.getSecureConnectBundle().toPath();
        return builder -> builder.withCloudSecureConnectBundle(bundle);
    }


}
