package org.pacos.plugin.kafka.config;
import org.springframework.context.annotation.*;
@Configuration @ComponentScan(basePackages={"org.pacos.plugin.kafka.backend","org.pacos.plugin.kafka.view.config","org.pacos.plugin.kafka.security","org.pacos.plugin.kafka.camunda"}) public class KafkaPackageScanning {}