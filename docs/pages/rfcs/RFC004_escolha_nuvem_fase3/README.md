# Escolha da nuvem - Fase 3 - AWS

- **Número da RFC**: 0004
- **Data**: 30 de agosto de 2026
- **Autores**: Andre Lui
- **Status**: Aceita

## Resumo

O objetivo desta RFC é propor e justificar a escolha da nuvem AWS na Fase 3 do projeto da oficina mecanica, espera-se um impacto direto na infraestrutura de todo o sistema e em todos os componentes.

## Contexto

Na fase anterior do projeto, utilizamos um modelo de infraestrutura "Local" com minikube integrado, onde todos os manifestos do kubernetes eram aplicados localmente, utilizando como referencia um diretório específico do mono repo "sistema-oficina-mecanica". Para essa Fase 3, é proposto que, além do incremento de observabilidade que a fase 2 não tinha, exige-se uma migração de Infraestrutura, onde o dever do grupo é escolher uma nuvem específica para realizar a migração de todo o projeto na Fase 3 da Pós Tech - FIAP.

## Proposta

Implementar a Migração do projeto do sistema da oficina mecânica na nuvem AWS, de modo que todos os recursos e componentes sejam provisionados na AWS - academy lab, utilizando terraform.

## Justificativa

Escolhemos a AWS por ela ser uma nuvem extremamente popular no mercado, e também amigável para aprender, uma vez que o ambiente do laboratório possibilita a simplificação do uso da CLI da AWS e os custos são altamente previsíveis e mínimo. Consideramos também na decisão o fato de que a FIAP nos proporcionou algumas licenças de estudante, o que nos permitiu trabalhar com a AWS de uma maneira mais intuitiva e barata.

## Impactos

Espera-se uma evolução massiva do projeto, uma vez que a infraestrutura migrará de local para a nuvem, precisamos reformular boa parte do nosso código Terraform e executar testes contínuos para atingirmos um resultado consistente na AWS. Para isso, é necessário criar diversos módulos, testar as integrações, migrar as pipelines e validar o funcionamento correto e otimizado da infraestrutura no nosso ambiente do laboratório AWS. 

### Impacto na Arquitetura

Há impactos gigantescos na arquitetura, pois além de migrarmos para a cloud, também precisamos seguir orientações e novos objetivos na Fase 3, onde precisaremos trabalhar com Gateways nativos da cloud, códigos "Serverless" (lambda) e provisionar load balancers nativos, além de provisionar o bando de dados Postgres via RDS. É um impacto grande, mas que permite com que os nossos resultados sejam os mesmos que obtivemos localmente, com a adição de que evoluiremos os componentes e garantiremos que as novas demandas da Fase 3 sejam desenvolvidas e entregues com sucesso.

### Impacto nos Recursos

Falando dos recursos adicionais, obviamente há um custo maior de realizarmos a migração para a cloud, mas para o nosso contexto, é altamente benéfico para o aprendizado, já que somos custeados e apoiados pela Instituição de ensino que estamos integrados ao realizarmos esse projeto. Estamos devidamente habilitados.

## Alternativas Consideradas

A única alternativa considerada foi utilizar a nuvem da Microsoft (Azure), pois ela também é utilizada amplamente no mercado. Nossa decisão de não utilizar Azure, foi tomada basicamente pelo fator "custo" e praticidade, uma vez que teríamos que provisionar um ambiente de aprendizado no Azure de modo independente da instituição de ensino, o que não valeria a pena para o grupo como um todo.

## Implementação

Plano proposto: migrar código terraform, segregar repositórios, migrar pipelines de infraestrutura e deploy, criar novas ADRs e RFCs, revisar e testar as integrações, obter resultados de observabilidade.

## Referências

- https://aws.amazon.com/pt/training/awsacademy/
- [Diagrama de Componentes - Fase 3](../../../infra/Fase3/Diagrama-Componentes-Fase3-AWS.png)
- https://docs.aws.amazon.com/pt_br/whitepapers/latest/web-application-hosting-best-practices/key-components-of-an-aws-web-hosting-architecture.html
- https://www.studocu.com/pt-br/document/faculdade-santa-maria/algoritmo-e-programacao/aws-academy-learner-lab-student-guide/42420206
