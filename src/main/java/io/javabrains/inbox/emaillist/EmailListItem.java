package io.javabrains.inbox.emaillist;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.Table;

import java.util.List;

@Table(value = "messages_by_user_folder")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmailListItem {

    @PrimaryKey
    private EmailListItemKey key;
    @CassandraType(type = CassandraType.Name.LIST, typeArguments = CassandraType.Name.TEXT)
    private List<String> to;
    @CassandraType(type = CassandraType.Name.TEXT)
    private String subject;
    @CassandraType(type = CassandraType.Name.BOOLEAN)
    private boolean isUnread;
    @Transient
    public String agoTimeString;

}
