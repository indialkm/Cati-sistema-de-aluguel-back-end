package com.cati.tcc.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.cati.tcc.service.TransacaoService;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/public/webhooks")
@Tag(name = "Webhooks")
public class WebhookController {

    private final TransacaoService transacaoService;

    public WebhookController(TransacaoService transacaoService) {
        this.transacaoService = transacaoService;
    }

    @PostMapping("/stripe")
    public ResponseEntity<Void> handleStripeWebhook(@RequestBody String payload) {
        // No TCC, vamos simplificar: o payload contém o JSON do Stripe.
        // O ideal é usar a biblioteca do Stripe para converter, 
        // mas para tester a lógica do Aluguel agora, vamos focar no ID.
        
        // Aqui você chamaria o seu transacaoService
        // transacaoService.confirmarPagamentoEGerarAluguel(idExtraidoDoJson);
        
        return ResponseEntity.ok().build();
    }
}