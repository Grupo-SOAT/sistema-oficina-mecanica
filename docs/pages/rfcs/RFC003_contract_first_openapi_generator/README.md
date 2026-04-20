# Contract First com OpenAPI Generator

- **Número da RFC**: 0001
- **Data**: 20 de abril de 2026
- **Autores**: Erick Vinícius
- **Status**: Proposta

## Resumo

Adotar a abordagem **Contract First** para desenvolvimento e manutenção de APIs REST utilizando **OpenAPI Generator**.

## Contexto

Esse tipo de abordagem inverte o fluxo tradicional: definindo o contrato da API (OpenAPI Specification) primeiro, e gerando o código da implementação a partir deste contrato, garantindo consistência total entre documentação e implementação. E obrigando o time a manter a API minimamente documentada antes de começar a implementar novas funcionalidades.

## Proposta

Implementar uma estratégia **Contract First** através das seguintes práticas:

### 1. OpenAPI como Fonte Única da Verdade

- Manter `src/main/resources/openapi/api.yaml` como referência principal da API
- Estrutura modular com referências via `$ref` para endpoints e schemas
  - Cada path único deve contar com um arquivo `.yaml` na pasta `endpoints/` que lista todas as operações possíveis
  - Cada payload de requisição ou resposta deve contar com um arquivo `.yaml` na pasta `schemas/`
- Versionamento de mudanças na API refletido no arquivo OpenAPI

### 2. Geração Automática de Código

Utilizar o plugin do **OpenAPI Generator** para gerar as interfaces dos Controllers e os DTOs:

```bash
mvn clean generate-sources
```

> O resultado será armazenado em `./target/generated-sources/openapi/` e não deve ser versionado.

### 3. Fluxo de Desenvolvimento

**Antes (tradicional):**

```
Código Java -> Gerar Documentação -> Documentar Manualmente -> Deploy
```

**Depois (Contract First):**

```
OpenAPI Spec -> Gerar Código -> Implementar Lógica -> Deploy
```

### 4. Estrutura de Arquivos

```
src/main/resources/openapi/
├── api.yaml                    (contrato principal)
├── endpoints/                  (definições de operações)
│   ├── clients.yaml
│   ├── service-orders.yaml
│   └── ...
└── schemas/                    (modelos de dados)
    ├── ClientData.yaml
    ├── ClientRequest.yaml
    └── ...

target/generated-sources/openapi/
├── api/
│   ├── ClientsApi.java         (gerado)
│   ├── VehiclesApi.java        (gerado)
│   └── ...
└── models/
    ├── ClientData.java         (gerado)
    ├── ClientRequest.java      (gerado)
    └── ...

src/main/java/br/com/fiap/
├── .../controller/              (implementação manual)
│   ├── ClientsApiImpl.java      (implementa ClientsApi)
│   ├── VehiclesApiImpl.java     (implementa VehiclesApi)
│   └── ...
└── domain/                      (lógica de negócio)
```

## Justificativa

### 1. **Consistência Garantida**

- O código gerado sempre está sincronizado com a documentação
- Impossível ter discrepâncias entre contrato e implementação
- Conformidade com o padrão OpenAPI 3.x.x

### 2. **Velocidade de Desenvolvimento**

- Reduz o boilerplate (interfaces, DTOs, validações básicas)
- Permite focar em lógica de negócio, não em estrutura técnica
- Mudanças na API refletem instantaneamente no código

### 3. **Documentação Sempre Atualizada**

- A especificação gerada é a verdade absoluta
- Reduz necessidade de manutenção manual de docs

## Impactos

### Impacto na Arquitetura

- **Positivo**: Separação clara entre interface e implementação
- **Positivo**: Estrutura consistente e previsível
- **Positivo**: Facilita adição de novos contextos delimitados
- **Consideração**: Necessário configurar Maven/Gradle para executar gerador automaticamente

### Impacto nos Processos

- **Novo fluxo**: Atualizar OpenAPI -> Gerar código -> Implementar lógica
- **Novo padrão**: Equipe deve manter OpenAPI como contrato vivo
- **Benefício**: Code Review pode incluir validação do contrato
- **Benefício**: PRs focam mais em lógica de negócio, menos em estrutura técnica

### Impacto nos Recursos

- **Ferramentas**: OpenAPI Generator (Maven plugin)
- **Conhecimento**: Equipe precisa aprender YAML OpenAPI 3.x.x
- **CI/CD e Docker Image Build**: Devemos incluir passo de geração de código

## Alternativas Consideradas

### 1. **Abordagem tradicional: Swagger Docs nas classes Java**

**Descrição**: Documentar a API diretamente no código Java utilizando annotations como:

- SpringDoc OpenAPI (`@Operation`, `@ApiResponse`, `@Schema`)
- Swagger Annotations (`@Api`, `@ApiModel`, `@ApiParam`)
- javax.validation annotations

**Exemplo**:

```java
@RestController
@RequestMapping("/clients")
@Tag(name = "Clients", description = "Gerenciamento de clientes")
public class ClientController {
    
    @GetMapping
    @Operation(
        summary = "Listar clientes",
        description = "Retorna lista paginada de clientes"
    )
    @ApiResponse(
        responseCode = "200",
        description = "Sucesso",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = PaginatedClientResponse.class)
        )
    )
    public ResponseEntity<PaginatedClientResponse> listClients(
        @RequestParam(defaultValue = "10") int pageSize
    ) {
        // implementação
    }
}
```

**Prós**:

- Documentação próxima do código
- Menos arquivos para manter
- Ferramentas IDE integradas
- Execução mais rápida (sem passo de geração)

**Contras**:

- Annotations poluem o código
- Código grande e repetitivo
- Mudanças na documentação requerem mudanças no código Java
- DTOs criados manualmente, propensos a erros
- Sincronização manual entre documentação e implementação
- Difícil gerar clientes em outras linguagens

## Implementação

- Configuração do plugin no `pom.xml`
- Criação do arquivo pai `api.yaml`
- Criação dos endpoints sob demanda em `endpoints/`
- Criação dos DTOs sob demanda em `schemas/`

## Referências

- [OpenAPI Generator Documentation](https://openapi-generator.tech/)
- [OpenAPI Specification 3.0.3](https://spec.openapis.org/oas/v3.0.3)
- [Contract First API Design](https://www.swaggerhub.com/blog/api-design/contract-first-api-design/)
- [SpringDoc OpenAPI](https://springdoc.org/)
- [API-First Development](https://swagger.io/resources/articles/best-practices-in-api-first-design/)
