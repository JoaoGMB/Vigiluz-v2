package br.com.vigiluz.dto;

import br.com.vigiluz.model.Status;

public class AtualizarStatusRequest {

    private Status novoStatus;
    private String comentario;

    public Status getNovoStatus() { return novoStatus; }
    public void setNovoStatus(Status novoStatus) { this.novoStatus = novoStatus; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }
}
