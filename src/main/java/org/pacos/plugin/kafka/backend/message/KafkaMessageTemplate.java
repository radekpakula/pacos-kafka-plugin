package org.pacos.plugin.kafka.backend.message;
import jakarta.persistence.*; import org.pacos.plugin.kafka.backend.connection.KafkaConnection; import java.time.LocalDateTime;
@Entity @Table(name="kafka_message_template") public class KafkaMessageTemplate {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=200) private String name;
 @ManyToOne(fetch=FetchType.LAZY,optional=false) @JoinColumn(name="connection_id",nullable=false) private KafkaConnection connection;
 @Column(nullable=false,length=1000) private String topic; @Column(name="message_key",length=4000) private String messageKey; @Lob @Column(nullable=false) private String payload;
 @Column(name="created_at",nullable=false) private LocalDateTime createdAt; @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
 @PrePersist void pre(){createdAt=LocalDateTime.now();updatedAt=createdAt;} @PreUpdate void upd(){updatedAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;} public KafkaConnection getConnection(){return connection;} public void setConnection(KafkaConnection v){connection=v;} public String getTopic(){return topic;} public void setTopic(String v){topic=v;} public String getMessageKey(){return messageKey;} public void setMessageKey(String v){messageKey=v;} public String getPayload(){return payload;} public void setPayload(String v){payload=v;}
}