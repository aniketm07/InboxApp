package io.javabrains.inbox.controllers;

import io.javabrains.inbox.folders.Folder;
import io.javabrains.inbox.folders.FolderService;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
public class InboxHelper {

    public void getFolders(Model model, FolderService folderService, String userId){
        // Fetching folders of User
        // Fetching default folders for the view
        List<Folder> defaultFolders =  folderService.fetchDefaultFolder(userId);
        // Fetching user specific folders for the view
        List<Folder> userFolders =  folderService.findAllByUserId(userId);
        if(userFolders!=null) {
            defaultFolders.addAll(userFolders);
        }
        model.addAttribute("defaultFolders", defaultFolders);
    }
}
