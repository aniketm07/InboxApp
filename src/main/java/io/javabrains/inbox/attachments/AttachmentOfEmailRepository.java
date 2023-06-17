package io.javabrains.inbox.attachments;

import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AttachmentOfEmailRepository extends CassandraRepository<AttachmentsOfEmail, UUID> {
}
