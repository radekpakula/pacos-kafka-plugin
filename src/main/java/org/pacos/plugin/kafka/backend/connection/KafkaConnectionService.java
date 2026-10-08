package org.pacos.plugin.kafka.backend.connection;
import org.apache.kafka.clients.admin.AdminClient; import org.springframework.stereotype.Service; import org.springframework.transaction.annotation.Transactional; import java.util.*;
@Service public class KafkaConnectionService {
 private final KafkaConnectionRepository repo; public KafkaConnectionService(KafkaConnectionRepository repo){this.repo=repo;}
 @Transactional public KafkaConnection save(KafkaConnection c){return repo.save(c);} @Transactional public void delete(Long id){repo.deleteById(id);} public List<KafkaConnection> findAll(){return repo.findAll();} public Optional<KafkaConnection> find(Long id){return repo.findById(id);}
 public void test(KafkaConnection c) throws Exception {try(AdminClient a=AdminClient.create(properties(c))){a.listTopics().names().get();}}
 public List<String> topics(KafkaConnection c) throws Exception {try(AdminClient a=AdminClient.create(properties(c))){return a.listTopics().names().get().stream().sorted().toList();}}
 public Properties properties(KafkaConnection c){Properties p=new Properties();p.put("bootstrap.servers",c.getBootstrapServers());p.put("security.protocol",c.getSecurityProtocol());if(c.getSaslMechanism()!=null&&!c.getSaslMechanism().isBlank())p.put("sasl.mechanism",c.getSaslMechanism());if(c.getUsername()!=null&&!c.getUsername().isBlank())p.put("sasl.jaas.config","org.apache.kafka.common.security.plain.PlainLoginModule required username=\""+escape(c.getUsername())+"\" password=\""+escape(Optional.ofNullable(c.getPassword()).orElse(""))+"\";");parse(c.getAdditionalProperties(),p);return p;}
 private void parse(String raw,Properties p){if(raw==null)return;for(String line:raw.split("\\R")){String s=line.trim();if(s.isEmpty()||s.startsWith("#"))continue;int i=s.indexOf('=');if(i>0)p.put(s.substring(0,i).trim(),s.substring(i+1).trim());}}
 private String escape(String s){return s.replace("\\","\\\\").replace("\"","\\\"");}
}