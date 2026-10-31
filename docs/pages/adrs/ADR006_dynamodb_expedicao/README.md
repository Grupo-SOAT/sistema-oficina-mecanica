# ADR006 - DynamoDB como banco do ms-expedicao

- **Número da ADR**: 006
- **Data**: 17 de outubro de 2026
- **Autor**: Grupo-SOAT
- **Status**: Aceita

## Contexto

A plataforma exige pelo menos um banco relacional e um não relacional. Os serviços
`ms-pedidos` e `ms-orcamentos` usam PostgreSQL por causa das transações de OS e
pagamento. O `ms-expedicao` tem outro perfil de acesso: fila de execução por
Filial, consulta direta por OS e histórico de status — alto volume, baixa
necessidade de joins e agregações.

Era necessário escolher o banco não relacional e desenhar as chaves de acesso.

## Decisão

Usar **Amazon DynamoDB** no `ms-expedicao`, com **chaves legíveis** e um índice
secundário global para a fila.

**Tabela `execution_jobs` (tabela base):**

| Item      | PK (imutável)         | SK                          | Uso                            |
|-----------|-----------------------|-----------------------------|--------------------------------|
| Job       | `SO#<serviceOrderId>` | `JOB`                       | leitura direta por OS          |
| Histórico | `SO#<serviceOrderId>` | `TS#<occurredAt>#<eventId>` | linha do tempo, somente append |

**GSI1 (fila):**

| GSI1 PK                             | GSI1 SK                                   | Projeção                                   |
|-------------------------------------|-------------------------------------------|--------------------------------------------|
| `FILIAL#<filialId>#STATUS#<status>` | `<priority>#<createdAt>#<serviceOrderId>` | `ALL` (ou `INCLUDE` com os campos da fila) |

Regras:

1. A fila é consultada por `Query` no GSI1, nunca por `Scan`.
2. Mudança de status é `UpdateItem` no atributo; o GSI1 se atualiza sozinho.
   Não há delete+put nem movimentação de item.
3. A PK da tabela base nunca muda durante o ciclo da OS.
4. Todas as chaves são strings legíveis, visíveis em logs, console e CloudWatch.
5. UUIDv3 foi considerada e **rejeitada** como chave de partição: o DynamoDB já
   distribui pelo hash, então UUID não melhora particionamento, e a chave opaca
   piora operação e cria contrato de derivação oculto entre serviços.
6. UUIDs permanecem apenas para identificadores de entidade/evento, sempre
   aleatórios (`UUIDv4`/`UUIDv7`).

## Justificativa

- **Nativo AWS:** sem operação de banco, integra com IAM, Streams e CloudWatch.
- **Aderente ao padrão de acesso:** a fila por Filial/status é exatamente um
  `Query` por partição com ordenação por sort key.
- **Escala e custo:** capacidade on-demand, custo baixo no volume do projeto.
- **Sem estado a operar no cluster:** diferente de MongoDB, não exige StatefulSet,
  backup e upgrade próprios.
- **Streams:** integra com o requisito de publicação de eventos via DynamoDB Streams.

## Consequências

### Positivas

- Zero operação de banco.
- Filas isoladas por Filial e status.
- Mudança de status sem movimentação de item.
- Integração natural com CloudWatch e Streams.

### Negativas

- GSI é eventualmente consistente; por alguns milissegundos um job pode aparecer
  no status antigo e no novo. A fila deduplica por `serviceOrderId`.
- Consultas ad hoc e agregações são limitadas; relatórios continuam no serviço de relatórios.
- Desenvolvimento local exige DynamoDB Local.
- Acoplamento ao DynamoDB/AWS (mitigado pelo adapter hexagonal).

## Alternativas Consideradas

1. **MongoDB.** Mais flexível para consultas e agregações, mas exige operar um
   banco stateful no EKS (backup, upgrade, monitoramento) e não traz vantagem para
   o padrão de fila.
2. **Redis.** Ótimo para fila, porém com durabilidade e consultas mais limitadas
   para histórico.
3. **PostgreSQL com JSONB.** Atenderia tecnicamente, mas não cumpre o requisito
   de banco não relacional.
4. **DynamoDB com chave derivada por UUIDv3.** Rejeitada: sem ganho de distribuição,
   chave opaca e acoplamento à regra de derivação.

## Implementação

1. Provisionar a tabela no `db-oficina-mecanica`/`k8s-infra` com streams habilitados
   e capacidade on-demand.
2. Criar o GSI1 conforme acima.
3. Implementar o acesso via adapter na arquitetura hexagonal do `ms-expedicao`.
4. Publicar mudanças via DynamoDB Streams + Lambda para `execution-events`.
5. Usar DynamoDB Local no `docker-compose.e2e.yaml` e no perfil local.

## Referências

- [Contratos de eventos](../../../events.md)
- [ADR004 - Decomposição em microsserviços](../ADR004_decomposicao_microsservicos/README.md)
- [Documentação DynamoDB](https://docs.aws.amazon.com/dynamodb/)

