package io.javabrains.inbox.attachments;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.nio.ByteBuffer;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(value="attachments_of_email")
public class AttachmentsOfEmail {
    @PrimaryKey
    private UUID id;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String fileName;
    @CassandraType(type = CassandraType.Name.BLOB)
    private ByteBuffer data;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String downloadURL;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String fileType;
}
