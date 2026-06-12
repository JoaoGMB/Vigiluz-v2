package br.com.vigiluz.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class HistoricoStatus {

    private final LocalDateTime dataHora;
    private final Status statusAtual;
    private final String comentarioObrigatorio;

    public HistoricoStatus(Status statusAtual, String comentarioObrigatorio) {
        this.dataHora = LocalDateTime.now();
        this.statusAtual = statusAtual;
        this.comentarioObrigatorio = comentarioObrigatorio;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public Status getStatusAtual() {
        return statusAtual;
    }

    public String getComentarioObrigatorio() {
        return comentarioObrigatorio;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatadorDeData = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return "[" + dataHora.format(formatadorDeData) + "] "
                + statusAtual
                + " — "
                + comentarioObrigatorio;
    }
}
