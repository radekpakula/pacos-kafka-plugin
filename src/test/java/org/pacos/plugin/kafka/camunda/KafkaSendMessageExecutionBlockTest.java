package org.pacos.plugin.kafka.camunda;

import java.util.List;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.binder.Binder;
import org.junit.jupiter.api.Test;
import org.pacos.base.camunda.BlockFormHandler;
import org.pacos.base.utils.ObjectMapperUtils;
import org.pacos.plugin.kafka.backend.connection.KafkaConnection;
import org.pacos.plugin.kafka.backend.connection.KafkaConnectionService;
import org.pacos.plugin.kafka.backend.message.KafkaMessageService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class KafkaSendMessageExecutionBlockTest {

    @Test
    void whenCreateFormWithModelJsonThenRecordIsRestored() throws Exception {
        KafkaConnectionService connections = mock(KafkaConnectionService.class);
        KafkaMessageService messages = mock(KafkaMessageService.class);
        KafkaConnection connection = mock(KafkaConnection.class);

        when(connection.getId()).thenReturn(11L);
        when(connection.getName()).thenReturn("local");
        when(connections.findAll()).thenReturn(List.of(connection));

        KafkaSendMessageRecord expected = new KafkaSendMessageRecord();
        expected.setConnectionId(11L);
        expected.setTopic("orders");
        expected.setMessageKey("order-${orderId}");
        expected.setPayload("{"orderId":"${orderId}"}");

        ObjectMapper mapper = ObjectMapperUtils.getMapper();
        String modelJson = mapper.writeValueAsString(expected);

        KafkaSendMessageExecutionBlock block = new KafkaSendMessageExecutionBlock(connections, messages);
        BlockFormHandler<KafkaSendMessageRecord> handler = block.blockForm();
        Binder<KafkaSendMessageRecord> binder =
                handler.createForm(new VerticalLayout(), modelJson, List.of());

        KafkaSendMessageRecord actual = new KafkaSendMessageRecord();
        binder.writeBean(actual);

        assertEquals(expected.getConnectionId(), actual.getConnectionId());
        assertEquals(expected.getTopic(), actual.getTopic());
        assertEquals(expected.getMessageKey(), actual.getMessageKey());
        assertEquals(expected.getPayload(), actual.getPayload());
    }
}
