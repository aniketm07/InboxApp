package io.javabrains.inbox.folders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UnreadEmailStatsService {
    @Autowired
    UnreadEmailStatsRepository unreadEmailStatsRepository;

    public void incrementUnreadCounter(String userId, String label){
        unreadEmailStatsRepository.incrementUnreadCounter(userId, label);
    }

    public void decrementUnreadCounter(String userId, String label){
        unreadEmailStatsRepository.decrementUnreadCounter(userId, label);
    }

    public List<UnreadEmailStats> findAllByUserId(String userId){
        return unreadEmailStatsRepository.findAllByUserId(userId);
    }
}
