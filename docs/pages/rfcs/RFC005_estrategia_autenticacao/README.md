# Estrategia de Autenticação no Sistema Oficina Mecanica

- **Número da RFC**: 0005
- **Data**: 30 de agosto de 2026
- **Autores**: Andre Lui
- **Status**: Aceita

## Resumo

Esta RFC irá propor a estratégia de autenticação para a Fase 3 no projeto da oficina mecanica.

## Contexto

Durante as fases anteriores, consolidamos o sistema da oficina mecânica com a estratégia de autenticação via Token JWT em endpoints de Login, também mapeamos e definimos ROLES, as quais algumas possuem mais acessos no sistema, e outras não. A lógica e validação fica totalmente centralizada no Monolito da oficina mecânica, utilizando como recurso de apoio o banco de dados Postgres SQL. Na fase 3, entretanto, surgiu a necessidade de expandir a estratégia, adicionando mais uma camada de autenticação para tornar o sistema mais robusto e seguro.

## Proposta

Implementar, juntamente com a autenticação JWT, uma verificação de CPF do cliente via Lambda. A verificação deverá ser feita antes mesmo do cliente poder acessar o sistema monolito (endpoints), portanto, toda requisição será feita se, e apenas se, o CPF estiver válido e ativo no sistema. A consulta será feita feita a partir da lambda acessando diretamente o banco de dados e verificando os dados de CPF, e após isso, redireciona o tráfego para o monolito, criando uma camada extra de proteção, e mantendo a autenticação previamente feita com JWT. Na prática, a estratégia de autenticação desta Fase é uma evolução do que já fizemos anteriormente, agora no contexto de cloud.

## Justificativa

Optamos por esta solução por conta principalmente das exigências e orientações desta Fase, as quais descrevem que devemos realizar a validação de CPF via lambda + autenticação por JWT. Analisamos o cenário e concluímos que, por já termos implementado a autenticação por JWT no próprio monolito anteriormente, valeria mais a pena manter essa lógica a nível do sistema monolito, ao invés de delegar essa responsabilidade extra para a lambda. Portanto, nosso cenário é de uma lambda que possui uma função definida e menos complexa, o que reduz acoplamento e reaproveita o que já está feito e consistente no sistema.

## Impactos

### Impacto na Arquitetura e Recursos

Arquiteturalmente falando, há impactos de relativa complexidade, uma vez que o sistema inteiro nas fases anteriores estava rodando em uma infraestrutura local com Minikube, e agora vamos migrar tudo para a AWS. No contexto de autenticação, há impactos no escopo de fluxo de requisição e banco de dados, além do recurso extra da lambda e e lógica acoplada dentro dela.

## Alternativas Consideradas

Consideramos gerar o JWT pela própria lambda, mas descartamos essa ideia por conta da alta complexidade de refatoração e remapeamento dos endpoints do monolito. Temos muitos recursos que podem ser acessados e o monolito já tem a lógica consistente e funcional para lidar com o JWT. A decisão final, portanto, foi seguir com o plano da lambda fazendo a verificação por CPF + o monolito lidando com o login e gerenciando os tokens JWT.

## Implementação

Criar e desenvolver a lambda para validar o CPF e servir de "proxy" + provisioná-la via terraform, integrá-la com o aws gateway + rds postgres no sistema, testando e integrando-a também com o load balancer presente no EKS. 

## Referências

- https://aws.amazon.com/pt/lambda/
- [Diagrama de Componentes - Fase 3](../../../infra/Fase3/Diagrama-Componentes-Fase3-AWS.png)
- https://docs.aws.amazon.com/pt_br/lambda/latest/dg/welcome.html
- https://docs.spring.io/spring-security/reference/servlet/authentication/architecture.html
