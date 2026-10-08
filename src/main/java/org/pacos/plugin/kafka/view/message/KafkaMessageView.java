package org.pacos.plugin.kafka.view.message;

import java.util.List;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import org.pacos.plugin.kafka.backend.connection.KafkaConnectionService;
import org.pacos.plugin.kafka.backend.message.KafkaMessageDraft;
import org.pacos.plugin.kafka.backend.message.KafkaMessageService;

public class KafkaMessageView extends VerticalLayout {

    private final KafkaMessageService messages;
    private final KafkaMessageForm<KafkaMessageDraft> form;
    private final Binder<KafkaMessageDraft> binder;

    public KafkaMessageView(KafkaConnectionService connections, KafkaMessageService messages) {
        this.messages = messages;
        form = new KafkaMessageForm<>(connections, messages, KafkaMessageDraft.class, true);
        binder = form.create(this, List.of());

        Button send = new Button("Send", event -> send());
        Button save = new Button("Save configuration", event -> save());
        add(new HorizontalLayout(send, save));
    }

    private void send() {
        try {
            KafkaMessageDraft bean = form.write();
            messages.send(bean.getConnectionId(), bean.getTopic(), emptyToNull(bean.getMessageKey()), bean.getPayload());
            Notification.show("Message sent");
        } catch (Exception e) {
            Notification.show("Send failed: " + e.getMessage());
        }
    }

    private void save() {
        try {
            KafkaMessageDraft bean = form.write();
            TextField name = new TextField("Configuration name");
            Dialog dialog = new Dialog(name);
            Button ok = new Button("Save", event -> {
                if (name.isEmpty()) {
                    name.setInvalid(true);
                    return;
                }

                try {
                    messages.saveTemplate(form.toTemplate(name.getValue(), bean));
                    dialog.close();
                    form.refreshTemplates();
                    Notification.show("Configuration saved");
                } catch (Exception e) {
                    Notification.show("Save failed: " + e.getMessage());
                }
            });

            dialog.add(ok);
            dialog.open();
        } catch (Exception e) {
            Notification.show("Validation failed: " + e.getMessage());
        }
    }

    private String emptyToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
