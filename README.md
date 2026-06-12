# 💡 VigiLuz

> Sistema inteligente de registro e acompanhamento de solicitações de iluminação pública.

VigiLuz permite que cidadãos registrem ocorrências de iluminação pública — como postes apagados, luzes piscando ou fiação exposta — e acompanhem o andamento em tempo real por meio de um número de protocolo.

---

## 🚀 Tecnologias

- **Java 17**
- **Spring Boot 3.2.5**
- **Thymeleaf** — templates HTML server-side
- **Spring Validation** — validação de requisições
- **Maven** — gerenciamento de dependências

---

## 📁 Estrutura do Projeto

```
vigiluz/
└── src/main/
    ├── java/br/com/vigiluz/
    │   ├── controller/        # Endpoints REST e rotas web
    │   ├── service/           # Regras de negócio
    │   ├── repository/        # Armazenamento em memória
    │   ├── model/             # Entidades (Solicitacao, Status, Categoria...)
    │   └── dto/               # Objetos de requisição e resposta
    └── resources/
        ├── templates/         # Páginas HTML (Thymeleaf)
        ├── static/
        │   ├── css/           # Estilos
        │   └── js/            # Scripts do frontend
        └── application.properties
```

---

## ⚙️ Como rodar

### Pré-requisitos

- JDK 17 ou superior
- Maven instalado (ou use o wrapper `./mvnw` incluso)

### Executando

```bash
# Clone o repositório
git clone https://github.com/seu-usuario/vigiluz.git
cd vigiluz

# Rode com o Maven Wrapper
./mvnw spring-boot:run
```

Acesse no navegador: [http://localhost:8080](http://localhost:8080)

---

## 🗺️ Páginas disponíveis

| Rota | Descrição |
|---|---|
| `/` | Página inicial |
| `/denuncia` | Formulário de nova solicitação |
| `/protocolo` | Exibição do protocolo gerado |
| `/consultar` | Consulta de solicitação por protocolo |
| `/acompanhamento` | Acompanhamento do status |
| `/painel` | Painel administrativo |
| `/login` | Login do sistema |

---

## 🔌 API REST

### Registrar solicitação
```http
POST /solicitacoes
Content-Type: application/json

{
  "descricao": "Poste apagado na Rua das Flores",
  "bairro": "Centro",
  "categoria": "POSTE_APAGADO",
  "prioridade": "ALTA",
  "anonima": false,
  "nomeUsuario": "João Silva"
}
```

### Listar todas as solicitações
```http
GET /solicitacoes
```

### Buscar por protocolo
```http
GET /solicitacoes/{protocolo}
```

### Atualizar status
```http
PATCH /solicitacoes/{protocolo}/status
Content-Type: application/json

{
  "novoStatus": "EM_EXECUCAO",
  "observacao": "Equipe despachada ao local"
}
```

---

## 📋 Categorias e Status

**Categorias de ocorrência:**
- `POSTE_APAGADO`
- `LUZ_PISCANDO`
- `FIACAO_EXPOSTA`

**Fluxo de status:**
```
ABERTO → TRIAGEM → EM_EXECUCAO → RESOLVIDO → ENCERRADO
```

---

## 🔒 Funcionalidades

- ✅ Registro anônimo ou identificado
- ✅ Geração automática de protocolo único
- ✅ Detecção de solicitações duplicadas
- ✅ Histórico completo de alterações de status
- ✅ Painel administrativo para gestão das ocorrências
- ✅ Armazenamento em memória (sem necessidade de banco de dados)

---

## 📄 Licença

Este projeto foi desenvolvido para fins acadêmicos.
