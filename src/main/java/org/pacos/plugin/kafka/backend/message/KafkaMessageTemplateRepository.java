package org.pacos.plugin.kafka.backend.message;
import org.springframework.data.jpa.repository.JpaRepository; import java.util.*;
public interface KafkaMessageTemplateRepository extends JpaRepository<KafkaMessageTemplate,Long>{Optional<KafkaMessageTemplate> findByName(String name); List<KafkaMessageTemplate> findAllByOrderByNameAsc();}