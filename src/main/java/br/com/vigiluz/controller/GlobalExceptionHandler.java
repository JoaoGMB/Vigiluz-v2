package br.com.vigiluz.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleJsonInvalido(HttpMessageNotReadableException ex) {
        String detalhe = ex.getMessage() != null && ex.getMessage().contains("Categoria")
                ? "Valor inválido para 'categoria'. Use: POSTE_APAGADO, LUZ_PISCANDO ou FIACAO_EXPOSTA."
                : ex.getMessage() != null && ex.getMessage().contains("Prioridade")
                ? "Valor inválido para 'prioridade'. Use: ALTA, MEDIA ou BAIXA."
                : ex.getMessage() != null && ex.getMessage().contains("Status")
                ? "Valor inválido para 'novoStatus'. Use: ABERTO, TRIAGEM, EM_EXECUCAO, RESOLVIDO ou ENCERRADO."
                : "Corpo da requisição inválido ou malformado.";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("erro", detalhe));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGenerico(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(Map.of("erro", "Erro interno: " + ex.getMessage()));
    }
}
