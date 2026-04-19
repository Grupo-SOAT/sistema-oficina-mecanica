# RFC002 - Chat Bot Auto Atendimento

- **Número da RFC**: 0002  
- **Data**: 18 de abril de 2026  
- **Autor**: Andre Lui  
- **Status**: **Aceita**

---

## Resumo

Esta RFC propõe a implementação de um chatbot de autoatendimento como alternativa ao atendimento humano no sistema de gerenciamento da oficina mecânica. O objetivo é oferecer aos clientes a possibilidade de realizar operações de forma autônoma, aumentando a eficiência operacional, reduzindo erros humanos e melhorando a experiência do usuário.

---

## Contexto

Atualmente, o sistema prevê a atuação de um atendente humano responsável por intermediar diversas operações entre o cliente e a oficina. Essas operações incluem:

- Cadastro de clientes e veículos  
- Envio de orçamentos  
- Atualização do status de orçamentos (aprovado, rejeitado, parcialmente aprovado)  
- Criação de ordens de serviço  

Embora o atendimento humano seja eficaz, ele apresenta limitações:

- Possibilidade de erros operacionais  
- Dependência de disponibilidade humana  
- Tempo de resposta variável  
- Custo operacional contínuo  

Além disso, há uma crescente demanda por soluções de autoatendimento que ofereçam maior rapidez, autonomia e conveniência ao cliente.

---

## Proposta

Implementar um chatbot de autoatendimento integrado ao sistema da oficina, permitindo que o cliente realize, de forma autônoma, as mesmas operações atualmente executadas pelo atendente humano.

### Funcionalidades do chatbot:

- Cadastro de clientes  
- Cadastro de veículos  
- Consulta de orçamentos pendentes  
- Envio de orçamentos ao cliente  
- Recebimento de resposta do cliente sobre orçamentos:
  - Aprovação total  
  - Rejeição total  
  - Rejeição parcial 
- Atualização automática do status dos orçamentos  
- Abertura de ordem de serviço  

### Características da solução:

- Atendimento 24/7  
- Integração com o backend (Spring Boot)  
- Execução de regras de negócio já existentes (reuso dos Application Services)  
- Possibilidade de fallback para atendimento humano  

---

## Justificativa

### 1. Eficiência Operacional

O chatbot permite automatizar tarefas repetitivas e operacionais, reduzindo:

- Tempo de atendimento  
- Carga de trabalho humano  
- Gargalos no fluxo de atendimento  

Isso melhora significativamente a produtividade da oficina.

---

### 2. Redução de Erros Humanos

Processos automatizados garantem:

- Execução consistente das regras de negócio  
- Redução de falhas manuais  
- Maior confiabilidade nos dados  

---

### 3. Melhoria na Experiência do Cliente

O autoatendimento oferece:

- Respostas imediatas  
- Disponibilidade contínua (24/7)  
- Maior autonomia para o cliente  

Isso resulta em maior satisfação e fidelização.

---

### 4. Redução de Custos

Com a automação:

- Reduz-se a necessidade de múltiplos atendentes  
- Diminui-se o custo operacional  
- Permite escalar o atendimento sem aumento proporcional de custos  

---

### 5. Escalabilidade

O chatbot permite atender múltiplos clientes simultaneamente sem degradação significativa de desempenho, ao contrário do atendimento humano.

---

### 6. Alinhamento com Boas Práticas Arquiteturais

A solução:

- Reutiliza serviços de aplicação já existentes (DDD)  
- Mantém separação de responsabilidades  
- Facilita evolução futura (ex: integração com IA mais avançada)
- Define o backend como Source of Truth para execução de operações para o negócio

![Diagrama c4 model - Solução Chat Bot](../../diagrams/c4-model/c1-context.png)

---

## Impactos

### Impacto na Arquitetura

- Introdução de uma nova camada de interação (chatbot)  
- Integração com os Application Services existentes  
- Possível uso de mensageria para processamento assíncrono  
- Necessidade de controle de contexto conversacional  

---

### Impacto nos Processos

- Redução da dependência de atendimento humano  
- Mudança no fluxo de atendimento (cliente → chatbot → sistema)  
- Possibilidade de fallback para atendente humano em casos complexos  

---

### Impacto nos Recursos

- Desenvolvimento ou integração de plataforma de chatbot  
- Infraestrutura adicional para suportar o serviço  
- Monitoramento e treinamento contínuo (caso utilize IA)  

---

## Alternativas Consideradas

### 1. Manter apenas atendimento humano

**Prós:**
- Interação mais personalizada  
- Facilidade para lidar com exceções  

**Contras:**
- Baixa escalabilidade  
- Maior custo operacional  
- Maior propensão a erros  

❌ Rejeitado por limitar eficiência e escalabilidade.

---

### 2. Implementar apenas automações internas (sem chatbot)

**Prós:**
- Redução parcial de erros  
- Melhoria de processos internos  

**Contras:**
- Não resolve o problema de experiência do cliente  
- Mantém dependência de atendimento humano  

❌ Rejeitado por não atender ao objetivo de autoatendimento.

---

### 3. Chatbot apenas informativo (sem ações)

**Prós:**
- Simples de implementar  
- Baixo risco  

**Contras:**
- Não agrega valor operacional significativo  
- Não automatiza processos críticos  

❌ Rejeitado por baixo impacto no negócio.

---

## Implementação

### Passos:

1. Definir plataforma do chatbot (ex: web, WhatsApp, etc.)  
2. Criar camada de integração com backend (API REST)  
3. Mapear fluxos de conversa baseados nos casos de uso  
4. Integrar chatbot aos Application Services existentes  
5. Implementar validações e regras de negócio  
6. Configurar fallback para atendimento humano  
7. Testar fluxos com usuários reais  
8. Monitorar métricas e ajustar continuamente  

---

## Referências

- Domain-Driven Design (Eric Evans)  
- Arquitetura Orientada a Serviços  
- Práticas de UX para Chatbots  
- Estratégias de Automação de Atendimento  
- Plataforma para criação de chat bots pré scriptados: https://typebot.com/