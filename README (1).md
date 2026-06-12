# 💡 VigiLuz

Sistema de registro e acompanhamento de solicitações de iluminação pública. Cidadãos registram ocorrências, recebem um protocolo e acompanham o andamento até a resolução.

---

## Como funciona

### Registro de ocorrência

O cidadão informa o bairro, a categoria do problema e uma descrição. O registro pode ser feito de forma **anônima ou identificada**. Ao enviar, o sistema gera automaticamente um protocolo no formato `VIGI-YYYYMMDD-XXXX`.

Antes de registrar, o sistema verifica se já existe uma solicitação **idêntica em aberto** para o mesmo bairro e categoria. Se houver, não cria uma duplicata — apenas notifica que a equipe foi reforçada.

Se um bairro acumular 3 ou mais solicitações abertas, um **alerta de área crítica** é emitido automaticamente para o gestor.

---

### Prioridade e SLA

Cada solicitação recebe uma prioridade que define o prazo estimado de atendimento:

| Prioridade | Prazo |
|---|---|
| Alta | 4 horas |
| Média | 48 horas |
| Baixa | 168 horas (7 dias) |

---

### Acompanhamento

Com o número de protocolo, o cidadão pode consultar o status atual e o histórico completo de movimentações da solicitação.

**Fluxo de status:**

```
ABERTO → TRIAGEM → EM_EXECUCAO → RESOLVIDO → ENCERRADO
```

Cada mudança de status pode incluir um comentário da equipe responsável.

---

### Painel administrativo

O painel lista todas as solicitações e permite que gestores atualizem o status das ocorrências, adicionando observações a cada etapa.

---

## Categorias de ocorrência

- Poste apagado
- Luz piscando
- Fiação exposta
