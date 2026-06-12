package br.com.vigiluz.dto;

public class RegistroResponse {

    private String protocolo;
    private String mensagem;
    private boolean duplicata;

    public RegistroResponse(String protocolo, String mensagem, boolean duplicata) {
        this.protocolo = protocolo;
        this.mensagem  = mensagem;
        this.duplicata = duplicata;
    }

    public String getProtocolo() { return protocolo; }
    public String getMensagem()  { return mensagem; }
    public boolean isDuplicata() { return duplicata; }
}
