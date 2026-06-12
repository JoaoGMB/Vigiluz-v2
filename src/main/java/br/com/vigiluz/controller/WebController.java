package br.com.vigiluz.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping("/")
    public String home() {
        return "index";
    }

    @GetMapping("/denuncia")
    public String denuncia() {
        return "denuncia";
    }

    @GetMapping("/protocolo")
    public String protocolo() {
        return "protocolo";
    }

    @GetMapping("/consultar")
    public String consultar() {
        return "consultar";
    }

    @GetMapping("/acompanhamento")
    public String acompanhamento() {
        return "acompanhamento";
    }

    @GetMapping("/funcionario/login")
    public String login() {
        return "login";
    }

    @GetMapping("/funcionario/painel")
    public String painel() {
        return "painel";
    }

    @GetMapping("/funcionario/detalhes")
    public String detalhes() {
        return "detalhes";
    }
}
