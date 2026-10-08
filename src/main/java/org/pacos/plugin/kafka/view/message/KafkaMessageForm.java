package org.pacos.plugin.kafka.view.message;

import java.util.List;
import java.util.Objects;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.pacos.plugin.kafka.backend.connection.KafkaConnection;
import org.pacos.plugin.kafka.backend.connection.KafkaConnectionService;
import org.pacos.plugin.kafka.backend.message.KafkaMessageDraft;
import org.pacos.plugin.kafka.backend.message.KafkaMessageService;
import org.pacos.plugin.kafka.backend.message.KafkaMessageTemplate;
import org.vaadin.addons.variablefield.data.Scope;

public class KafkaMessageForm<T extends KafkaMessageDraft> {

    private final KafkaConnectionService connections;
    private final KafkaMessageService messages;
    private final Class<T> type;
    private final boolean showTemplates;

    private ComboBox<Long> connection;
    private ComboBox<KafkaMessageTemplate> template;
    private Binder<T> binder;

    public KafkaMessageForm(
            KafkaConnectionService connections,
            KafkaMessageService messages,
            Class<T> type,
            boolean showTemplates) {
        this.connections = connections;
        this.messages = messages;
        this.type = type;
        this.showTemplates = showTemplates;
    }

    public Binder<T> create(VerticalLayout layout, List<Scope> scopes) {
        binder = new Binder<>(type);

        connection = createConnectionSelector();

        TextField topic = new TextField("Topic");
        topic.setWidthFull();

        TextField key = new TextField("Key (optional)");
        key.setWidthFull();

        TextArea payload = new TextArea("Payload");
        payload.setWidthFull();
        payload.setMinHeight("180px");

        bindFields(topic, key, payload);

        if (showTemplates) {
            template = createTemplateSelector();
            layout.add(createTemplateControls(), connection, topic, key, payload);
        } else {
            layout.add(connection, topic, key, payload);
        }

        return binder;
    }

    private ComboBox<Long> createConnectionSelector() {
        ComboBox<Long> selector = new ComboBox<>("Kafka connection");
        selector.setItems(connections.findAll().stream()
                .map(KafkaConnection::getId)
                .filter(Objects::nonNull)
                .toList());
        selector.setItemLabelGenerator(id ->
                connections.find(id).map(KafkaConnection::getName).orElse("#" + id));
        binder.forField(selector)
                .asRequired("Connection is required")
                .bind(KafkaMessageDraft::getConnectionId, KafkaMessageDraft::setConnectionId);
        return selector;
    }

    private ComboBox<KafkaMessageTemplate> createTemplateSelector() {
        ComboBox<KafkaMessageTemplate> selector = new ComboBox<>("Saved message configuration");
        selector.setItems(messages.templates());
        selector.setItemLabelGenerator(KafkaMessageTemplate::getName);
        return selector;
    }

    private HorizontalLayout createTemplateControls() {
        Button load = new Button("Load", event -> loadSelectedTemplate());
        return new HorizontalLayout(template, load);
    }

    private void loadSelectedTemplate() {
        KafkaMessageTemplate selected = template.getValue();
        if (selected == null) {
            return;
        }

        T bean = newBean();
        bean.setConnectionId(selected.getConnection().getId());
        bean.setTopic(selected.getTopic());
        bean.setMessageKey(selected.getMessageKey());
        bean.setPayload(selected.getPayload());
        binder.readBean(bean);
    }

    private void bindFields(TextField topic, TextField key, TextArea payload) {
        binder.forField(topic)
                .asRequired("Topic is required")
                .bind(KafkaMessageDraft::getTopic, KafkaMessageDraft::setTopic);
        binder.forField(key)
                .bind(KafkaMessageDraft::getMessageKey, KafkaMessageDraft::setMessageKey);
        binder.forField(payload)
                .asRequired("Payload is required")
                .bind(KafkaMessageDraft::getPayload, KafkaMessageDraft::setPayload);
    }

    public T newBean() {
        try {
            return type.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }

    public ComboBox<KafkaMessageTemplate> templateSelector() {
        return template;
    }

    public void refreshTemplates() {
        if (template != null) {
            template.setItems(messages.templates());
        }
    }

    public KafkaMessageTemplate toTemplate(String name, T bean) {
        KafkaMessageTemplate template = new KafkaMessageTemplate();
        template.setName(name);
        template.setConnection(connections.find(bean.getConnectionId()).orElseThrow());
        template.setTopic(bean.getTopic());
        template.setMessageKey(bean.getMessageKey());
        template.setPayload(bean.getPayload());
        return template;
    }

    public void bind(T bean) {
        binder.readBean(bean);
    }

    public T write() throws Exception {
        T bean = newBean();
        binder.writeBean(bean);
        return bean;
    }
}
