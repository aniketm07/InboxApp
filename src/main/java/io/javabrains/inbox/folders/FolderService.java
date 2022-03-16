package io.javabrains.inbox.folders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    public List<Folder> fetchDefaultFolder(String userId) {
        return Arrays.asList(new Folder(userId, "Inbox", "blue"), new Folder(userId, "Sent Items", "green"), new Folder(userId, "Important", "yellow"));
    }

    public List<Folder> findAllByUserId(String userId) {
        return folderRepository.findAllByUserId(userId);
    }
}
