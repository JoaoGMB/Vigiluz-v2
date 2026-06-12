package br.com.vigiluz.dto;

import br.com.vigiluz.model.Categoria;
import br.com.vigiluz.model.HistoricoStatus;
import br.com.vigiluz.model.Prioridade;
import br.com.vigiluz.model.Solicitacao;
import br.com.vigiluz.model.Status;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class SolicitacaoResponse {

    private String protocolo;
    private String descricao;
    private String bairro;
    private Categoria categoria;
    private Prioridade prioridade;
    private boolean anonima;
    private String usuarioNome;
    private Status statusAtual;
    private LocalDateTime dataAbertura;
    private List<HistoricoStatusResponse> historico;

    public static SolicitacaoResponse from(Solicitacao s) {
        SolicitacaoResponse r = new SolicitacaoResponse();
        r.protocolo   = s.getProtocolo();
        r.descricao   = s.getDescricao();
        r.bairro      = s.getBairro();
        r.categoria   = s.getCategoria();
        r.prioridade  = s.getPrioridade();
        r.anonima     = s.isAnonima();
        r.usuarioNome = (s.getUsuario() != null) ? s.getUsuario().getNome() : null;
        r.statusAtual = s.getStatusAtual();
        r.historico   = s.getListaDeHistorico().stream()
                          .map(HistoricoStatusResponse::from)
                          .collect(Collectors.toList());
        
        if (!s.getListaDeHistorico().isEmpty()) {
            r.dataAbertura = s.getListaDeHistorico().get(0).getDataHora();
        }
        return r;
    }

    public String getProtocolo()   { return protocolo; }
    public String getDescricao()   { return descricao; }
    public String getBairro()      { return bairro; }
    public Categoria getCategoria(){ return categoria; }
    public Prioridade getPrioridade(){ return prioridade; }
    public boolean isAnonima()     { return anonima; }
    public String getUsuarioNome() { return usuarioNome; }
    public Status getStatusAtual() { return statusAtual; }
    public LocalDateTime getDataAbertura() { return dataAbertura; }
    public List<HistoricoStatusResponse> getHistorico() { return historico; }

    public static class HistoricoStatusResponse {
        private LocalDateTime dataHora;
        private Status status;
        private String comentario;

        public static HistoricoStatusResponse from(HistoricoStatus h) {
            HistoricoStatusResponse hr = new HistoricoStatusResponse();
            hr.dataHora   = h.getDataHora();
            hr.status     = h.getStatusAtual();
            hr.comentario = h.getComentarioObrigatorio();
            return hr;
        }

        public LocalDateTime getDataHora()  { return dataHora; }
        public Status getStatus()           { return status; }
        public String getComentario()       { return comentario; }
    }
}
