package br.com.vigiluz.dto;

import br.com.vigiluz.model.Categoria;
import br.com.vigiluz.model.Prioridade;

public class SolicitacaoRequest {

    private String descricao;
    private String bairro;
    private Categoria categoria;
    private Prioridade prioridade;
    private boolean anonima;
    private String usuarioNome;
    private String usuarioCpf;

    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }

    public String getBairro() { return bairro; }
    public void setBairro(String bairro) { this.bairro = bairro; }

    public Categoria getCategoria() { return categoria; }
    public void setCategoria(Categoria categoria) { this.categoria = categoria; }

    public Prioridade getPrioridade() { return prioridade; }
    public void setPrioridade(Prioridade prioridade) { this.prioridade = prioridade; }

    public boolean isAnonima() { return anonima; }
    public void setAnonima(boolean anonima) { this.anonima = anonima; }

    public String getUsuarioNome() { return usuarioNome; }
    public void setUsuarioNome(String usuarioNome) { this.usuarioNome = usuarioNome; }

    public String getUsuarioCpf() { return usuarioCpf; }
    public void setUsuarioCpf(String usuarioCpf) { this.usuarioCpf = usuarioCpf; }
}
