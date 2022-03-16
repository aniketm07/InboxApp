package io.javabrains.inbox.emaillist;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmailListItemRepository extends CassandraRepository<EmailListItem, EmailListItemKey> {

    public List<EmailListItem> findAllByKey_UserIdAndKey_Label(String userId, String label);
}
