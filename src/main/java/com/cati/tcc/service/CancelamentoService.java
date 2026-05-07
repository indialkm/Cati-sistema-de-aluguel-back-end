package com.cati.tcc.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.Pagamento;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.Reserva;
import com.cati.tcc.model.enums.StatusAluguel;
import com.cati.tcc.model.enums.StatusPedido;
import com.cati.tcc.model.enums.StatusReserva;
import com.cati.tcc.repository.ReservaRepository;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CancelamentoService {

    private final PedidoService pedidoService;
    private final PagamentoService pagamentoService;
    private final AluguelService aluguelService;
    private final HistoricoTransacaoService historicoService;

	public CancelamentoService(PedidoService pedidoService, PagamentoService pagamentoService,
			AluguelService aluguelService, HistoricoTransacaoService historicoService) {
		this.pedidoService = pedidoService;
		this.pagamentoService = pagamentoService;
		this.aluguelService = aluguelService;
		this.historicoService = historicoService;
	}

	@Transactional
    public String executarCancelamentoTotal(UUID pedidoId) {
		
        Pedido pedido = pedidoService.buscarPorId(pedidoId);

        if (StatusPedido.CANCELADO.equals(pedido.getStatus())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Este pedido já foi cancelado anteriormente.");
        }
        
        if (pedido.getStatus() == StatusPedido.AGUARDANDO_PAGAMENTO) {
            pedidoService.cancelarPedidoSemPagamento(pedidoId);
            
            historicoService.registrarMudanca(
            	    pedido, 
            	    "Pedido pendente cancelado com sucesso."
            	);
            
            return "Pedido pendente cancelado com sucesso.";
        }
        
         
        if (StatusPedido.PAGO.equals(pedido.getStatus())) {
            
        	Pagamento pagamento = pagamentoService.buscarPorpedido(pedidoId);
            
            pagamentoService.realizarEstorno(pagamento.getId());
            
            if (pedido.getItensPedidos() != null) {
                pedido.getItensPedidos().forEach(item -> {
                    if (item.getReserva() != null) {
                        item.getReserva().setDisponibilidade(StatusReserva.CANCELADO);
                    }
                });
            }
            
            List<Aluguel> alugueis = aluguelService.alugueisPorPedido(pedidoId);
            alugueis.forEach(a -> {
                a.setStatus(StatusAluguel.CANCELADO);
                aluguelService.atualizarAlugueis(a);
            });
            
            
            // 5. Atualiza o Status do Pedido
            pedido.setStatus(StatusPedido.CANCELADO);
            pedidoService.atualizarPedido(pedido); 
            
            historicoService.registrarMudanca(
            	    pedido, 
            	    "Pedido cancelado e estorno realizado com sucesso via Stripe."
            	);
           
            return "Pedido cancelado e estorno realizado com sucesso via Stripe.";
            
            
        }
        
        return "Pedido cancelado.";
    }
}