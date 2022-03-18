package io.javabrains.inbox.controllers;

import io.javabrains.inbox.emaillist.EmailListItem;
import io.javabrains.inbox.emaillist.EmailListItemKey;
import io.javabrains.inbox.emaillist.EmailListItemService;
import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderService;
import io.javabrains.inbox.folders.UnreadEmailStats;
import io.javabrains.inbox.folders.UnreadEmailStatsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InboxHelper {

    @Autowired
    UnreadEmailStatsService unreadEmailStatsService;
    @Autowired
    EmailListItemService emailListItemService;

    public void getFolders(Model model, FolderService folderService, String userId) {

        // Fetching default folders for the view
        List<Folder> defaultFolders = folderService.fetchDefaultFolder(userId);

        // Fetching user specific folders for the view
        List<Folder> userFolders = folderService.findAllByUserId(userId);
        if (userFolders != null) {
            defaultFolders.addAll(userFolders);
        }

        // Fetching unread stats of all the folders for the given UserId
        List<UnreadEmailStats> unreadEmailStatsList = unreadEmailStatsService.findAllByUserId(userId);
        defaultFolders.forEach(folder -> {
            Optional<UnreadEmailStats> optional = unreadEmailStatsList
                    .stream()
                    .filter(unreadFolder -> folder.getLabel().equalsIgnoreCase(unreadFolder.getLabel()))
                    .findFirst();
            optional.ifPresent(unreadEmailStats -> folder.setUnreadCount(unreadEmailStats.getUnreadCount()));
        });

        model.addAttribute("defaultFolders", defaultFolders);
    }

    /*
        This will mark email as read when it is opened and decrement the counter in folders list
     */
    public void markEmailRead(UUID id, String userId, String folder) {
        EmailListItem email = emailListItemService.findAllByKey(new EmailListItemKey(userId, folder, id));
        if(email!=null && email.isUnread()) {
            email.setUnread(false);
            emailListItemService.updateEmailListItem(email);
            unreadEmailStatsService.decrementUnreadCounter(userId, folder);
        }
    }
}
