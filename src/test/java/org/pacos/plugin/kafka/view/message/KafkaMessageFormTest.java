package org.pacos.plugin.kafka.view.message;

import java.util.List;

import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import org.junit.jupiter.api.Test;
import org.pacos.plugin.kafka.backend.connection.KafkaConnection;
import org.pacos.plugin.kafka.backend.connection.KafkaConnectionService;
import org.pacos.plugin.kafka.backend.message.KafkaMessageDraft;
import org.pacos.plugin.kafka.backend.message.KafkaMessageService;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KafkaMessageFormTest {

    @Test
    void whenCreateFormWithTemplatesThenTemplateSelectorIsCreated() {
        KafkaConnectionService connections = mock(KafkaConnectionService.class);
        KafkaMessageService messages = mock(KafkaMessageService.class);
        KafkaConnection connection = mock(KafkaConnection.class);

        when(connection.getId()).thenReturn(1L);
        when(connections.findAll()).thenReturn(List.of(connection));
        when(messages.templates()).thenReturn(List.of());

        KafkaMessageForm<KafkaMessageDraft> form =
                new KafkaMessageForm<>(connections, messages, KafkaMessageDraft.class, true);

        form.create(new VerticalLayout(), List.of());

        assertNotNull(form.templateSelector());
    }

    @Test
    void whenCreateFormWithoutTemplatesThenTemplateSelectorIsNotCreated() {
        KafkaConnectionService connections = mock(KafkaConnectionService.class);
        KafkaMessageService messages = mock(KafkaMessageService.class);
        KafkaConnection connection = mock(KafkaConnection.class);

        when(connection.getId()).thenReturn(1L);
        when(connections.findAll()).thenReturn(List.of(connection));

        KafkaMessageForm<KafkaMessageDraft> form =
                new KafkaMessageForm<>(connections, messages, KafkaMessageDraft.class, false);

        form.create(new VerticalLayout(), List.of());

        assertNull(form.templateSelector());
    }
}
