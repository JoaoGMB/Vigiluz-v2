package br.com.vigiluz.controller;

import br.com.vigiluz.dto.AtualizarStatusRequest;
import br.com.vigiluz.dto.RegistroResponse;
import br.com.vigiluz.dto.SolicitacaoRequest;
import br.com.vigiluz.dto.SolicitacaoResponse;
import br.com.vigiluz.model.Solicitacao;
import br.com.vigiluz.service.SolicitacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/solicitacoes")
public class SolicitacaoController {

    private final SolicitacaoService service;

    public SolicitacaoController(SolicitacaoService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<?> registrarSolicitacao(@RequestBody SolicitacaoRequest request) {
        try {
            RegistroResponse resposta = service.registrarSolicitacao(request);
            if (resposta.isDuplicata()) {
                return ResponseEntity.ok(resposta);
            }
            return ResponseEntity.status(HttpStatus.CREATED).body(resposta);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<List<SolicitacaoResponse>> listarTodas() {
        List<SolicitacaoResponse> lista = service.listarTodas()
                .stream()
                .map(SolicitacaoResponse::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{protocolo}")
    public ResponseEntity<?> buscarPorProtocolo(@PathVariable String protocolo) {
        Optional<Solicitacao> resultado = service.buscarPorProtocolo(protocolo);
        if (resultado.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Nenhuma solicitação encontrada para o protocolo: " + protocolo));
        }
        return ResponseEntity.ok(SolicitacaoResponse.from(resultado.get()));
    }

    @PatchMapping("/{protocolo}/status")
    public ResponseEntity<?> atualizarStatus(
            @PathVariable String protocolo,
            @RequestBody AtualizarStatusRequest request) {
        try {
            boolean atualizado = service.atualizarStatus(protocolo, request);
            if (!atualizado) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of("erro", "Protocolo não encontrado: " + protocolo));
            }
            return ResponseEntity.ok(Map.of("mensagem", "Status atualizado com sucesso."));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(Map.of("erro", e.getMessage()));
        }
    }
}
