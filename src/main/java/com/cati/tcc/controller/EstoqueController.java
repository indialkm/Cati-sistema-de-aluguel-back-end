package com.cati.tcc.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cati.tcc.dto.request.EstoqueRequest;
import com.cati.tcc.dto.response.DetalhesModeloResponse;
import com.cati.tcc.dto.response.EquipamentoResponse;
import com.cati.tcc.dto.response.EstoqueResponse;
import com.cati.tcc.dto.update.EstoquePatchDTO;
import com.cati.tcc.mapper.EquipamentoMapper;
import com.cati.tcc.mapper.EstoqueMapper;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.service.EstoqueService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/estoque")
@Tag(name = "Estoque")
public class EstoqueController {
	
	private final EstoqueService estoqueService;
    private final EstoqueMapper mapper;
    private final EquipamentoMapper equipamentoMapper;

    public EstoqueController(EstoqueService estoqueService, EstoqueMapper mapper, EquipamentoMapper equipamentoMapper) {
		this.estoqueService = estoqueService;
		this.mapper = mapper;
		this.equipamentoMapper = equipamentoMapper;
	}

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) 
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<EstoqueResponse> criar(
        @RequestPart("request") @Valid EstoqueRequest request, 
        @RequestPart(value = "arquivos", required = false) List<MultipartFile> arquivos            
    ) {
      
        var estoque = estoqueService.criar(request, arquivos); 
        var response = mapper.toResponse(estoque);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping
    @PreAuthorize("permitAll()")
	public ResponseEntity<Page<EstoqueResponse>> vitrine(
			@PageableDefault(page = 0, size = 10) Pageable pageable)
			
	{
    	
    	Page<EstoqueResponse> paginaResponse = estoqueService.listarEstoques(pageable)
                .map(mapper::toResponse);
    			


    	return ResponseEntity.ok(paginaResponse);
	}
    
    @GetMapping("/detalhes/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<DetalhesModeloResponse> obterDetalhesProduto(@PathVariable UUID id) {
        DetalhesModeloResponse response = estoqueService.buscarDetalhesProcessados(id);
        return ResponseEntity.ok(response);
    }
   
    @PreAuthorize("hasRole('OWNER')")
	@SecurityRequirement(name = "bearer-key")
	@GetMapping("/visualizar")
	public ResponseEntity<Page<EstoqueResponse>> visualizarDadosEstoque(
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return ResponseEntity.ok(estoqueService.listarTodos(pageable).map(mapper::toResponse));
	}

	@PreAuthorize("hasRole('OWNER')")
	@SecurityRequirement(name = "bearer-key")
	@GetMapping("/categoria")
	public ResponseEntity<Page<EstoqueResponse>> filtrarPorCategoria(
			@RequestParam String nome,
			@PageableDefault(page = 0, size = 10) Pageable pageable) {

		return ResponseEntity.ok(estoqueService.filtrarPorCategoria(nome, pageable).map(mapper::toResponse));
	}
	
	@PatchMapping("/atualizar/{id}")
    public ResponseEntity<EstoqueResponse> atualizarParcial(
            @PathVariable UUID id, 
            @RequestBody EstoquePatchDTO dto) { 
        return ResponseEntity.ok( mapper.toResponse(estoqueService.atualizarParcial(id, dto)));
    }

	
	@PreAuthorize("hasRole('OWNER')")
	@SecurityRequirement(name = "bearer-key")
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable UUID id) {
	    estoqueService.excluir(id);
	    return ResponseEntity.noContent().build();
	}
}
