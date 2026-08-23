# ADR003 - Stack de observabilidade self-hosted OTel-first

- **Número da ADR**: 003
- **Data**: 11 de agosto de 2026
- **Autor**: Erick Vinícius
- **Status**: Aceita

## Contexto

O monolito (`mnl-oficina-mecanica`) precisa de observabilidade para a Fase 3 do Tech Challenge: logs estruturados,
métricas, traces e health checks. Duas frentes coexistem:

1. **Aplicação** (o repo mencionado): padronização dos logs em formato JSON com identificadores indexáveis, métricas via
   Micrometer/Prometheus, health check e logs de negócio das mudanças de status da OS.
2. **Infraestrutura** (`k8s-infra-oficina-mecanica`): onde a stack de coleta/visualização é implantada.

Dito isso, havia uma escolha de stack a fazer: adotar New Relic (SaaS) imediatamente ou uma stack self-hosted baseada em
OpenTelemetry (LGTM), podendo migrar para New Relic posteriormente.

## Decisão

Adotar a **stack self-hosted OTel-first**:

- **Alloy** (Grafana) como coletor OTel, fazendo descoberta de targets e envio aos backends;
- **Prometheus** para métricas;
- **Loki** para logs;
- **Tempo** para traces;
- **Grafana** para dashboards e alertas (as-code).

Na aplicação:

- **OTel Java Agent** (auto-instrumentação) injetado via init container no repo de infra;
- **Logs** em JSON via `logstash-logback-encoder` para stdout;
- **Métricas** expostas em `/actuator/prometheus` (Micrometer);
- **Probes** de liveness/readiness/startup via actuator.

Correlação de logs: `trace_id` e `span_id` injetados no MDC pelo OTel Java Agent; `user_id` injetado no MDC pelo
`MdcUserFilter` (fora do domínio); `so_id` emitido como campo explícito via `StructuredArguments` nos use cases
necessários.

## Justificativa

- **Migrável para New Relic**: New Relic ingere OTLP nativamente, então a instrumentação (OTel) não muda; bastaria
  trocar o backend.
- **Sem vendor lock-in**: stack open-source, rodável localmente e no cluster.
- **Custo zero agora**: adequado ao escopo acadêmico do Tech Challenge.
- **Correlação sem acoplar o domínio**: `slf4j` apenas nos use cases; MDC gerenciado no adapter (`MdcUserFilter`);
  modelos de domínio permanecem puros.

## Consequências

### Consequências Positivas

- Observabilidade completa (logs + métricas + traces + health) com correlação entre sinais.
- Domínio desacoplado de Spring e de qualquer detalhe de observabilidade.
- Caminho de migração simples para New Relic.

### Consequências Negativas

- Operar a stack self-hosted (Alloy/Prometheus/Loki/Tempo/Grafana) exige esforço de infra.
- `user_id` limitado ao subject do JWT (CPF fica para a Fase 3).

## Alternativas Consideradas

### 1. New Relic imediato

**Prós:** zero operação; dashboards prontos.

**Contras:** custo; vendor lock-in; menos controle.

❌ Rejeitada nesta fase - podendo ser reconsiderada depois via OTLP sem re-instrumentar.

### 2. Logs sem JSON estruturado (formato texto padrão)

**Prós:** nenhuma dependência extra.

**Contras:** impossível indexar/correlacionar campos no Loki de forma eficiente.

❌ Rejeitada por inviabilizar a correlação de logs e alarmística.

## Implementação

1. `pom.xml`: adicionar `micrometer-registry-prometheus` e `logstash-logback-encoder`.
2. `application.properties`: expor `health,info,metrics,prometheus`; habilitar probes liveness/readiness; definir
   `spring.application.name`.
3. `logback-spring.xml`: appender JSON com `trace_id`/`span_id`/`user_id` no MDC.
4. `MdcUserFilter`: preenche `user_id` no MDC após autenticação; remove no `finally`.
5. Use cases de transição de OS: logar `service_order status transitioned` com `so_id`/`from_status`/`to_status`/
   `duration_in_status_seconds`.

## Referências

- [OpenTelemetry Java Instrumentation](https://opentelemetry.io/docs/zero-code/java/agent/)
- [Micrometer Prometheus](https://micrometer.io/docs/registry/prometheus)
- [logstash-logback-encoder](https://github.com/logfellow/logstash-logback-encoder)
- [Grafana Alloy](https://grafana.com/docs/alloy/latest/)
