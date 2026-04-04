# Por onde começar?

## Rodando o projeto

1. É necessário ter o [Docker](https://www.docker.com/) instalado na máquina antes de prosseguir. Para confirmar que o serviço está rodando digite `docker run hello-world`, se uma mensagem aparecer com os dizeres `Hello from Docker!`, então estamos prontos.
2. Navegar até a pasta do arquivo Compose: `$ cd ./docker-local` (navegando a partir da raiz do projeto).
3. Rodar o comando `docker-compose up -d` e aguardar o download das imagens e criação dos containeres. Essa etapa garante que o banco de dados seja preenchido com valores default para agilizar possíveis testes locais. Para mais detalhes, leia as instruções em [./docker-local/db-seed/README.md](https://github.dev/Grupo-SOAT/sistema-oficina-mecanica/docker-local/db-seed/README.md).
4. Ao finalizar, a aplicação estará disponível em http://localhost:8080. Sendo possível consultar a documentação no padrão SwaggerDocs em http://localhost:8080/swagger-ui/.
