package com.cati.tcc.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cati.tcc.dto.request.ItemCarrinhoRequest;
import com.cati.tcc.dto.request.ReservaRequest;
import com.cati.tcc.dto.response.CarrinhoResponse;
import com.cati.tcc.dto.response.ReservaResponse;
import com.cati.tcc.mapper.ReservaMapper;
import com.cati.tcc.model.Carrinho;
import com.cati.tcc.service.ReservaService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/reserva")
@Tag(name = "Reserva")
public class ReservaController {
	
	
	private final ReservaService reservaService;
	private final ReservaMapper reservaMapper;
	
	public ReservaController(ReservaService reservaService, ReservaMapper mapperService) {
		this.reservaService = reservaService;
		this.reservaMapper = mapperService;
	}



	@PostMapping("/adicionar")
	@PreAuthorize("permitAll()")
    public ResponseEntity<ReservaResponse> save(@RequestBody ReservaRequest request) {
       
		return ResponseEntity.ok(reservaMapper.toResponse(reservaService.criarReserva(request)));
		
    }

}
