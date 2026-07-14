# Fase 1

# sistema-oficina-mecanica

Repositório para armazenar o código fonte do Tech Challenge - POS TECH Software Architecture - FIAP

## Descrição

Um sistema planejado para facilitar a criação e acompanhamento de Ordens de Serviço e o Controle de Estoque de uma Oficina Mecânica.

## Tecnologias

- [Java 21](https://www.oracle.com/java/technologies/javase/21-relnote-issues.html)
- [Maven 3.9.9](https://maven.apache.org/docs/3.9.9/release-notes.html)
- [Spring Framework 7.0](https://github.com/spring-projects/spring-framework/wiki/Spring-Framework-7.0-Release-Notes)

## Infraestrutura

- [Docker](https://www.docker.com/)
- [PostgreSQL](https://www.postgresql.org/)

## Documentação

- [Por onde começar?](./docs/pages/get-started.md)
- [Contato](./docs/pages/contact.md)
- [Event Storming](./docs/pages/event-storming.md)
- [Domain Storytelling](./docs/pages/domain-storytelling.md)
- [Linguagem Ubíqua](./docs/pages/ubiquitous-language.md)
- [C4 Model](./docs/pages/c4-model.md)
- [ADRs](./docs/pages/adrs)
- [RFCs](./docs/pages/rfcs)

# Fase 2

Vídeo: https://youtu.be/LGhzrMXHJs4

## Contexto

Na Fase 2 do projeto do sistema da oficina mecânica, foi solicitada a aplicação e evolução de práticas de engenharia e desenvolvimento para garantir a maior qualidade, resiliência e escalabilidade do sistema. Portanto, esta fase foi focada principalmente em provisionamento de infraestrutura, automação, melhorias técnicas do código e implementação de novas funcionalidades e recursos.

## Solução implementada

1. Práticas de automação e engenharia de plataforma (infraestrutura):

    1.1 - Arquitetura do Projeto:

    - Estabelecemos um modelo de infraestrutura local baseada em Minikube (Kubernetes Local) com orquestração IaC via Terraform.

    - Documentação: [Diagrama de Infraestrutura](./docs/infra/v2/README.md)

    1.2 - Manifestos Kubernetes e Scripts Terraform:

    - Nas pastas [/k8s](./k8s/) e [/infra](./infra/) encontram-se todos os manifestos e scripts utilizados para provisionar e manipular a infraestrutura.

    1.3 - CI/CD:
    
    - Adotamos um modelo de automação que provisiona a infraestrutura, habilita o deploy das aplicações e destrói todos os recursos, proporcionando um ambiente que pode ser totalmente controlado via esteiras, tendo como ferramenta centralizadora o Terraform.

    - Documentação: [Diagrama de CI/CD](./docs/pipeline/README.md)


2. Implementação de novas funcionalidades, integração de APIs e serviços externos:

    - Consulta e Controle de Status da OS
    - Geração de Orçamentos Automático com Base no Serviço
    - Criação de microsserviço de orçamentos
    - Comunicação entre Monolito e Microsserviço de orçamentos via Kafka (mensageria)
    - Atualização de Status da OS via email
    - Aprovação / Reprovação de Orçamento via email
    - Mailpit como simulador de email

    Diagrama de implementação: [Feature Orçamentos/Email](./docs/pages/diagrams/feature-email/diagrama-feature-email.png)

3. Adoção da Arquitetura Hexagonal no código.

    - Documentação: [ADR - Arquitetura Hexagonal](./docs/pages/adrs/ADR001_arquitetura_codigo_java/README.md)


## Instruções para rodar o Cluster Localmente

1. Pré-Requisitos: 

    - Minikube - Instale de acordo com seu sistema operacional: https://minikube.sigs.k8s.io/docs/start/?arch=%2Fwindows%2Fx86-64

    - kubectl - Baixe o binário aqui: https://kubernetes.io/pt-br/docs/tasks/tools/

    - Docker - Se estiver no windows, baixe o Docker Desktop: https://docs.docker.com/desktop/setup/install/windows-install/

    - Terraform - https://developer.hashicorp.com/terraform

2. Clone o Repositório:

    ```bash
    git clone https://github.com/Grupo-SOAT/sistema-oficina-mecanica
    ```

3. Troque as secrets como desejar, acessando o arquivo [k8s/secret.yaml](./k8s/secret.yaml) (Substitua os Placeholders pelos valores que preferir).

4. Para utilizar o seed local e gerar dados no banco automaticamente, adicione isto no seu manifesto [deployment-monolito.yaml](./k8s/deployment-monolito.yaml):

    (Atenção à identação):

    ```yaml
    env:    
        - name: SPRING_PROFILES_ACTIVE
        value: "local"
    ```

5. Se estiver no windows, abra o Docker Desktop.

6. Estando dentro da pasta de [/infra](./infra/), execute os comandos do terraform, na sequência:

    ```bash
    terraform init
    terraform validate
    terraform plan
    terraform apply
    ```

    Com isso, toda a infraestrutura será provisionada - incluindo o banco de dados, kafka, o monolito e o microsserviço.

7. Para expor o cluster e acessar o monolito do Sistema Oficina Mecânica, execute:

    Primeira Opção (Via **Ingress**):
    ```bash
    kubectl port-forward -n ingress-nginx service/ingress-nginx-controller 8080:80
    ```

    Segunda Opção (**Serviço Exposto Diretamente**):
    ```bash
    kubectl port-forward svc/workshop-backend-service -n oficina-mecanica 8080:80
    ```

8. Teste o acesso ao backend pelo Swagger, Postman ou outra ferramenta que preferir:

    - Swagger: http://localhost:8080/swagger-ui/index.html#/

    Os demais endpoints também estarão rodando em **localhost:8080**.

9. Para destruir toda a infraestutura, estando dentro da pasta [/infra](./infra/), basta rodar:

    ```bash
    terraform destroy
    ```

    Isso destruirá todos os componentes do cluster e inclusive o próprio cluster, resetando o seu tfstate.
