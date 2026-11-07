# Diagramas — Arquitetura distribuída

Diagramas em Mermaid (renderizam no GitHub). Complementam o
[catálogo de eventos](../../../events.md) e as ADRs 004–007.

## Arquitetura de serviços

```mermaid
flowchart LR
    Client([Cliente / Atendente / Mecânico]) --> GW[API Gateway + JWT Authorizer]
    GW -->|/service-orders*| P[ms-pedidos]
    GW -->|/budgets*| O[ms-orcamentos]
    GW -->|demais rotas| M[mnl-oficina-mecanica]

    P --> PG1[(PostgreSQL<br/>ms_pedidos)]
    O --> PG2[(PostgreSQL<br/>ms_orcamentos)]
    M --> PG3[(PostgreSQL<br/>mnl)]
    E[ms-expedicao] --> DDB[(DynamoDB<br/>execution_jobs + GSI1)]

    P <--> K[(Kafka)]
    O <--> K
    E <--> K

    PG1 -. outbox .-> D1[Debezium Server]
    PG2 -. outbox .-> D2[Debezium Server]
    D1 --> K
    D2 --> K
    DDB -. Streams .-> L[Lambda publisher] --> K

    O --> MP[Mercado Pago<br/>Checkout Pro]

    P -. cascade REST .-> M
    M -. relatórios read-only .-> P

    subgraph Observabilidade
        A[Grafana Alloy] --> PR[Prometheus]
        A --> LO[Loki]
        A --> TE[Tempo]
        PR --> G[Grafana]
        LO --> G
        TE --> G
    end
```

## Fluxo feliz do Saga (coreografado)

```mermaid
sequenceDiagram
    autonumber
    actor C as Cliente
    participant P as ms-pedidos
    participant O as ms-orcamentos
    participant E as ms-expedicao
    participant K as Kafka

    C->>P: POST /service-orders/cascade
    P->>P: cria OS (PENDING)
    P->>K: SERVICE_ORDER_CREATED
    K->>O: SERVICE_ORDER_CREATED
    O->>O: gera orçamento + envia e-mail
    O->>K: BUDGET_CREATED
    K->>P: BUDGET_CREATED
    P->>P: OS -> AWAITING_APPROVAL

    C->>O: aprova orçamento (link)
    O->>K: BUDGET_APPROVED
    K->>P: BUDGET_APPROVED
    P->>P: OS -> APPROVED

    C->>O: paga (Checkout Pro)
    O->>O: webhook MP + reconsulta
    O->>K: PAYMENT_APPROVED
    K->>P: PAYMENT_APPROVED
    P->>P: OS -> IN_PROGRESS
    P->>K: SERVICE_ORDER_IN_PROGRESS
    K->>E: SERVICE_ORDER_IN_PROGRESS
    E->>E: enfileira job na Filial

    E->>K: EXECUTION_COMPLETED
    K->>P: EXECUTION_COMPLETED
    P->>P: OS -> COMPLETED

    C->>P: confirma entrega
    P->>P: OS -> DELIVERED
```

## Compensação: pagamento rejeitado

```mermaid
sequenceDiagram
    autonumber
    participant P as ms-pedidos
    participant O as ms-orcamentos
    participant E as ms-expedicao
    participant K as Kafka

    Note over P: OS em APPROVED
    O->>K: PAYMENT_REJECTED
    K->>P: PAYMENT_REJECTED
    P->>P: OS -> CANCELLED
    P->>K: SERVICE_ORDER_CANCELLED
    Note over E: nenhum job de execução criado
    Note over O: nenhum reembolso necessário (pagamento não aprovado)
```

## Compensação: cancelamento após pagamento

```mermaid
sequenceDiagram
    autonumber
    participant P as ms-pedidos
    participant O as ms-orcamentos
    participant K as Kafka

    Note over P: OS em IN_PROGRESS, pagamento aprovado
    P->>P: OS -> CANCELLED
    P->>K: SERVICE_ORDER_CANCELLED
    P->>K: PAYMENT_REFUND_REQUESTED
    K->>O: PAYMENT_REFUND_REQUESTED
    O->>O: reembolso Mercado Pago (idempotente)
    O->>K: PAYMENT_REFUNDED
    K->>P: PAYMENT_REFUNDED
```

## Máquina de estados da OS

```mermaid
stateDiagram-v2
    [*] --> PENDING : SERVICE_ORDER_CREATED
    PENDING --> IN_INSPECTION : SERVICE_ORDER_INSPECTION_STARTED
    PENDING --> CANCELLED : cancelamento
    IN_INSPECTION --> AWAITING_APPROVAL : BUDGET_CREATED
    IN_INSPECTION --> CANCELLED : cancelamento
    AWAITING_APPROVAL --> APPROVED : BUDGET_APPROVED
    AWAITING_APPROVAL --> CANCELLED : BUDGET_REJECTED
    APPROVED --> IN_PROGRESS : PAYMENT_APPROVED
    APPROVED --> CANCELLED : PAYMENT_REJECTED / timeout
    IN_PROGRESS --> COMPLETED : EXECUTION_COMPLETED
    IN_PROGRESS --> CANCELLED : cancelamento / timeout
    COMPLETED --> DELIVERED : SERVICE_ORDER_DELIVERED
    CANCELLED --> DELIVERED : entrega
    DELIVERED --> [*]
```

> `PARTIALLY_REJECTED` permanece fora de escopo (placeholder 501) e não aparece no fluxo atual.

## Chaves do DynamoDB

```mermaid
flowchart LR
    subgraph Base["execution_jobs (tabela base)"]
        J["PK SO#35;&lt;soId&gt; · SK JOB<br/>filialId, status, priority, createdAt"]
        T["PK SO#35;&lt;soId&gt; · SK TS#35;&lt;occurredAt&gt;#35;&lt;eventId&gt;"]
    end

    subgraph GSI["GSI1 (fila)"]
        Q["PK FILIAL#35;&lt;filialId&gt;#35;STATUS#35;&lt;status&gt;<br/>SK priority#35;createdAt#35;soId"]
    end

    J -. projeção .-> Q
    J --- T
```

