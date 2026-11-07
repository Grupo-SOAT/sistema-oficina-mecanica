# ADR005 - Saga coreografada para o ciclo da OS

- **Número da ADR**: 005
- **Data**: 17 de outubro de 2026
- **Autor**: Grupo-SOAT
- **Status**: Aceita

## Contexto

O ciclo da OS atravessa três serviços: abertura no `ms-pedidos`, orçamento e
pagamento no `ms-orcamentos`, execução no `ms-expedicao`. O fluxo distribuído exige
o Saga Pattern com rollback/compensação em caso de falha, podendo ser orquestrado
ou coreografado, com a escolha documentada e justificada.

Parte do fluxo já usa Kafka (eventos de orçamento), e o domínio da OS já possui
uma máquina de estados implementada com o padrão State.

## Decisão

Adotar **Saga coreografada** sobre Kafka, sem orquestrador central.

1. Cada serviço reage a eventos e publica novos fatos.
2. O `ms-pedidos` é o **único** serviço que altera o status da OS.
3. As compensações usam apenas estados existentes: falha leva a `CANCELLED`.
   Não é criado estado artificial (`FAILED`, `EXECUTION_FAILED`).
4. Pagamento é pré-condição para execução: a OS só vai para `IN_PROGRESS` após
   `PAYMENT_APPROVED`.
5. Cada etapa aguardada possui timeout; o timeout dispara a mesma compensação de
   cancelamento e registra auditoria.
6. Consumidores são idempotentes (`eventId`) e aplicam guarda de estado, ignorando
   transições ilegais ou eventos fora de ordem.

Fluxo feliz e compensações: [diagramas do Saga](../../diagrams/distributed/README.md#fluxo-feliz-do-saga-coreografado).

- Fluxo feliz: seção "Fluxo feliz do Saga (coreografado)".
- Compensações: seções "Compensação: pagamento rejeitado" e "Compensação: cancelamento após pagamento".

## Justificativa

- **Sem componente novo:** não exige Camunda, Axon ou um orquestrador dedicado;
  usa o Kafka e os serviços já existentes.
- **Aderente ao que já existe:** o time já opera eventos de orçamento anteriormente.
- **Baixo acoplamento:** cada serviço conhece apenas os eventos que consome.
- **Estado único:** a regra de escritor único no `ms-pedidos` evita conflitos e
  mantém a máquina de estados como autoridade.
- **Compensação simples:** como falha sempre termina em `CANCELLED`, o rollback é
  determinístico e usa transições já permitidas pelo domínio.

## Consequências

### Positivas

- Nenhum ponto único de orquestração.
- Serviços evoluem e são implantados de forma independente.
- A máquina de estados existente é reaproveitada.
- Compensações auditáveis e testáveis.

### Negativas

- O fluxo é emergente: rastrear exige correlação (`correlationId`) e observabilidade.
- Ordenação entre tópicos diferentes não é garantida; depende de chave por OS,
  guarda de estado e idempotência.
- Um evento mal modelado pode gerar ciclos; o catálogo em `events.md` é obrigatório.
- Depuração é mais difícil do que com um orquestrador central.

## Alternativas Consideradas

1. **Saga orquestrada (orquestrador no `ms-pedidos`, Camunda/Axon ou custom).**
   Rollback mais explícito e fácil de demonstrar, mas adiciona componente/estado
   central e complexidade de framework. Rejeitada por escopo.
2. **Event sourcing do agregado OS.**
   Daria histórico completo, mas exigiria reescrever persistência e projeções.
   Rejeitada por prazo.
3. **Transações distribuídas (2PC).**
   Inviável entre PostgreSQL, DynamoDB e Mercado Pago.

## Implementação

1. Catálogo de eventos e tópicos em [`docs/events.md`](../../../events.md).
2. Outbox transacional + Debezium Server nos serviços Postgres.
3. DynamoDB Streams + Lambda no `ms-expedicao`.
4. Deduplicação por `eventId`, retry com backoff e DLT por tópico.
5. Timeouts por etapa e auditoria de compensação.
6. Propagação de `correlationId` e `traceparent`.

## Referências

- [Contratos de eventos](../../../events.md)
- [ADR004 - Decomposição em microsserviços](../ADR004_decomposicao_microsservicos/README.md)
- [ADR007 - Convenções de eventos](../ADR007_convencoes_eventos_kafka/README.md)

