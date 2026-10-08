# Architecture notes

## Shared application service

`KafkaConnectionService` owns persisted connection configurations. `KafkaMessageService` is the single send/topic-discovery entry point. Both the Vaadin module and BPMN block use these services.

## Manual UI

The module contains connection management and a manual send form. The send form exposes the same conceptual fields as the BPMN block: connection, topic, key and payload. Saved message configurations can be loaded, edited and sent.

## Reusable configuration model

There are three levels:

1. `KafkaConnection` — reusable broker connection resource.
2. `KafkaMessageTemplate` — reusable message configuration referencing a connection.
3. `KafkaSendMessageRecord` — process-local BPMN configuration containing copied values from a template.

A BPMN block does not store only a template id. Loading a template copies its values into the block model, so later edits to a reusable template do not silently change an already configured process.

## Shared BPMN form

`KafkaMessageForm` is the reusable Vaadin form component used by both `KafkaMessageView` and `KafkaSendMessageExecutionBlock`. `PanelKafka` remains only the PacOS desktop-window container.

This follows the PacOS `BlockFormHandler` contract and avoids maintaining a separate BPMN-specific form.

## BPMN runtime

`KafkaSendMessageExecutionBlock` implements PacOS `ExecutableBlock<KafkaSendMessageRecord>`. Runtime values are resolved with the existing PacOS `ProcessVariableManager`; ${variableName} placeholders are supported in topic, key and payload.

## Hardening

1. Protect Kafka credentials using the PacOS secret/credential mechanism when available.
2. Enforce `VIEW`, `CONFIGURE` and `SEND` permissions consistently across UI actions.
3. Improve template management with rename/delete UX and connection-scoped names when needed.
4. Add Avro/Protobuf serializers and Schema Registry support.
5. Expose producer metadata such as partition and offset as optional BPMN result variables.
6. Consider producer reuse/caching only if message throughput requires it.
7. Add API endpoints only when external automation needs direct access.
