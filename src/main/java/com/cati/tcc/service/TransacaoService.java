package com.cati.tcc.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.dto.response.PagamentoResponse;
import com.cati.tcc.mapper.PagamentoMapper;
import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.Carrinho;
import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.Pagamento;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusAluguel;
import com.cati.tcc.model.enums.StatusPagamento;
import com.cati.tcc.model.enums.StatusPedido;
import com.cati.tcc.model.enums.StatusReserva;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;

@Service
public class TransacaoService {
	
	private final CarrinhoService carrinhoService;
    private final PedidoService pedidoService;
    private final PagamentoService pagamentoService;
    private final PagamentoMapper pagamentoMapper;
    private final AluguelService aluguelService;
    private final AuthService authService;
    private final HistoricoTransacaoService historicoService;



	public TransacaoService(CarrinhoService carrinhoService, PedidoService pedidoService,
			PagamentoService pagamentoService, PagamentoMapper pagamentoMapper, AluguelService aluguelService,
			AuthService authService, HistoricoTransacaoService historicoService) {
		this.carrinhoService = carrinhoService;
		this.pedidoService = pedidoService;
		this.pagamentoService = pagamentoService;
		this.pagamentoMapper = pagamentoMapper;
		this.aluguelService = aluguelService;
		this.authService = authService;
		this.historicoService = historicoService;
	}


	@Transactional
    public Pedido finalizarLocacao() {
        
        UUID userId = authService.getAuthenticatedUserId();
    
        Carrinho carrinho = carrinhoService.buscarCarrinhoAtivo();

        if (carrinho.getItens().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carrinho vazio!");
        }

        Pedido novoPedido = pedidoService.gerarPedidoDeCarrinho(carrinho);
 
        carrinhoService.inativarCarrinho(carrinho.getId());

        return novoPedido;
    }
    
   
	@Transactional
	public Pagamento finalizarCheckout(PagamentoRequest request) {
	    
	   
	    Pedido pedido = pedidoService.buscarPorId(request.idPedido());
	    Optional<Pagamento> pagamentoExistente = pagamentoService.buscarOpcionalPorPedidoId(pedido.getId());

	    if (pagamentoExistente.isPresent()) {
	        Pagamento pag = pagamentoExistente.get();
	      
	        if (pag.getStatus() == StatusPagamento.APROVADO) {
	            throw new ResponseStatusException(
	                HttpStatus.BAD_REQUEST, 
	                "Pagamento já processado: O pedido #" + pedido.getId() + " já foi pago com sucesso."
	            );
	        }
  
	        return pag; 
	    }
	   
	    return pagamentoService.iniciarPagamento(request);
	}
    
 	
    @Transactional
    public List<Aluguel> gerarAlugueisTeste(UUID idPedido) {

       
        Pedido pedido = pedidoService.buscarPorId(idPedido);

        pedido.getItensPedidos().forEach(item -> {
            item.getReserva().setDisponibilidade(StatusReserva.ALUGADA);
        });
 
       return aluguelService.gerarAlugueisParaPedidoTeste(pedido);
        
        
    }
    
    @Transactional
    public void confirmarPagamentoEGerarAluguel(String stripePaymentId) {
        
    	try {
    	PaymentIntent intent = PaymentIntent.retrieve(stripePaymentId);
    	
    	if (!"succeeded".equals(intent.getStatus())) {
            throw new RuntimeException("O pagamento ainda não foi confirmado no Stripe!");
        }
    	
        Pagamento pagamento = pagamentoService.buscarPorExternalId(stripePaymentId);

        if (pagamento.getStatus() == StatusPagamento.APROVADO) {
            return; 
        }

     
        pagamento.setStatus(StatusPagamento.APROVADO);
        pagamentoService.pagamentoAprovado(pagamento);
        pagamento.setClientSecret(null);
        pagamento.setDataPagamento(LocalDateTime.now());
        
        
        Pedido pedido = pagamento.getPedido();
        pedido = pedidoService.fecharPedido(pedido.getId());
        
  
        pedido.getItensPedidos().forEach(item -> {
            item.getReserva().setDisponibilidade(StatusReserva.ALUGADA);
        });
        

        aluguelService.gerarAlugueisParaPedido(pedido);
      
        // método do histórico
        historicoService.registrarMudanca(
         	    pedido, 
         	    "Pagamento feito com sucesso via Stripe."
         	);
        
        
    	}catch (StripeException e) {
            throw new RuntimeException("Erro ao validar pagamento no Stripe: " + e.getMessage());
        }
    
    }}
    
 
	
