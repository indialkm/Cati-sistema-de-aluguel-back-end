package com.cati.tcc.controller;

import org.springframework.web.bind.annotation.*;
import com.cati.tcc.service.TransacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public/teste")
@Tag(name = "Testes Manuais")
public class PagamentoTesteController {

    private final TransacaoService transacaoService;

    public PagamentoTesteController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    // URL: http://localhost:8080/public/teste/confirmar/pi_12345
    /*@PostMapping("/confirmar/{stripeId}")
    public String simularSucessoStripe(@PathVariable String stripeId) {
        transacaoService.confirmarPagamentoEGerarAluguel(stripeId);
        return "Processamento de confirmação disparado! Verifique as tabelas tb_aluguel e tb_pedido.";
    }*/
}