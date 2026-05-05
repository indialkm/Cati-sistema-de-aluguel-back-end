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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.dto.response.AluguelResponse;
import com.cati.tcc.dto.response.ChecklistResponse;
import com.cati.tcc.dto.response.EmpresaResponse;
import com.cati.tcc.mapper.AluguelMapper;
import com.cati.tcc.mapper.ChecklistMapper;
import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.enums.StatusAluguel;
import com.cati.tcc.service.AluguelService;
import com.cati.tcc.service.ChecklistService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/alugueis")
@Tag(name = "Aluguel", description = "Endpoints para gestão de aluguel" )
public class AluguelController {
	
	private final AluguelService aluguelService;
    private final AluguelMapper aluguelMapper;
    
    private final ChecklistMapper checkMapper;
    private final ChecklistService checkService;
   
	public AluguelController(AluguelService aluguelService, AluguelMapper aluguelMapper, ChecklistMapper checkMapper,
			ChecklistService checkService) {
		this.aluguelService = aluguelService;
		this.aluguelMapper = aluguelMapper;
		this.checkMapper = checkMapper;
		this.checkService = checkService;
	}

	@Operation(
	        summary = "Listar todos os alugueis", 
	        description = "Este endpoint é usado apra validar todos os alugueis, ."
	    )
	@GetMapping
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Page<AluguelResponse>> listar(
            @PageableDefault(size = 10, sort = "id") Pageable paginacao) {
        
    	return ResponseEntity.ok(aluguelService.listarTodos(paginacao)
                .map(aluguelMapper::toResponse));
    }
    
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('OWNER', 'ADMIN')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<AluguelResponse> aluguelPorId(@PathVariable("id") UUID idAluguel ){
    	
      	return ResponseEntity.ok(aluguelMapper.toResponse(aluguelService.buscarAluguel(idAluguel)));
    }
    
   
    @Operation(
            summary = "Buscar aluguel por pedido", 
            description = "Este endpoint busca na base de dados a lista de alugueis que correspondem ao pedido selecionado."
        )
    @GetMapping("/pedido/{id}")
    @PreAuthorize("hasRole('CLIENT')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<List<AluguelResponse>> buscarAluguelPedido(@PathVariable("id") UUID idPedido){
    	return ResponseEntity.ok(aluguelService.alugueisPorPedido(idPedido).stream().map(aluguelMapper::toResponse).toList());
    	
    }
    
    /**********Mundaça de Status**********************/
    

    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<AluguelResponse> atualizarStatus(
        @PathVariable UUID id, 
        @RequestBody String novoStatus 
    ) {
    	String statusLimpo = novoStatus.replace("\"", "");
        return ResponseEntity.ok( aluguelMapper.toResponse(aluguelService.mudarStatus(id, novoStatus)));
    }
    
    
    @PatchMapping(value = "/{idAluguel}/montar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Void> atualizarParaMontada(
        @PathVariable UUID idAluguel, 
        @RequestPart("dados") ChecklistRequest request, 
        @RequestPart("fotos") List<MultipartFile> fotos  
    ) {
        
        aluguelService.statusMontada(idAluguel, request, fotos);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping(value = "/{idAluguel}/retirar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Void> atualizarParaRetirada(
        @PathVariable UUID idAluguel, 
        @RequestPart("dados") ChecklistRequest request, 
        @RequestPart("fotos") List<MultipartFile> fotos
    ) {
        aluguelService.statusRetirada(idAluguel, request, fotos);
        return ResponseEntity.noContent().build();
    }    
    
    @GetMapping("/filtrar")
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Page<AluguelResponse>> filtrarAlugueis(
            @RequestParam(required = false) List<StatusAluguel> status,
            @PageableDefault(size = 10) Pageable pageable) {
        
       ;
        return ResponseEntity.ok(aluguelService.
        		buscarComFiltroStatus(status, pageable).map(aluguelMapper::toResponse));
    }
    
    @PreAuthorize("hasRole('CLIENT')")
	@SecurityRequirement(name = "bearer-key")
	@GetMapping("/acompanhar/{idAluguel}")
	public ResponseEntity<AluguelResponse> acompanharStatus(
			@PathVariable("idAluguel") UUID idAluguel) {
		
		return ResponseEntity.ok(aluguelMapper.toResponse(aluguelService.buscarPorId(idAluguel)));
	}
    
    
}
