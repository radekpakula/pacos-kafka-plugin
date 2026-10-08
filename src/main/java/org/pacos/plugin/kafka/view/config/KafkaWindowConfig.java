package org.pacos.plugin.kafka.view.config;
import org.pacos.base.session.UserSession; import org.pacos.base.window.DesktopWindow; import org.pacos.base.window.config.WindowConfig; import org.pacos.plugin.kafka.security.KafkaPermissions; import org.pacos.plugin.kafka.view.PanelKafka; import org.springframework.stereotype.Component;
@Component public class KafkaWindowConfig implements WindowConfig {
 public String title(){return "Kafka";} public String icon(){return "img/icon/kafka.svg";} public Class<? extends DesktopWindow> activatorClass(){return PanelKafka.class;} public boolean isApplication(){return true;} public boolean isAllowMultipleInstance(){return false;} public boolean isAllowedForCurrentSession(UserSession s){return s.hasPermission(KafkaPermissions.VIEW);}
}