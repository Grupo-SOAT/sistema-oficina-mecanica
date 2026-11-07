# ADR004 - Decomposição em microsserviços

- **Número da ADR**: 004
- **Data**: 17 de outubro de 2026
- **Autor**: Grupo-SOAT
- **Status**: Aceita

## Contexto

A arquitetura atual exige a refatoração do sistema para, no mínimo, três microsserviços
independentes, cada um com repositório, infraestrutura e banco de dados próprios,
além de pelo menos um banco relacional e um não relacional. O sistema atual é um
monólito (`mnl-oficina-mecanica`) com um microsserviço de orçamentos
(`ms-orcamentos`) extraído anteriormente.

É necessário definir onde ficam as fronteiras: quem é dono da Ordem de Serviço,
quem processa orçamento e pagamento, quem executa a fila de trabalho e o que
permanece no monólito.

## Decisão

Adotar **quatro serviços** e dois repositórios de apoio:

| Serviço                       | Repositório                        | Responsabilidade                                                                   | Banco                        |
|-------------------------------|------------------------------------|------------------------------------------------------------------------------------|------------------------------|
| **SO Service**                | `ms-pedidos` (novo)                | Ordem de Serviço: criação (inclusive em cascata), transições de status, histórico. | PostgreSQL (`ms_pedidos`)    |
| **Billing Service**           | `ms-orcamentos` (existente)        | Orçamento, envio, decisão do cliente, pagamento Mercado Pago, reembolso.           | PostgreSQL (`ms_orcamentos`) |
| **Execution Service**         | `ms-expedicao` (novo)              | Fila de execução por Filial, diagnóstico/reparo, conclusão.                        | DynamoDB                     |
| **Registration/Auth Service** | `mnl-oficina-mecanica` (existente) | Clientes, veículos, catálogo, almoxarifado, usuários, autenticação, relatórios.    | PostgreSQL (`mnl`)           |

Repositórios de apoio: `e2e-oficina-mecanica` (testes de fluxo completo),
`k8s-infra-oficina-mecanica` (manifestos/Argo CD/Terraform) e
`db-oficina-mecanica` (infraestrutura do RDS).

Regras de fronteira:

1. **`ms-pedidos` é o único escritor do status da OS.** Os demais serviços publicam
   fatos; apenas o `ms-pedidos` executa a transição.
2. Nenhum serviço acessa o banco de outro. Integração somente por HTTP síncrono
   (quando necessário) ou eventos no Kafka.
3. A OS e seus itens de serviço pertencem ao `ms-pedidos`; o catálogo permanece no `mnl`.
4. O `mnl` deixa de expor `/service-orders` e passa a oferecer endpoints internos
   para o fluxo em cascata e leitura para relatórios.
5. O roteamento público é por path no API Gateway/Ingress: `/service-orders*` →
   `ms-pedidos`, `/budgets*` → `ms-orcamentos`, restante → `mnl`.

## Justificativa

- Atende ao requisito de três ou mais serviços independentes sem dissolver
  contextos que não geram valor de negócio separado (cadastros e autenticação).
- Mantém o monólito como o quarto serviço, aproveitando o que já está estável e
  reduzindo o risco de uma reescrita completa em quatro semanas.
- A separação segue os ciclos de negócio (OS, orçamento/pagamento, execução) e
  permite bancos com características distintas (SQL para transações, NoSQL para fila).
- O `ms-orcamentos` já existe e é evoluído, não recriado.
- A regra de escritor único elimina disputa de estado entre serviços.

## Consequências

### Positivas

- Fronteiras claras de dados e de deploy.
- Bancos independentes e credenciais separadas.
- Evolução e escala por serviço.
- O `ms-pedidos` concentra a máquina de estados, facilitando auditoria.

### Negativas

- Criação em cascata passa a ser uma chamada síncrona entre `ms-pedidos` e `mnl`,
  com necessidade de compensação.
- Relatórios que dependem de OS passam a consultar o `ms-pedidos`.
- Mais repositórios, pipelines e manifestos para operar.
- O monólito permanece como serviço, contrariando uma leitura mais "pura" de
  microsserviços; a justificativa é escopo e risco.

## Alternativas Consideradas

1. **Dissolver o monólito em serviços por contexto** (cadastros, auth, catálogo…).
   Mais puro, porém inviável no prazo e sem ganho de avaliação proporcional.
2. **Manter o monólito como dono da OS e criar apenas serviços satélites.**
   Criaria posse duplicada da OS; rejeitado.
3. **Fazer o `mnl` proxyar os endpoints extraídos (strangler facade).**
   Mantém URLs, mas transforma o monólito em proxy síncrono e acopla os deploys.

## Implementação

1. Criar `ms-pedidos` e `ms-expedicao`.
2. Extrair do `mnl` o domínio `serviceorder` e o domínio `service` (itens da OS).
3. Reduzir o `mnl`, adicionando endpoints internos de veículo/catálogo e leitura para relatórios.
4. Configurar os bancos e credenciais no `db-oficina-mecanica`.
5. Configurar o roteamento por path no `k8s-infra`.
6. Registrar os contratos de eventos em [`docs/events.md`](../../../events.md).

## Referências

- [Contratos de eventos](../../../events.md)
- [ADR005 - Saga coreografada](../ADR005_saga_coreografada/README.md)
- [ADR006 - DynamoDB no ms-expedicao](../ADR006_dynamodb_expedicao/README.md)

