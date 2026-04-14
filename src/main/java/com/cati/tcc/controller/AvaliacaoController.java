package com.cati.tcc.controller;

import com.cati.tcc.dto.request.AvaliacaoRequest;
import com.cati.tcc.dto.response.AvaliacaoResponse;
import com.cati.tcc.mapper.AvaliacaoMapper;
import com.cati.tcc.service.AvaliacaoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/avaliacoes")
@Tag(name = "Avaliacao")
public class AvaliacaoController {

    private final AvaliacaoService avaliacaoService;
    private final AvaliacaoMapper avaliacaoMapper;

    public AvaliacaoController(AvaliacaoService avaliacaoService,  AvaliacaoMapper avalicaoMapper) {
        this.avaliacaoService = avaliacaoService;
        this.avaliacaoMapper = avalicaoMapper;
    }

    @PostMapping("/estoque/{idEstoque}")
    public ResponseEntity<AvaliacaoResponse> criar(
            @PathVariable UUID idEstoque, 
            @RequestBody @Valid AvaliacaoRequest request) {
        
        // O Service já mapeia, salva e devolve o ResponseDTO
        return ResponseEntity.status(HttpStatus.CREATED)
                             .body(avaliacaoMapper.toResponse(avaliacaoService.criar(idEstoque, request)));
    }

    @GetMapping("/estoque/{idEstoque}")
    public ResponseEntity<Page<AvaliacaoResponse>> listarPorEstoque(
            @PathVariable UUID idEstoque, 
            Pageable pageable) {
        
        return ResponseEntity.ok(avaliacaoService.exibirAvaliacoesEstoque(idEstoque, pageable).map(avaliacaoMapper::toResponse));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<AvaliacaoResponse> atualizarParcial(
            @PathVariable Long id, 
            @RequestBody AvaliacaoRequest dto) {
        
        return ResponseEntity.ok(avaliacaoMapper.toResponse(avaliacaoService.atualizarParcial(id, dto)));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        avaliacaoService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/estoque/{idEstoque}/media")
    public ResponseEntity<Double> buscarMedia(@PathVariable UUID idEstoque) {
        return ResponseEntity.ok(avaliacaoService.avaliacaoTotal(idEstoque));
    }
}