# API Documentation - Sistema de Venda de Salgados

## Endpoints REST

### 1. POST /clientes/login

Realiza login de cliente no sistema.

**Request:**
```json
{
  "email": "joao@email.com",
  "senha": "123456"
}
```

**Response (Success - 200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "email": "joao@email.com",
  "mensagem": "Login realizado com sucesso"
}
```

**Response (Error - 401 UNAUTHORIZED):**
```json
{
  "id": null,
  "nome": null,
  "email": null,
  "token": null,
  "mensagem": "Email não encontrado"
}
```

**Response (Error - 401 UNAUTHORIZED):**
```json
{
  "id": null,
  "nome": null,
  "email": null,
  "token": null,
  "mensagem": "Senha incorreta"
}
```

---

### 2. POST /pedidos

Cria um novo pedido no sistema.

**Request:**
```json
{
  "clienteId": 1,
  "itens": [
    {
      "salgadoId": 1,
      "sabor": "Frango",
      "quantidade": 10,
      "valorUnitario": 5.00
    },
    {
      "salgadoId": 2,
      "sabor": "Queijo",
      "quantidade": 15,
      "valorUnitario": 4.50
    }
  ],
  "tipoPreco": "PADRAO"
}
```

**Tipos de Preço disponíveis:**
- `PADRAO` - Preço normal sem desconto
- `PROMOCIONAL` - Preço com 10% de desconto

**Response (Success - 201 CREATED):**
```json
{
  "id": 1,
  "clienteId": 1,
  "clienteNome": "João Silva",
  "valorTotal": 117.50,
  "status": "CONFIRMADO",
  "dataCriacao": "2024-06-06T14:30:00",
  "tipoPreco": "PADRAO",
  "itens": [
    {
      "id": 1,
      "salgadoId": 1,
      "sabor": "Frango",
      "quantidade": 10,
      "valorUnitario": 5.00,
      "subtotal": 50.00
    },
    {
      "id": 2,
      "salgadoId": 2,
      "sabor": "Queijo",
      "quantidade": 15,
      "valorUnitario": 4.50,
      "subtotal": 67.50
    }
  ],
  "mensagem": "Pedido criado com sucesso"
}
```

**Response (Error - 404 NOT FOUND):**
```json
{
  "id": null,
  "clienteId": null,
  "clienteNome": null,
  "valorTotal": null,
  "status": null,
  "dataCriacao": null,
  "itens": null,
  "mensagem": "Cliente não encontrado"
}
```

**Response (Error - 400 BAD REQUEST):**
```json
{
  "id": null,
  "clienteId": null,
  "clienteNome": null,
  "valorTotal": null,
  "status": null,
  "dataCriacao": null,
  "itens": null,
  "mensagem": "Estoque insuficiente para salgado: Frango"
}
```

**Response (Error - 500 INTERNAL SERVER ERROR):**
```json
{
  "id": null,
  "clienteId": null,
  "clienteNome": null,
  "valorTotal": null,
  "status": null,
  "dataCriacao": null,
  "itens": null,
  "mensagem": "Erro ao criar pedido: [detalhes do erro]"
}
```

---

### 3. POST /pedidos/{id}/estornar

Estorna um pedido existente, devolvendo o estoque e gerando movimento de crédito.

**Request:**
```
POST /pedidos/1/estornar
```

**Response (Success - 200 OK):**
```json
{
  "id": 1,
  "clienteId": 1,
  "clienteNome": "João Silva",
  "valorTotal": 117.50,
  "status": "ESTORNADO",
  "dataCriacao": "2024-06-06T14:30:00",
  "tipoPreco": "PADRAO",
  "itens": [
    {
      "id": 1,
      "salgadoId": 1,
      "sabor": "Frango",
      "quantidade": 10,
      "valorUnitario": 5.00,
      "subtotal": 50.00
    },
    {
      "id": 2,
      "salgadoId": 2,
      "sabor": "Queijo",
      "quantidade": 15,
      "valorUnitario": 4.50,
      "subtotal": 67.50
    }
  ],
  "mensagem": "Pedido estornado com sucesso"
}
```

**Response (Error - 500 INTERNAL SERVER ERROR):**
```json
{
  "id": null,
  "clienteId": null,
  "clienteNome": null,
  "valorTotal": null,
  "status": null,
  "dataCriacao": null,
  "itens": null,
  "mensagem": "Erro ao estornar pedido: [detalhes do erro]"
}
```

---

### 4. GET /clientes/{id}/historico

Busca o histórico completo de pedidos e movimentações de um cliente.

**Request:**
```
GET /clientes/1/historico?dias=30
```

**Parâmetros Query:**
- `dias` (opcional) - Número de dias para buscar histórico (padrão: 30)

**Response (Success - 200 OK):**
```json
{
  "clienteId": 1,
  "clienteNome": "João Silva",
  "pedidos": [
    {
      "id": 1,
      "clienteId": 1,
      "clienteNome": "João Silva",
      "valorTotal": 117.50,
      "status": "CONFIRMADO",
      "dataCriacao": "2024-06-06T14:30:00",
      "tipoPreco": "PADRAO",
      "itens": [
        {
          "id": 1,
          "salgadoId": 1,
          "sabor": "Frango",
          "quantidade": 10,
          "valorUnitario": 5.00,
          "subtotal": 50.00
        }
      ],
      "mensagem": null
    },
    {
      "id": 2,
      "clienteId": 1,
      "clienteNome": "João Silva",
      "valorTotal": 65.00,
      "status": "ESTORNADO",
      "dataCriacao": "2024-06-05T10:15:00",
      "tipoPreco": "PADRAO",
      "itens": [
        {
          "id": 3,
          "salgadoId": 3,
          "sabor": "Catupiry",
          "quantidade": 10,
          "valorUnitario": 6.50,
          "subtotal": 65.00
        }
      ],
      "mensagem": null
    }
  ],
  "movimentos": [
    {
      "id": 1,
      "tipoMovimento": "DEBITO",
      "valor": 117.50,
      "dataHora": "2024-06-06T14:30:00"
    },
    {
      "id": 2,
      "tipoMovimento": "CREDITO",
      "valor": 65.00,
      "dataHora": "2024-06-05T10:15:00"
    }
  ],
  "resumoFinanceiro": {
    "totalCreditos": 65.00,
    "totalDebitos": 117.50,
    "saldo": -52.50,
    "totalGasto": 117.50,
    "totalEstornado": 65.00
  },
  "estatisticasPedidos": {
    "total": 2,
    "confirmados": 1,
    "cancelados": 0,
    "estornados": 1,
    "criados": 0
  }
}
```

**Response (Error - 404 NOT FOUND):**
```
404 Not Found
```

---

## DTOs Utilizados

### LoginRequest
```json
{
  "email": "string",
  "senha": "string"
}
```

### LoginResponse
```json
{
  "id": "long",
  "nome": "string",
  "email": "string",
  "token": "string",
  "mensagem": "string"
}
```

### PedidoRequest
```json
{
  "clienteId": "long",
  "itens": [
    {
      "salgadoId": "long",
      "sabor": "string",
      "quantidade": "integer",
      "valorUnitario": "double"
    }
  ],
  "tipoPreco": "string"
}
```

### PedidoResponse
```json
{
  "id": "long",
  "clienteId": "long",
  "clienteNome": "string",
  "valorTotal": "double",
  "status": "enum",
  "dataCriacao": "datetime",
  "tipoPreco": "string",
  "itens": [
    {
      "id": "long",
      "salgadoId": "long",
      "sabor": "string",
      "quantidade": "integer",
      "valorUnitario": "double",
      "subtotal": "double"
    }
  ],
  "mensagem": "string"
}
```

### HistoricoResponse
```json
{
  "clienteId": "long",
  "clienteNome": "string",
  "pedidos": [PedidoResponse],
  "movimentos": [
    {
      "id": "long",
      "tipoMovimento": "enum",
      "valor": "double",
      "dataHora": "datetime"
    }
  ],
  "resumoFinanceiro": {
    "totalCreditos": "double",
    "totalDebitos": "double",
    "saldo": "double",
    "totalGasto": "double",
    "totalEstornado": "double"
  },
  "estatisticasPedidos": {
    "total": "long",
    "confirmados": "long",
    "cancelados": "long",
    "estornados": "long",
    "criados": "long"
  }
}
```

---

## Status Pedido (Enum)

- `CRIADO` - Pedido criado e aguardando confirmação
- `CONFIRMADO` - Pedido confirmado e processado
- `CANCELADO` - Pedido cancelado pelo cliente
- `ESTORNADO` - Pedido estornado e valor devolvido

## Tipo Movimento (Enum)

- `CREDITO` - Movimento de crédito (estorno)
- `DEBITO` - Movimento de débito (compra)

---

## Observações

- Todos os endpoints retornam JSON
- Token de login é gerado como UUID (implementação simplificada)
- Em produção, utilizar autenticação JWT real
- Validações adicionais podem ser implementadas nos Services
- Os endpoints utilizam os Design Patterns implementados (Strategy, Factory, Command, Observer, State)
