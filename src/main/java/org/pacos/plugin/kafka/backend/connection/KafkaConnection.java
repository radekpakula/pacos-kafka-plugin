package org.pacos.plugin.kafka.backend.connection;
import jakarta.persistence.*; import java.time.LocalDateTime;
@Entity @Table(name="kafka_connection") public class KafkaConnection {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 @Column(nullable=false,unique=true,length=200) private String name;
 @Column(name="bootstrap_servers",nullable=false,length=2000) private String bootstrapServers;
 @Column(name="security_protocol",nullable=false,length=40) private String securityProtocol="PLAINTEXT";
 @Column(name="sasl_mechanism",length=80) private String saslMechanism;
 @Column(length=500) private String username; @Column(length=2000) private String password;
 @Column(name="additional_properties",length=10000) private String additionalProperties;
 @Column(name="created_at",nullable=false) private LocalDateTime createdAt; @Column(name="updated_at",nullable=false) private LocalDateTime updatedAt;
 @PrePersist void prePersist(){createdAt=LocalDateTime.now();updatedAt=createdAt;} @PreUpdate void preUpdate(){updatedAt=LocalDateTime.now();}
 public Long getId(){return id;} public String getName(){return name;} public void setName(String v){name=v;} public String getBootstrapServers(){return bootstrapServers;} public void setBootstrapServers(String v){bootstrapServers=v;} public String getSecurityProtocol(){return securityProtocol;} public void setSecurityProtocol(String v){securityProtocol=v;} public String getSaslMechanism(){return saslMechanism;} public void setSaslMechanism(String v){saslMechanism=v;} public String getUsername(){return username;} public void setUsername(String v){username=v;} public String getPassword(){return password;} public void setPassword(String v){password=v;} public String getAdditionalProperties(){return additionalProperties;} public void setAdditionalProperties(String v){additionalProperties=v;}
}