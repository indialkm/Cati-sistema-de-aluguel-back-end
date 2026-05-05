package com.cati.tcc.controller;

import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.cati.tcc.dto.request.ItemCarrinhoRequest;
import com.cati.tcc.dto.response.CarrinhoResponse;
import com.cati.tcc.mapper.CarrinhoMapper;
import com.cati.tcc.model.Carrinho;
import com.cati.tcc.service.CarrinhoService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;


@RestController
@RequestMapping("/carrinho")
@Tag(name = "Carrinho")
public class CarrinhoController {
	
	private final CarrinhoService carrinhoService;
    private final CarrinhoMapper carrinhoMapper;

    public CarrinhoController(CarrinhoService carrinhoService, CarrinhoMapper carrinhoMapper) {
		this.carrinhoService = carrinhoService;
		this.carrinhoMapper = carrinhoMapper;
	}

    @PostMapping("/adicionar")
    public ResponseEntity<CarrinhoResponse> adicionar(
        @RequestParam(required = false) UUID idCarrinho,
        @RequestBody ItemCarrinhoRequest request
    ) {

        return ResponseEntity.ok(carrinhoMapper.toResponse
        		(carrinhoService.adicionarItem(idCarrinho, request)));
    }
	
	@GetMapping("/{id}")
	@PreAuthorize("permitAll()")
	public ResponseEntity<CarrinhoResponse> buscarPorId(@PathVariable("id") UUID carrinhoID){
		
		return ResponseEntity.ok(carrinhoMapper.toResponse(carrinhoService.buscarCarrinhoId(carrinhoID))) ;
		
	}
	
	@GetMapping("/status")
	@PreAuthorize("permitAll()")
	public ResponseEntity<CarrinhoResponse> buscarPorAtivo(){
		
		return ResponseEntity.ok(carrinhoMapper.toResponse(carrinhoService.buscarCarrinhoAtivo())) ;
		
	}


}
