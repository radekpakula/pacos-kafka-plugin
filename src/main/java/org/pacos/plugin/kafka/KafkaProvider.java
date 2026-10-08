package org.pacos.plugin.kafka;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.spring.annotation.EnableVaadin;
import com.vaadin.flow.theme.Theme;
import org.pacos.config.property.WorkingDir;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
@SpringBootApplication(scanBasePackages={"org.pacos.core.config","org.pacos.plugin.*.config"})
@EnableVaadin(value={"org.pacos.core","org.pacos.base","org.pacos.plugin.*"}) @EnableAsync @Theme("desktop") @NpmPackage(value="line-awesome",version="1.3.0") @Push
public class KafkaProvider implements AppShellConfigurator { public static void main(String[] args){System.setProperty("workingDir","/opt/kafka-provider");WorkingDir.initialize();SpringApplication.run(KafkaProvider.class,args);} }