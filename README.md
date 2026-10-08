# PacOS Kafka Provider

Kafka provider plugin built against the PacOS extension model and the skeleton project.

## Configuration model

There are three distinct levels:

1. **KafkaConnection** – durable reusable connection resource. It contains bootstrap servers, security protocol, SASL settings and arbitrary Kafka properties.
2. **KafkaMessageTemplate** – durable reusable message configuration. It points to a connection and stores topic, key and payload.
3. **KafkaSendMessageRecord** – the configuration of one BPMN block. It stores concrete connection id, topic, key and payload values in the process model. Loading a template copies values into the BPMN configuration; later template edits therefore do not silently change existing processes.

## Reuse

A connection is reusable in all Kafka message operations. A saved message configuration can be loaded from the Kafka window and from the BPMN block editor. The BPMN block stores copied values, not only a template id, so deployed process definitions remain stable when reusable templates change.

## BPMN integration

`KafkaSendMessageExecutionBlock` implements PacOS `ExecutableBlock<KafkaSendMessageRecord>` and uses the existing `BlockFormHandler` contract. The same reusable `KafkaMessageForm` is used by the normal Kafka UI and the BPMN editor.

Runtime values support \`${variableName}\\` placeholders in topic, key and payload through PacOS `ProcessVariableManager`.

## Security

The current implementation persists Kafka credentials in the module database because it follows the persistence capabilities available in the skeleton. For production deployments, credentials should be protected by the PacOS secret/credential facility if one is enabled in the target installation.

## Local build

The project expects the PacOS Maven repository and PacOS 3.4.0 BOM.

```bash
mvn clean package
```

The shaded jar can then be imported into PacOS like other modules.
