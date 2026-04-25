# language: pt
@reporting
Funcionalidade: Exportação de Relatórios
  Como um usuário da oficina
  Quero consultar relatórios do sistema
  Para acompanhar indicadores e documentos operacionais

	# === RELATÓRIO DE TEMPO MÉDIO DE SERVIÇO ===

  Esquema do Cenário: Consulta de tempo médio de execução por serviço
    Dado que eu esteja devidamente logado
    Quando acesso o endpoint "GET" "/reports/services/:id/average-time"
    Então devo receber uma resposta com status "<status>"
    Exemplos:
      | status |
      | 200    |

	# === RELATÓRIO DE ORÇAMENTO DA OS ===

  Esquema do Cenário: Consulta de orçamento de ordem de serviço
    Dado que eu esteja devidamente logado
    Quando acesso o endpoint "GET" "/reports/service-orders/:id/budget"
    Então devo receber uma resposta com status "<status>"
    Exemplos:
      | status |
      | 200    |

	# === RELATÓRIO DE PEDIDO DE COMPRA ===

  Esquema do Cenário: Consulta de pedido de compra
    Dado que eu esteja devidamente logado
    Quando acesso o endpoint "GET" "/reports/purchase-order/:id"
    Então devo receber uma resposta com status "<status>"
    Exemplos:
      | status |
      | 200    |
