# language: pt
@reporting
Funcionalidade: Exportação de Relatórios
  Como um usuário da oficina
  Quero consultar relatórios do sistema
  Para acompanhar indicadores e documentos operacionais

	# === RELATÓRIO DE TEMPO MÉDIO DE SERVIÇO ===

  Cenário: Consulta de tempo médio de execução por serviço
    Dado que eu esteja devidamente logado
    Quando acesso o endpoint "GET" "/reports/catalog/services/:id/average-time"
    Então devo receber uma resposta com status "200"
    E o content-type da resposta deve ser "application/pdf"
    E o body da resposta não deve ser vazio
    E o nome do arquivo deve indicar exportação recente

  Cenário: Consulta de tempo médio de execução de todos os serviços em CSV
    Dado que eu esteja devidamente logado
    Quando acesso o endpoint "GET" "/reports/catalog/services/average-time"
    Então devo receber uma resposta com status "200"
    E o content-type da resposta deve ser "text/csv"
    E o body da resposta não deve ser vazio
    E o nome do arquivo deve indicar exportação recente

#	# === RELATÓRIO DE ORÇAMENTO DA OS ===
#
#  Cenário: Consulta de orçamento de ordem de serviço
#    Dado que eu esteja devidamente logado
#    Quando acesso o endpoint "GET" "/reports/service-orders/:id/budget"
#    Então devo receber uma resposta com status "200"
#    E o content-type da resposta deve ser "application/pdf"
#    E o body da resposta não deve ser vazio
#    E o nome do arquivo deve indicar exportação recente
#
#	# === RELATÓRIO DE PEDIDO DE COMPRA ===
#
#  Cenário: Consulta de pedido de compra
#    Dado que eu esteja devidamente logado
#    Quando acesso o endpoint "GET" "/reports/purchase-order/:id"
#    Então devo receber uma resposta com status "200"
#    E o content-type da resposta deve ser "application/pdf"
#    E o body da resposta não deve ser vazio
#    E o nome do arquivo deve indicar exportação recente

