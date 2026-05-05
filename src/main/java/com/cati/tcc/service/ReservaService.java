package com.cati.tcc.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.dto.request.ReservaRequest;
import com.cati.tcc.dto.response.PeriodoBloqueadoResponse;
import com.cati.tcc.mapper.ReservaMapper;
import com.cati.tcc.model.Equipamento;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.PeriodoBloqueado;
import com.cati.tcc.model.Reserva;
import com.cati.tcc.model.enums.StatusEquipamento;
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
	   
	    List<Estoque> disponiveis = verificarDisponibilidadeParaReserva(
	        request.idEstoque(), 
	        request.dataInicial(), 
	        request.dataFinal()
	    );

	    if (disponiveis.isEmpty()) {
	        throw new ResponseStatusException(HttpStatus.CONFLICT, "Equipamento indisponível ou em manutenção.");
	    }

	    Estoque estoque = disponiveis.get(0);

	    Equipamento equipamentoReal = estoque.getEquipamentos().stream()
	            .filter(eq -> eq.getStatusEquipamento() == StatusEquipamento.DISPONIVEL)
	            .findFirst()
	            .orElseThrow(() -> new ResponseStatusException(HttpStatus.CONFLICT, "Nenhum item físico pronto para uso."));

	   
	    Reserva reserva = reservaMapper.toEntity(request);
	    reserva.setEstoque(estoque);
	    reserva.setDisponibilidade(StatusReserva.AGUARDANDO_PAGAMENTO);
	    reserva.setAuditoriaReserva(LocalDateTime.now());

	    return reservaRepository.save(reserva);
	}
	
	public List<Estoque> verificarDisponibilidadeParaReserva(UUID id, LocalDateTime inicio, LocalDateTime fim) {
     
		List<StatusReserva> bloqueantes = List.of(
            StatusReserva.ALUGADA
        );

        
        long totalOperacional = estoqueService.contarItensOperacionais(id);
        long totalReservado = reservaRepository.contarReservasConflitantes(id, inicio, fim, bloqueantes);

      
        if (totalOperacional > totalReservado) {
            return List.of(estoqueService.buscarPorId(id));
        }

        return List.of();
    }
	
	
    @Transactional
    public Reserva buscarPorId(UUID id) {
    	Reserva reserva = reservaRepository.findById(id)
    		.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reserva não encontrada"));
    		return reserva;		
   
    }

   
    public List<PeriodoBloqueado> calcularDatasBloqueadas(UUID idEstoque) {
        Estoque estoque = estoqueService.buscarId(idEstoque);
        List<Equipamento> equipamentos = estoque.getEquipamentos();
        int totalEquipamentos = equipamentos.size();

    
        List<StatusReserva> statusQueOcupamEstoque = List.of(
            StatusReserva.ALUGADA
        );

        
        List<Reserva> reservasAtivas = reservaRepository.buscarReservasBloqueantes(
            idEstoque, 
            LocalDateTime.now(), 
            statusQueOcupamEstoque
        );

        Map<LocalDate, Integer> ocupacaoDiaria = new HashMap<>();

        for (Reserva r : reservasAtivas) {
            LocalDate data = r.getDataInicial().toLocalDate();
            while (!data.isAfter(r.getDataFinal().toLocalDate())) {
                ocupacaoDiaria.put(data, ocupacaoDiaria.getOrDefault(data, 0) + 1);
                data = data.plusDays(1);
            }
        }

        return ocupacaoDiaria.entrySet().stream()
            .filter(entry -> entry.getValue() >= totalEquipamentos)
            .map(entry -> new PeriodoBloqueado(entry.getKey().atStartOfDay(), entry.getKey().atTime(23, 59)))
            .toList();
    }
    
    // Metodo para ajudar no Schedule
    @Transactional
    public int limparReservasAguardandoPagamento(int horasLimite) {
        
        LocalDateTime instanteCorte = LocalDateTime.now().minusHours(horasLimite);
        
        
        List<Reserva> expiradas = reservaRepository.findByDisponibilidadeAndAuditoriaReservaBefore(
            StatusReserva.AGUARDANDO_PAGAMENTO,
            instanteCorte
        );

        if (!expiradas.isEmpty()) {
            int quantidade = expiradas.size();
            reservaRepository.deleteAll(expiradas); 
            return quantidade;
        }
        
        return 0;
    }
    
    
   
}