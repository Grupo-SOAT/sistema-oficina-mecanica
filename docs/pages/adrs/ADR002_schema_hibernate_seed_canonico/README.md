# ADR002 - Schema via Hibernate e seed canônico opcional

- **Número da ADR**: 002
- **Data**: 06 de maio de 2026
- **Autores**: Erick Vinícius
- **Status**: Proposta

## Contexto

A aplicação utiliza PostgreSQL como banco principal em runtime, mas ainda depende de H2 em memória para execução local e testes integrados. Essa abordagem gerou dois problemas objetivos:

1. o H2 não suporta adequadamente o mapeamento atual de `text[]` utilizado em `users.role`, o que impede validar localmente o comportamento real do domínio;
2. o schema e o seed foram espalhados por múltiplos arquivos e ambientes, criando divergência estrutural e funcional.

No estado atual, a responsabilidade de criação do banco está duplicada entre arquivos como:

- `docker-local/db-seed/create-db.sql`
- `src/main/resources/db/local-schema.sql`
- `src/test/resources/db/schema.sql`

Além disso, parte dos testes Cucumber depende de arquivos de seed específicos que hoje acumulam tanto baseline comum quanto preparação pontual de cenário.

## Decisão

Adotar as seguintes diretrizes:

### 1. Schema gerado pelo Hibernate

O schema relacional deixa de ser mantido manualmente em scripts SQL por ambiente e passa a ser gerado automaticamente pelo Hibernate/JPA a partir das entidades.

Diretriz por ambiente:

- **test**: `spring.jpa.hibernate.ddl-auto=create-drop`
- **local**: `spring.jpa.hibernate.ddl-auto=create-drop`
- **default/ci**: manter configuração conservadora compatível com o estágio atual do projeto

Com isso, scripts de schema duplicados tornam-se obsoletos.

### 2. Seed canônico opcional

Os dados mínimos compartilhados por execução local, docker-compose e testes integrados passam a existir em **um único arquivo canônico**.

Esse seed representa apenas o baseline comum da aplicação, por exemplo:

- usuários básicos
- fornecedores mínimos
- catálogo e insumos reutilizados por cenários recorrentes
- dados mínimos para autenticação e relatórios já implementados

O seed canônico deve ser **opcional** e controlado por propriedade, para evitar acoplamento com ambientes que não devam carregar massa padrão automaticamente.

Diretriz:

- propriedade: `app.seed.enabled`
- default: `false`
- `local` e `test`: `true`

### 3. Fixtures específicas de cenário continuam permitidas

Arquivos SQL que representam **delta de cenário** não são proibidos.

Exemplos válidos:

- limpar tabela para um cenário específico
- truncar dados que o cenário exige ausentes
- inserir combinação muito específica de registros para uma feature

A regra é:

- **baseline comum** → seed canônico
- **ajuste pontual de cenário** → fixture específica

Ou seja, um arquivo como `clear-services.sql` pode continuar existindo se ele representar preparação pontual e não bootstrap geral da aplicação.

## Consequências

### Positivas

- elimina incompatibilidade estrutural entre H2 e PostgreSQL
- reduz divergência entre local, testes e execução via docker-compose
- diminui manutenção duplicada de schema
- concentra a massa mínima em um único ponto de verdade
- deixa os cenários Cucumber mais explícitos: baseline comum + delta específico

### Negativas / trade-offs

- a criação do schema fica dependente da modelagem JPA atual, o que exige mais atenção à qualidade das entidades
- testes integrados passam a depender de container Docker ativo
- ainda não resolve governança de evolução de schema em ambientes persistentes, algo que futuramente pode exigir Flyway ou Liquibase

## Alternativas consideradas

### 1. Manter H2 com compatibilidade PostgreSQL

Rejeitada porque a compatibilidade é parcial e falha exatamente em um ponto relevante do modelo (`text[]`).

### 2. Manter schema manual e apenas trocar H2 por PostgreSQL em testes

Rejeitada porque preservaria a duplicação atual de schema por ambiente.

### 3. Adotar Flyway imediatamente

Boa alternativa de médio prazo, mas maior escopo do que o necessário para o problema atual. O projeto ainda está em WIP, com módulos incompletos, então a troca incremental por schema automático + seed canônico é mais proporcional neste momento.

## Implementação

1. Remover dependência operacional de H2 para local/testes integrados
2. Introduzir PostgreSQL via Testcontainers no profile de testes
3. Configurar profile local para usar PostgreSQL real
4. Centralizar seed canônico em arquivo único reutilizável
5. Carregar seed canônico de forma opcional via inicialização controlada
6. Remover scripts de schema duplicados e seeds redundantes
7. Ajustar testes Cucumber para usar baseline comum + fixtures específicas apenas onde necessário

## Referências

- `src/main/java/br/com/fiap/postech/adapter/output/user/persistence/entity/UserEntity.java`
- `src/test/resources/cucumber/reporting.feature`
- `src/test/resources/cucumber/supplies.feature`
- `src/test/resources/db/reporting-seed.sql`
- `src/test/resources/db/supplies-seed.sql`
