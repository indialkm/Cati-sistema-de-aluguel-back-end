package com.cati.tcc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.dto.request.ReservaRequest;
import com.cati.tcc.mapper.ReservaMapper;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.Reserva;
import com.cati.tcc.model.enums.StatusReserva;
import com.cati.tcc.repository.ReservaRepository;

@Service
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final ReservaMapper reservaMapper;
    
	private final EstoqueService estoqueService;	

	public ReservaService(ReservaRepository reservaRepository, ReservaMapper reservaMapper,
			EstoqueService estoqueService) {
		this.reservaRepository = reservaRepository;
		this.reservaMapper = reservaMapper;
		this.estoqueService = estoqueService;
	}

	@Transactional
	public Reserva criarReserva(ReservaRequest request) {
	    // 1. Validar disponibilidade (Sua lógica já está ótima!)
	    // Passamos o ID que agora vem de dentro do Request Body
	    List<Estoque> disponiveis = estoqueService.verificarDisponibilidadeParaReserva(
	        request.idEstoque(), 
	        request.dataInicial(), 
	        request.dataFinal()
	    );

	    if (disponiveis.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "Equipamento indisponível para este período.");
	    }

	    // 2. Mapear e Salvar
	    Reserva reserva = reservaMapper.toEntity(request);
	    reserva.setEstoque(disponiveis.get(0));
	    reserva.setDisponibilidade(StatusReserva.ALUGADA);

	    return reservaRepository.save(reserva);
	}
	
	
    @Transactional
    public Reserva buscarPorId(UUID id) {
    	Reserva reserva = reservaRepository.findById(id)
    		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada"));
    		return reserva;
    	
    }

    
}