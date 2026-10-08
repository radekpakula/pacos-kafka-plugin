package org.pacos.plugin.kafka.backend.message;
public class KafkaMessageDraft {
 private Long connectionId; private String topic; private String messageKey; private String payload;
 public Long getConnectionId(){return connectionId;} public void setConnectionId(Long v){connectionId=v;} public String getTopic(){return topic;} public void setTopic(String v){topic=v;} public String getMessageKey(){return messageKey;} public void setMessageKey(String v){messageKey=v;} public String getPayload(){return payload;} public void setPayload(String v){payload=v;}
}