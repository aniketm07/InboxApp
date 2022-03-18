package io.javabrains.inbox.folders;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.cassandra.core.cql.PrimaryKeyType;
import org.springframework.data.cassandra.core.mapping.CassandraType;
import org.springframework.data.cassandra.core.mapping.PrimaryKeyColumn;
import org.springframework.data.cassandra.core.mapping.Table;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(value = "unread_email_stats")
public class UnreadEmailStats {

    @PrimaryKeyColumn(name = "user_id", ordinal = 0,type = PrimaryKeyType.PARTITIONED)
    private String userId;
    @PrimaryKeyColumn(name = "label", ordinal = 1,type = PrimaryKeyType.CLUSTERED)
    private String label;
    @CassandraType(type = CassandraType.Name.COUNTER)
    private Integer unreadCount ;
}
