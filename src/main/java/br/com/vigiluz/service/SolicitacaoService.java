package br.com.vigiluz.service;

import br.com.vigiluz.dto.AtualizarStatusRequest;
import br.com.vigiluz.dto.RegistroResponse;
import br.com.vigiluz.dto.SolicitacaoRequest;
import br.com.vigiluz.model.Categoria;
import br.com.vigiluz.model.HistoricoStatus;
import br.com.vigiluz.model.Prioridade;
import br.com.vigiluz.model.Solicitacao;
import br.com.vigiluz.model.Status;
import br.com.vigiluz.model.Usuario;
import br.com.vigiluz.repository.SolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository repository;
    private final AtomicInteger contadorProtocolo = new AtomicInteger(1);

    public SolicitacaoService(SolicitacaoRepository repository) {
        this.repository = repository;
    }

    public RegistroResponse registrarSolicitacao(SolicitacaoRequest request) {
        validarCamposObrigatorios(request);

        if (jaExisteSolicitacaoSemelhanteEmAberto(request.getBairro(), request.getCategoria())) {
            return new RegistroResponse(
                null,
                "Aviso: Já existe uma solicitação idêntica em aberto para este bairro. Equipe notificada do reforço.",
                true
            );
        }

        Usuario usuario = null;
        if (!request.isAnonima()) {
            String id = UUID.randomUUID().toString().substring(0, 8);
            String nome = request.getUsuarioNome() != null ? request.getUsuarioNome() : "Cidadão";
            String cpf = request.getUsuarioCpf() != null ? request.getUsuarioCpf() : "000.000.000-00";
            usuario = new Usuario(id, nome, cpf);
        }

        Solicitacao nova = new Solicitacao(
                gerarProtocoloInteligente(),
                request.getDescricao(),
                request.getBairro(),
                request.getCategoria(),
                request.getPrioridade(),
                request.isAnonima(),
                usuario
        );

        nova.adicionarHistorico(new HistoricoStatus(
                Status.ABERTO,
                "Solicitação registrada. Prazo estimado (SLA): "
                        + calcularSLA(request.getPrioridade()) + " horas."
        ));

        repository.salvar(nova);

        String mensagem = nova.getProtocolo();
        if (isAreaCritica(request.getBairro())) {
            mensagem += "\n[ALERTA GESTOR]: O bairro '"
                    + request.getBairro().toUpperCase()
                    + "' atingiu nível crítico de demandas!";
        }

        return new RegistroResponse(nova.getProtocolo(), mensagem, false);
    }

    public List<Solicitacao> listarTodas() {
        return repository.buscarTodas();
    }

    public Optional<Solicitacao> buscarPorProtocolo(String protocolo) {
        return repository.buscarPorProtocolo(protocolo);
    }

    public boolean atualizarStatus(String protocolo, AtualizarStatusRequest request) {
        if (request.getNovoStatus() == null) {
            throw new IllegalArgumentException("O campo 'novoStatus' é obrigatório.");
        }

        Optional<Solicitacao> solicitacaoOpt = repository.buscarPorProtocolo(protocolo);
        if (solicitacaoOpt.isEmpty()) {
            return false;
        }

        String comentario = request.getComentario() != null ? request.getComentario() : "";
        solicitacaoOpt.get().adicionarHistorico(
                new HistoricoStatus(request.getNovoStatus(), comentario)
        );
        return true;
    }

    private boolean jaExisteSolicitacaoSemelhanteEmAberto(String bairro, Categoria categoria) {
        for (Solicitacao s : repository.buscarTodas()) {
            boolean mesmaCategoriaEBairro = s.getBairro().equalsIgnoreCase(bairro)
                    && s.getCategoria() == categoria;
            boolean statusEmAberto = s.getStatusAtual() == Status.ABERTO
                    || s.getStatusAtual() == Status.TRIAGEM;
            if (mesmaCategoriaEBairro && statusEmAberto) {
                return true;
            }
        }
        return false;
    }

    private boolean isAreaCritica(String bairro) {
        long count = repository.buscarTodas().stream()
                .filter(s -> s.getBairro().equalsIgnoreCase(bairro)
                        && (s.getStatusAtual() == Status.ABERTO
                            || s.getStatusAtual() == Status.TRIAGEM))
                .count();
        return count >= 3;
    }

    private int calcularSLA(Prioridade prioridade) {
        return switch (prioridade) {
            case ALTA  -> 4;
            case MEDIA -> 48;
            case BAIXA -> 168;
        };
    }

    private String gerarProtocoloInteligente() {
        String dataHoje = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        return String.format("VIGI-%s-%04d", dataHoje, contadorProtocolo.getAndIncrement());
    }

    private void validarCamposObrigatorios(SolicitacaoRequest request) {
        if (request.getDescricao() == null || request.getDescricao().isBlank()) {
            throw new IllegalArgumentException("O campo 'descricao' é obrigatório.");
        }
        if (request.getBairro() == null || request.getBairro().isBlank()) {
            throw new IllegalArgumentException("O campo 'bairro' é obrigatório.");
        }
        if (request.getCategoria() == null) {
            throw new IllegalArgumentException("O campo 'categoria' é obrigatório.");
        }
        if (request.getPrioridade() == null) {
            throw new IllegalArgumentException("O campo 'prioridade' é obrigatório.");
        }
    }
}
