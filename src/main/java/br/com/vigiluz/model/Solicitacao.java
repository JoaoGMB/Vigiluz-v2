package br.com.vigiluz.model;

import java.util.ArrayList;
import java.util.List;

public class Solicitacao {

    private final String protocolo;
    private final String descricao;
    private final String bairro;
    private final Categoria categoria;
    private final Prioridade prioridade;
    private final boolean isAnonima;
    private final Usuario usuario;
    private final List<HistoricoStatus> listaDeHistorico;

    public Solicitacao(String protocolo, String descricao, String bairro,
                       Categoria categoria, Prioridade prioridade,
                       boolean isAnonima, Usuario usuario) {
        this.protocolo = protocolo;
        this.descricao = descricao;
        this.bairro = bairro;
        this.categoria = categoria;
        this.prioridade = prioridade;
        this.isAnonima = isAnonima;
        this.usuario = isAnonima ? null : usuario;
        this.listaDeHistorico = new ArrayList<>();
    }

    public void adicionarHistorico(HistoricoStatus novoRegistro) {
        listaDeHistorico.add(novoRegistro);
    }

    public Status getStatusAtual() {
        if (listaDeHistorico.isEmpty()) {
            return null;
        }
        return listaDeHistorico.get(listaDeHistorico.size() - 1).getStatusAtual();
    }

    public String getProtocolo() {
        return protocolo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getBairro() {
        return bairro;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public boolean isAnonima() {
        return isAnonima;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public List<HistoricoStatus> getListaDeHistorico() {
        return listaDeHistorico;
    }
}
