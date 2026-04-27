# language: pt
@serviceOrders
Funcionalidade: Gerenciamento de Ordens de Serviço
  Como um administrador da oficina
  Quero gerenciar ordens de serviço e seus fluxos
  Para controlar o atendimento do veículo e o orçamento do cliente

  # === LISTAGEM / PAGINAÇÃO / BUSCA ===

  Cenário: Listagem de ordens de serviço
    Dado que eu esteja devidamente logado
    E que o tamanho da pagina seja 10
    Quando eu listar as ordens de serviço
    Então devo receber uma resposta com status "200"
    E a resposta deve conter uma lista de dados
    E a resposta deve conter o campo "pageSize"
    E a resposta deve conter o campo "cursor"
    E a resposta deve conter o campo "isLast"

  Cenário: Busca de ordens de serviço por status
    Dado que eu esteja devidamente logado
    E que o filtro status seja "PENDING"
    E que o tamanho da pagina seja 10
    Quando eu listar as ordens de serviço
    Então devo receber uma resposta com status "200"
    E a resposta deve conter no maximo 1 ordens de serviço
    E a resposta deve conter ao menos uma ordem de serviço com status "PENDING"

  Cenário: Paginação sem resultados deve retornar no content
    Dado que eu esteja devidamente logado
    E que o filtro clientDocument seja "000.000.000-00"
    E que o tamanho da pagina seja 10
    Quando eu listar as ordens de serviço
    Então devo receber uma resposta com status "204"

  Cenário: Paginação de ordens de serviço com cursor
    Dado que eu esteja devidamente logado
    E que o cursor seja "1"
    E que o tamanho da pagina seja 10
    Quando eu listar as ordens de serviço
    Então devo receber uma resposta com status "200"
    E o primeiro item retornado deve ter id maior que 1

  # === DETALHAMENTO POR ID ===

  Cenário: Detalhamento de ordem de serviço por ID existente
    Dado que eu esteja devidamente logado
    E que o id da ordem de serviço seja 1
    Quando eu consultar a ordem de serviço por id
    Então devo receber uma resposta com status "200"
    E a resposta deve conter o campo "id"
    E a resposta deve conter o campo "clientId"
    E a resposta deve conter o campo "vehicleId"
    E a resposta deve conter o campo "description"
    E a resposta deve conter o campo "status"
    E a resposta deve conter o campo "estimatedAmount"

  Cenário: Detalhamento de ordem de serviço por ID inexistente
    Dado que eu esteja devidamente logado
    E que o id da ordem de serviço seja 99999
    Quando eu consultar a ordem de serviço por id
    Então devo receber uma resposta com status "404"
    E a resposta deve conter o campo reason com valor "SERVICE_ORDER_NOT_FOUND"

  # === CADASTRO ===

  Cenário: Cadastro de ordem de serviço com dados válidos
    Dado que eu esteja devidamente logado
    E que o corpo da nova ordem de serviço seja:
      | clientId | vehicleId | description            |
      | 1        | 1         | Troca de óleo e filtro |
    Quando eu criar a ordem de serviço
    Então devo receber uma resposta com status "201"
    E a resposta deve conter o campo "id"
    E a resposta deve conter o campo "status"
    E a resposta deve refletir o payload enviado

  Esquema do Cenário: Cadastro de ordem de serviço com ids inexistentes
    Dado que eu esteja devidamente logado
    E que o corpo da nova ordem de serviço seja:
      | clientId   | vehicleId   | description   |
      | <clientId> | <vehicleId> | <description> |
    Quando eu criar a ordem de serviço
    Então devo receber uma resposta com status "400"
    E a resposta deve conter o campo reason com valor "<reason>"
    Exemplos:
      | clientId | vehicleId | description            | reason                          |
      | 99999    | 1         | Troca de óleo e filtro | SERVICE_ORDER_CLIENT_NOT_FOUND  |
      | 1        | 99999     | Troca de óleo e filtro | SERVICE_ORDER_VEHICLE_NOT_FOUND |

  # === ATUALIZAÇÃO ===

  Cenário: Atualização de ordem de serviço existente
    Dado que eu esteja devidamente logado
    E que o id da ordem de serviço seja 1
    E que o corpo de atualização da ordem de serviço seja:
      | id | clientId | vehicleId | description                     | status   |
      | 1  | 1        | 1         | Troca de óleo, filtro e revisão | APPROVED |
    Quando eu atualizar a ordem de serviço
    Então devo receber uma resposta com status "200"
    E a resposta deve refletir o payload enviado

  Esquema do Cenário: Atualização de ordem de serviço com payload inválido
    Dado que eu esteja devidamente logado
    E que o id da ordem de serviço seja 1
    E que o corpo de atualização da ordem de serviço seja:
      | id   | clientId   | vehicleId   | description   | status   |
      | <id> | <clientId> | <vehicleId> | <description> | <status> |
    Quando eu atualizar a ordem de serviço
    Então devo receber uma resposta com status "400"
    E a resposta deve conter o campo reason com valor "<reason>"
    Exemplos:
      | id | clientId | vehicleId | description            | reason                          |
      | 1  | 99999    | 1         | Troca de óleo e filtro | SERVICE_ORDER_CLIENT_NOT_FOUND  |
      | 1  | 1        | 99999     | Troca de óleo e filtro | SERVICE_ORDER_VEHICLE_NOT_FOUND |

  Cenário: Atualização de ordem de serviço inexistente
    Dado que eu esteja devidamente logado
    E que o id da ordem de serviço seja 99999
    E que o corpo de atualização da ordem de serviço seja:
      | id    | clientId | vehicleId | description    | status   |
      | 99999 | 1        | 1         | Nao encontrada | APPROVED |
    Quando eu atualizar a ordem de serviço
    Então devo receber uma resposta com status "404"
    E a resposta deve conter o campo reason com valor "SERVICE_ORDER_NOT_FOUND"

  # === EXCLUSÃO ===

  Cenário: Exclusão de ordem de serviço existente
    Dado que eu esteja devidamente logado
    E que o id da ordem de serviço seja 1
    Quando eu remover a ordem de serviço
    Então devo receber uma resposta com status "202"

  Cenário: Exclusão de ordem de serviço inexistente
    Dado que eu esteja devidamente logado
    E que o id da ordem de serviço seja 99999
    Quando eu remover a ordem de serviço
    Então devo receber uma resposta com status "404"
    E a resposta deve conter o campo reason com valor "SERVICE_ORDER_NOT_FOUND"

  # === PROGRESSO DA ORDEM DE SERVIÇO ===

  Esquema do Cenário: Registrar ações de progresso da ordem de serviço
    Dado que eu esteja devidamente logado
    E que o id da ordem de serviço seja 1
    E que o corpo da ação de progresso da ordem de serviço seja:
      | action   | additionalInfo   | relatedServiceId   |
      | <action> | <additionalInfo> | <relatedServiceId> |
    Quando eu registrar o progresso da ordem de serviço
    Então devo receber uma resposta com status "202"
    Exemplos:
      | action              | additionalInfo          | relatedServiceId |
      | START_INSPECTION    | Recebida na recepcao    |                  |
      | COMPLETE_INSPECTION | Inspecao finalizada     |                  |
      | START_SERVICE       | Inicio da troca de oleo | 1                |
      | COMPLETE_SERVICE    | Serviço concluido       | 1                |
      | CANCEL_SERVICE      | Serviço cancelado       | 1                |
      | DELIVER_VEHICLE     | Veiculo entregue        |                  |

  Cenário: Progresso em ordem de serviço inexistente
    Dado que eu esteja devidamente logado
    E que o id da ordem de serviço seja 99999
    E que o corpo da ação de progresso da ordem de serviço seja:
      | action           | additionalInfo       | relatedServiceId |
      | START_INSPECTION | Recebida na recepcao |                  |
    Quando eu registrar o progresso da ordem de serviço
    Então devo receber uma resposta com status "404"
    E a resposta deve conter o campo reason com valor "SERVICE_ORDER_NOT_FOUND"

  # === ORCAMENTO DA ORDEM DE SERVIÇO ===

  Esquema do Cenário: Registrar decisão do cliente sobre o orçamento
    Dado que o orçamento da ordem de serviço de ID "1" foi enviado ao cliente
    E que a decisão do cliente seja "<decision>"
    E que a observação da decisão seja "<comment>"
    E que os serviços rejeitados sejam "<rejectedServiceIds>"
    Quando eu registrar a decisão do cliente sobre o orçamento da ordem de serviço
    Então devo receber uma resposta com status "202"
    Exemplos:
      | decision         | comment                       | rejectedServiceIds |
      | APPROVE          | Orçamento aprovado            |                    |
      | CANCEL           | Cliente desistiu              |                    |
      | REJECT           | Cliente recusou integralmente |                    |
      | PARTIALLY_REJECT | Cliente aceitou apenas parte  | 1                  |

  Cenário: Decisão de orçamento para ordem de serviço inexistente
    Dado que a decisão do cliente seja "APPROVE"
    E que a observação da decisão seja "Aprovado"
    E que os serviços rejeitados sejam ""
    E que o orçamento da ordem de serviço de ID "99999" foi enviado ao cliente
    Quando eu registrar a decisão do cliente sobre o orçamento da ordem de serviço
    Então devo receber uma resposta com status "404"
    E a resposta deve conter o campo reason com valor "SERVICE_ORDER_NOT_FOUND"
