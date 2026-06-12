package br.com.vigiluz.repository;

import br.com.vigiluz.model.Solicitacao;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Repository
public class SolicitacaoRepository {

    private final List<Solicitacao> armazenamento = new ArrayList<>();

    public Solicitacao salvar(Solicitacao solicitacao) {
        armazenamento.add(solicitacao);
        return solicitacao;
    }

    public List<Solicitacao> buscarTodas() {
        return Collections.unmodifiableList(armazenamento);
    }

    public Optional<Solicitacao> buscarPorProtocolo(String protocolo) {
        if (protocolo == null || protocolo.isBlank()) {
            return Optional.empty();
        }
        return armazenamento.stream()
                .filter(s -> s.getProtocolo().equalsIgnoreCase(protocolo))
                .findFirst();
    }

    public int contarTotal() {
        return armazenamento.size();
    }
}
