package com.cati.tcc.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.response.HistoricoResponse;
import com.cati.tcc.mapper.HistoricoMapper;
import com.cati.tcc.model.HistoricoTransacao;
import com.cati.tcc.service.HistoricoTransacaoService;

@RestController
@RequestMapping("/historicos")
public class HistoricoController {
	
    private final HistoricoTransacaoService service;
    private final HistoricoMapper mapper; // Injeção do Mapper

    // Adicionamos o mapper ao construtor
    public HistoricoController(HistoricoTransacaoService service, HistoricoMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping("/admin/todos")
    @PreAuthorize("hasRole('ROLE_OWNER')") 
    public ResponseEntity<Page<HistoricoResponse>> listarTudo(
            @PageableDefault(size = 10, sort = "dataEvento", direction = Direction.DESC) Pageable pageable) {
        
        // Buscamos a página de Entidades e convertemos para página de Responses
        Page<HistoricoResponse> response = service.consultarComoEmpresa(pageable)
                                                  .map(mapper::toResponse);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/meu-historico/{usuarioId}")
    @PreAuthorize("hasAnyRole('ROLE_CLIENT', 'ROLE_OWNER')")
    public ResponseEntity<Page<HistoricoResponse>> listarMeuHistorico(
            @PathVariable UUID usuarioId,
            @PageableDefault(size = 10, sort = "dataEvento", direction = Direction.DESC) Pageable pageable) {
        
        // Buscamos a página de Entidades e aplicamos o mapper
        Page<HistoricoResponse> response = service.consultarComoCliente(usuarioId, pageable)
                                                  .map(mapper::toResponse);
        
        return ResponseEntity.ok(response);
    }
}
