package org.pacos.plugin.kafka.backend.connection;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface KafkaConnectionRepository extends JpaRepository<KafkaConnection,Long>{Optional<KafkaConnection> findByName(String name);}