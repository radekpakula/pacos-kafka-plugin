package org.pacos.plugin.kafka.security;
import org.pacos.base.security.Permission;
public enum KafkaPermissions implements Permission {
 VIEW("kafka.view","View Kafka","Kafka","View Kafka provider"), CONFIGURE("kafka.configure","Configure Kafka","Kafka","Manage Kafka connections and saved message configurations"), SEND("kafka.send","Send Kafka messages","Kafka","Send Kafka messages");
 private final String key,label,category,description; KafkaPermissions(String key,String label,String category,String description){this.key=key;this.label=label;this.category=category;this.description=description;}
 public String getKey(){return key;} public String getLabel(){return label;} public String getCategory(){return category;} public String getDescription(){return description;}
}