package io.javabrains.inbox.folders;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.cassandra.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UnreadEmailStatsRepository extends CassandraRepository<UnreadEmailStats, String> {
    public List<UnreadEmailStats> findAllByUserId(String id);

    @Query("update unread_email_stats set unreadcount = unreadcount+1 where user_id = ?0  and label = ?1")
    public void incrementUnreadCounter(String userId, String label);

    @Query("update unread_email_stats set unreadcount = unreadcount-1 where user_id = ?0  and label = ?1")
    public void decrementUnreadCounter(String userId, String label);

}
