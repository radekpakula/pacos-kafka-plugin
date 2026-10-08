package org.pacos.plugin.kafka.config;
import org.flywaydb.core.Flyway; import org.springframework.beans.factory.annotation.*; import org.springframework.context.annotation.*;
@Configuration @PropertySource("classpath:kafka-module.properties") public class FlywayKafkaConfiguration {
 @Value("${kafka.datasource.url}") String url; @Value("${kafka.datasource.username}") String user; @Value("${kafka.datasource.password}") String password;
 @Bean @Primary @Qualifier("kafkaFlyWayMigration") Flyway kafkaFlyWayMigration(){Flyway f=Flyway.configure(getClass().getClassLoader()).baselineOnMigrate(true).outOfOrder(true).dataSource(url,user,password).locations("classpath:db/migration/kafka").load();f.migrate();return f;}
}