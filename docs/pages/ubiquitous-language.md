# Dicionário Ubíquo

## Atores

- Admin
  - Administrador da oficina mêcanica/Sistema.
- Atendente Humano
  - Pessoa responsável por cadastrar clientes e veículos, enviar orçamentos para o cliente e atualizar o Status da OS para aprovado ou reprovado de acordo com a resposta do cliente.
- Atendente Virtual
  - Chatbot tem as mesmas responsabilidades do atendente humano, porem tem a responsabilidade adicional de auxiliar o cliente para ser atendido por auto atendimento.
- Mecanico
  - Pessoa responsável por fazer a triagem e execução de serviços no veiculo do cliente.
- Cliente
  - Pesssoa que veio até a oficina com o objetivo de utilizar serviços da oficina mêcanica. Deve necessariamente ter pelo menos um veiculo cadastrado.
- Almoxarife
  - Pessoa responsável por cuidar do controle de estoque do almoxarifado (como saidas, entradas e encomendas de insumos).

## Termos Técnicos

- OS (Ordem de Serviço)
  - Uma Ordem de Serviço é um pedido técnico que contem varios serviços que serão feitos no veículo.
- Serviço
  - É uma ação (como trocar insumos, fazer manutenção, etc.) que sera feito pelo mecanico no veículo do cliente.
- Insumos
  - Qualquer equipamento que vai ser utilizado na execução dos serviços, por exemplo, peças que serão substituidas no veículo, utilização de produtos, etc.
- SKU (Stock Keeping Unit)
  - É um código alfanumérico exclusivo criado internamente para identificar produtos.
- Triagem
  - Inspeção geral feita pelo mecânico no inicio do fluxo para poder ser gerado um orçamento com base nos serviços/insumos que o mecânico julgar necessarios para o veiculo.
- Orçamento
  - Valor gerado a partir dos serviços/insumos que serão realizados/feitos no veiculo que o cliente devera pagar caso deseje prosseguir com os serviços decobertos na triagem.
- Almoxarifado
  - Local aonde ficam armazenados os insumos.
- Reparo Adicional
  - Caso seja identificado algun serviço/insumo que será necessario para o veiculo após a triagem inicial, esse serviço/insumo pode ser cadastrado como um reparo adicional na OS, que volta para o status de aguardando aprovação nesse caso.
- Fornecedor de Insumos
  - Empresa que fornece algum tipo de insumo para oficina.
- Pedido de Compra de Insumos
  - Um pedido de encomenda feito para um fornecedor de insumos ativo.

## Status

### OS (Ordem de Serviço)

- Recebida
  - Status que indica que uma OS foi criada por um atendente. Precisa necessariamente ter um veiculo vinculado com a OS.
- Em Diagnóstico
  - Status que indica que o veiculo vinculado com a OS esta passando por triagem do mecanico.
- Aguardando Aprovação
  - Status que indica que a triagem foi feita e os serviços/insumos que o mecanico julgou serem necessarios para o veiculo foram adicionados na OS e que orçamento foi gerado e enviado ao cliente e está esperando uma ser aprovada ou rejeitada.
- Parcialmente Rejeitada
  - Status que indica que um ou mais serviços/insumos da OS foram rejeitados pelo cliente, porem não todos os serviços da OS.
- Aprovada
  - Status que indica que todos os serviços/insumos da OS foram aprovados pelo cliente e a OS pode seguir o fluxo.
- Cancelada
  - Status que indica que todos os serviços/insumos da OS foram rejeitados pelo cliente e a OS é encerrada sem a execução dos serviços.
- Em Andamento
  - Status que indica que os serviços da OS estão sendo executados no veiculo cadastrado na OS.
- Finalizada
  - Status que indica que todos os serviços da OS foram executados no veiculo cadastrado na OS.
- Entregue
  - Status que indica que o veiculo após terem sido feitos os serviços foi entregue de volta para o cliente.

### Orçamento

- Enviado
  - Status que indica que o Orçamento foi enviado para o cliente.
- Aprovada
  - Status que indica que o orçamento foi aprovado pelo cliente e a OS pode prosseguir com seu fluxo.
- Parcialmente Rejeitada
  - Status que indica que um ou mais serviços/insumos da OS foram rejeitados pelo cliente no Orçamento, porem não todos os serviços/insumos da OS. Nesses casos a OS volta para ser feita novamente a triagem.
- Rejeitada
  - Status que indica que todos os serviços/insumos da OS foram rejeitados pelo cliente.

### Serviço

- Aprovado
  - Status que indica  que o serviço foi aprovado para ser executado no veiculo.
- Em Andamento
  - Status que indica  que o serviço esta Em Andamento no veiculo.
- Finalizado
  - Status que indica que o a execução do serviço foi finalizada.
- Rejeitado
  - Status que indica que o serviço foi rejeitado pelo cliente, porem existem outros serviços da OS que o cliente quer que sejam executados.
- Cancelado
  - Status que indica o serviço não sera executado porque o orçamento foi rejeitado pelo cliente.

### Fornecerdor

- Ativo
  - Status que indica que o fornecedor pode ser contatado para encomendar insumos
- Inativo
  - Status que indica que o fornecedor não pode ser contatado para encomendar insumos

### Pedido de Compra

- Pendente
  - Status que indica que o Pedido de Compra foi criado e enviado para o fornecedor.
- Confirmado
  - Status que indica que o Pedido de Compra foi confirmado pelo fornecedor.
- Cancelado pelo Fornecedor
  - Status que indica que o Pedido de Compra foi rejeitado pelo fornecedor.
- Despachado
  - Status que indica que o Pedido de Compra foi separado e os produtos estão a caminho da oficina.
- Cancelado pelo Almoxarife
  - Status que indica que o Pedido de Compra foi cancelado pelo almoxarife pelo sistema antes de ter sido despachado ou concluido.
- Concluído
  - Status que indica que o Pedido de Compra foi recebido na oficina.

### Insumo

- Disponivel
  - Esse status indica que o insumo esta disponivel para uso no almoxarifado e pode ser reservado por uma OS.
- Encomendado
  - Esse status indica que o insumo foi encomendado para um fornecedor de inusmos.
- Reservado
  - Esse status indica que o insumo esta reservado para uma OS no sistema de Almoxarifado, porem ele ainda não foi utilizado na execução do serviço, ele apenas não sera listado como disponivel no momento de selecionar o insumo no registro da OS.
- Separado
  - Esse status indica que o insumo foi retirado do almoxarifado para ser utilizado na execução do serviço.
