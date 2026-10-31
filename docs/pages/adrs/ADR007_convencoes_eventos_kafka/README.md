# ADR007 - Convenções de eventos e integração via Kafka

- **Número da ADR**: 007
- **Data**: 17 de outubro de 2026
- **Autor**: Grupo-SOAT
- **Status**: Aceita

## Contexto

Com a Saga coreografada, todos os serviços dependem do contrato de eventos.
Sem convenções explícitas, produtores e consumidores divergem, a ordenação quebra
e a operação fica difícil. A versão anterior usava dois tópicos específicos
(`budget-approval-request`, `budget-decision`) e payloads planos, sem envelope.

Era necessário definir: formato do payload, organização dos tópicos, chave,
garantias de entrega, versionamento e mecanismo de publicação (outbox/CDC).

## Decisão

### Formato

- Payload em **JSON**.
- **Envelope padrão** em todo evento: `eventId`, `type`, `version`, `occurredAt`,
  `correlationId`, `causationId`, `aggregateId`, `payload`.
- `type` é um **enum Java** (`EventType`), serializado em `UPPER_SNAKE_CASE`.
- Sem Schema Registry/Avro nesta versão.

### Tópicos

- **Topic-per-service**, kebab-case:
  `service-order-events`, `budget-events`, `execution-events`.
- Um `*.dlt` por tópico de origem.
- Chave da mensagem = `serviceOrderId` (string) → ordenação por OS.
- Retenção de 7 dias, sem compactação.
- Tópicos legados (`budget-approval-request`, `budget-decision`) são substituídos.

### Publicação e consumo

- **Outbox transacional** em `ms-pedidos` e `ms-orcamentos`, com **Debezium Server**
  por serviço relaying para o Kafka.
- **DynamoDB Streams → Lambda** para o `ms-expedicao`.
- Consumidores idempotentes por `eventId`, com retry, backoff e DLT.
- Guarda de estado para eventos fora de ordem.

### Versionamento

- Mudança aditiva mantém a versão.
- Mudança incompatível cria novo `type`/`version` e publica ambos durante a migração.
- Consumidores ignoram campos desconhecidos.
- O catálogo em [`docs/events.md`](../../../events.md) é a fonte de verdade.

## Justificativa

- JSON sem Schema Registry reduz atrito em quatro repositórios e mantém o foco na Saga.
- Topic-per-service reduz número de tópicos e garante ordem por OS com a mesma chave.
- Outbox + CDC elimina o dual-write (gravar no banco e publicar no Kafka fora de transação).
- Debezium Server (em vez de Kafka Connect compartilhado) preserva a posse por serviço
  e simplifica o deploy no cluster.
- Idempotência + DLT + guarda de estado tornam a coreografia tolerante a reentrega e ordem.

## Consequências

### Positivas

- Contrato único e documentado.
- Sem perda de evento por falha entre commit e publish.
- Ordenação por OS.
- Operação simples: um Debezium Server por serviço Postgres.

### Negativas

- Sem validação de schema em tempo de compilação; depende de disciplina e testes.
- Debezium Server adiciona um processo por serviço Postgres.
- CDC no DynamoDB exige Lambda e permissões adicionais.
- O catálogo precisa ser mantido sincronizado com o enum Java.

## Alternativas Consideradas

1. **Topic-per-event-type.** Mais simples de filtrar, porém multiplica tópicos e não
   melhora a ordenação.
2. **Avro + Schema Registry.** Contratos fortes, mas adiciona componente/licença e
   complexidade desproporcional ao escopo.
3. **Kafka Connect/Debezium compartilhado.** Um único cluster de conectores, porém
   concentra infraestrutura e reduz a autonomia por serviço.
4. **Relay de outbox na aplicação (polling).** Funciona, mas exige scheduler e
   aumenta a carga no banco; o CDC é mais direto.

## Implementação

1. Definir o enum `EventType` e as classes de envelope em cada serviço.
2. Criar os tópicos e DLTs.
3. Implementar outbox + Debezium Server nos serviços Postgres.
4. Implementar DynamoDB Streams + Lambda no `ms-expedicao`.
5. Implementar deduplicação, retry e DLT nos consumidores.
6. Documentar cada evento em `docs/events.md`.

## Referências

- [Contratos de eventos](../../../events.md)
- [ADR005 - Saga coreografada](../ADR005_saga_coreografada/README.md)
- [Debezium Server](https://debezium.io/documentation/reference/stable/operations/debezium-server.html)
- [DynamoDB Streams](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/Streams.html)

