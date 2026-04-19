# ADR001 - Arquitetura de Código Java e DDD - Fase 1

- **Número da ADR**: 0001  
- **Data**: 19 de abril de 2026  
- **Autor**: Andre Lui  
- **Status**: **Aceita**

---

## Contexto

O sistema da oficina mecânica está sendo desenvolvido utilizando Java com Spring Boot, seguindo princípios de Domain-Driven Design (DDD).

O projeto encontra-se na **Fase 1 (MVP)**, com as seguintes características:

- Arquitetura monolítica
- Equipe de desenvolvimento em fase inicial
- Necessidade de entrega rápida
- Baixa complexidade operacional inicial
- Evolução futura planejada para arquitetura distribuída

Os principais agregados identificados no domínio são:

- Ordem de Serviço (OS)
- Veículo
- Cliente
- Insumo
- Serviço
- Orçamento

Dado esse contexto, surgiu a necessidade de definir uma arquitetura de código que:

- Seja simples de entender e aplicar
- Mantenha alinhamento com DDD
- Permita evolução futura sem alto custo de refatoração
- Facilite onboarding de novos desenvolvedores

---

## Decisão

Adotar uma arquitetura baseada em:

### **Separação por contexto (feature-based) + padrão MVC interno por agregado**

Ou seja:

- Cada agregado terá sua própria estrutura de pastas
- Dentro de cada agregado, será aplicado o padrão MVC (adaptado ao backend)
- Organização baseada em **features/domínio**, e não apenas em camadas técnicas globais

---

### Estrutura de pastas proposta

```plaintext
src/main/java/com/oficina

├── cliente
│   ├── controller
│   │   └── ClienteController.java
│   ├── service
│   │   └── ClienteService.java
│   ├── repository
│   │   └── ClienteRepository.java
│   ├── dto
│   │   ├── ClienteRequestDTO.java
│   │   └── ClienteResponseDTO.java
│   ├── entity
│   │   └── Cliente.java
│   └── mapper
│       └── ClienteMapper.java
│
├── veiculo
│   ├── controller
│   ├── service
│   ├── repository
│   ├── dto
│   ├── entity
│   └── mapper
│
├── ordemservico
│   ├── controller
│   ├── service
│   ├── repository
│   ├── dto
│   ├── entity
│   └── mapper
│
├── orcamento
│   ├── controller
│   ├── service
│   ├── repository
│   ├── dto
│   ├── entity
│   └── mapper
│
├── insumo
│   ├── controller
│   ├── service
│   ├── repository
│   ├── dto
│   ├── entity
│   └── mapper
│
├── servico
│   ├── controller
│   ├── service
│   ├── repository
│   ├── dto
│   ├── entity
│   └── mapper
│
└── shared
    ├── exception
    ├── config
    └── util
```
---

### Características da abordagem

- Organização por **agregado (feature-first)**
- Aplicação de MVC dentro de cada contexto
- Baixo acoplamento entre agregados
- Alto grau de coesão interna

---

## Justificativa

### 1. Alinhamento com DDD

Embora simplificada, a abordagem respeita conceitos importantes:

- Separação por **bounded contexts (agregados)**
- Isolamento de responsabilidades por domínio
- Organização orientada ao negócio

Cada pasta representa um **contexto de domínio claro**, facilitando entendimento e evolução.

---

### 2. Simplicidade para MVP

A escolha evita:

- Overengineering
- Complexidade desnecessária (ex: múltiplas camadas DDD completas)
- Curva de aprendizado alta para equipe

Permite:

- Entrega rápida
- Desenvolvimento direto e eficiente
- Facilidade de manutenção inicial

---

### 3. Facilidade e praticidade para a equipe de desenvolvimento

A estrutura:

- É intuitiva (baseada em MVC conhecido)
- Permite com que cada desenvolvedor trabalhe isoladamente em cada contexto
- Evita confusão com arquiteturas mais complexas (hexagonal, clean, etc.)

---

### 4. Escalabilidade evolutiva

A arquitetura permite evolução futura para:

- Arquitetura em camadas mais sofisticada
- Separação em microsserviços por agregado
- Introdução de Domain Services mais robustos
- Aplicação de Clean Architecture ou Hexagonal

Ou seja:

➡️ **Não bloqueia evolução — apenas posterga complexidade**

---

### 5. Benefícios para o negócio

- Entrega mais rápida de valor
- Redução de custo inicial
- Menor risco de atrasos
- Possibilidade de validar o produto rapidamente (MVP)

---

### 6. Trade-off consciente

A decisão reconhece que:

- Não é a arquitetura DDD mais "pura"
- Não separa completamente domínio de infraestrutura

Mas aceita isso em troca de:

✔ Simplicidade  
✔ Velocidade  
✔ Clareza  

---

## Consequências

### Consequências Positivas

- Estrutura simples e intuitiva
- Fácil manutenção inicial
- Rápido desenvolvimento
- Melhor organização por domínio
- Baixo acoplamento entre features
- Boa base para evolução futura

---

### Consequências Negativas

- Mistura parcial de camadas (não totalmente isoladas)
- Pode dificultar testes mais isolados do domínio
- Pode exigir refatoração em fases futuras
- Menor aderência a DDD completo (Domain Layer puro)

---

## Alternativas Consideradas

### 1. Arquitetura em camadas tradicional (global)

```plaintext
controller/
service/
repository/
entity/
```

**Problema:**
- Mistura todos os domínios
- Baixa coesão
- Difícil manutenção com crescimento

❌ Rejeitada por não escalar bem.

---

### 2. Clean Architecture / Hexagonal

**Prós:**
- Alta separação de responsabilidades
- Forte aderência ao DDD

**Contras:**
- Alta complexidade inicial
- Curva de aprendizado elevada
- Overengineering para MVP

❌ Rejeitada para Fase 1.

---

### 3. Microsserviços desde o início

**Prós:**
- Alta escalabilidade
- Isolamento completo de contextos

**Contras:**
- Complexidade operacional alta
- Overhead de infraestrutura
- Desnecessário para MVP

❌ Rejeitada por complexidade.

---

## Implementação

### Passos:

1. Criar estrutura de pacotes por agregado
2. Definir controllers REST para cada agregado
3. Implementar services com regras de negócio
4. Criar repositories com Spring Data JPA
5. Definir DTOs para entrada e saída
6. Implementar mappers para conversão
7. Centralizar configurações e utilitários em `shared`
8. Garantir baixo acoplamento entre agregados

---

## Referências

- Domain-Driven Design — Eric Evans  
- Clean Architecture — Robert C. Martin  
- Spring Boot Best Practices  
- Feature-Based Package Structure  
- Modular Monolith Architecture  

---

## Links úteis:

- https://martinfowler.com/bliki/BoundedContext.html  
- https://martinfowler.com/articles/microservices.html  
- https://docs.spring.io/spring-boot/docs/current/reference/html/  
- https://herbertograca.com/2017/07/03/the-software-architecture-chronicles/
