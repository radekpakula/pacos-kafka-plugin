package org.pacos.plugin.kafka.camunda;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.pacos.base.camunda.BlockFormHandler;
import org.pacos.base.camunda.BlockMetadata;
import org.pacos.base.camunda.ExecutableBlock;
import org.pacos.base.camunda.ProcessVariableManager;
import org.pacos.base.camunda.ResultVariable;
import org.pacos.plugin.kafka.backend.connection.KafkaConnectionService;
import org.pacos.plugin.kafka.backend.message.KafkaMessageService;
import org.pacos.plugin.kafka.view.message.KafkaMessageForm;
import org.springframework.stereotype.Component;
import org.vaadin.addons.variablefield.data.Scope;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.binder.ValidationException;

@Component
public class KafkaSendMessageExecutionBlock implements ExecutableBlock<KafkaSendMessageRecord> {

    private static final Pattern VARIABLE = Pattern.compile("\$\{([^}]+)}");

    private final KafkaConnectionService connections;
    private final KafkaMessageService messages;

    public KafkaSendMessageExecutionBlock(KafkaConnectionService connections, KafkaMessageService messages) {
        this.connections = connections;
        this.messages = messages;
    }

    public BlockMetadata basicData() {
        return new BlockMetadata() {
            public String name() {
                return "Send Kafka message";
            }

            public String camundaDelegateName() {
                return "kafkaSendMessage";
            }

            public String[] group() {
                return new String[] { "Kafka" };
            }

            public ResultVariable[] resultVariables() {
                return new ResultVariable[0];
            }
        };
    }

    public BlockFormHandler<KafkaSendMessageRecord> blockForm() {
        return new BlockFormHandler<>() {
            public Class<KafkaSendMessageRecord> beanClas() {
                return KafkaSendMessageRecord.class;
            }

            public Binder<KafkaSendMessageRecord> createForm(VerticalLayout layout, List<Scope> scopes) {
                return new KafkaMessageForm<>(
                        connections,
                        messages,
                        KafkaSendMessageRecord.class,
                        false).create(layout, scopes);
            }

            public KafkaSendMessageRecord writeBean(Binder<?> binder) throws ValidationException {
                KafkaSendMessageRecord bean = new KafkaSendMessageRecord();
                binder.writeBean(bean);
                return bean;
            }
        };
    }

    public void execute(ProcessVariableManager manager, KafkaSendMessageRecord bean, List<Scope> scopes) throws Exception {
        if (bean == null) {
            throw new IllegalArgumentException("Kafka message configuration is missing");
        }
        Long connectionId = bean.getConnectionId();
        String topic = resolve(bean.getTopic(), manager);
        String key = resolve(bean.getMessageKey(), manager);
        String payload = resolve(bean.getPayload(), manager);
        messages.send(connectionId, topic, emptyToNull(key), payload);
    }

    private String resolve(String value, ProcessVariableManager manager) {
        if (value == null) {
            return null;
        }

        Matcher matcher = VARIABLE.matcher(value);
        StringBuffer out = new StringBuffer();

        while (matcher.find()) {
            Object valueObject = manager.getVariable(matcher.group(1).trim());
            matcher.appendReplacement(out,
                    Matcher.quoteReplacement(valueObject == null ? "" : String.valueOf(valueObject)));
        }

        matcher.appendTail(out);
        return out.toString();
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
