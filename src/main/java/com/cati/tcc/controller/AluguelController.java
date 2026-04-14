package com.cati.tcc.controller;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.dto.response.AluguelResponse;
import com.cati.tcc.dto.response.ChecklistResponse;
import com.cati.tcc.mapper.AluguelMapper;
import com.cati.tcc.mapper.ChecklistMapper;
import com.cati.tcc.service.AluguelService;
import com.cati.tcc.service.ChecklistService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/alugueis")
@Tag(name = "Aluguel")
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
    	
      	return ResponseEntity.ok(aluguelMapper.toResponse(aluguelService.buscarPorId(idAluguel)));
    }
    
    
    @PatchMapping("/{id}/montar")
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Void> atualizarParaMontado(@PathVariable UUID id) {
        aluguelService.alterarStatusParaMontado(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/retirar")
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<Void> atualizarParaRetirada(@PathVariable UUID id) {
        aluguelService.alterarStatusParaRetirada(id);
        return ResponseEntity.noContent().build();
    }
    
    @PatchMapping("/{id}/checkout")
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ChecklistResponse> checkoutEquipamento(@RequestBody ChecklistRequest request)
    {
    	return ResponseEntity.status(HttpStatus.CREATED).body(checkMapper.toResponse(checkService.saida(request)));
    }
    
    @PatchMapping("/{id}/checking")
    @PreAuthorize("hasRole('OWNER')")
    @SecurityRequirement(name = "bearer-key")
    public ResponseEntity<ChecklistResponse> checkinAluguel(@RequestBody ChecklistRequest request)
    {
    	return ResponseEntity.status(HttpStatus.CREATED).body(checkMapper.toResponse(checkService.entrada(request)));
    }
    
    
    
    
    
}
