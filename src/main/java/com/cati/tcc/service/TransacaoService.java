package com.cati.tcc.service;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import com.cati.tcc.dto.request.PagamentoRequest;
import com.cati.tcc.dto.response.PagamentoResponse;
import com.cati.tcc.mapper.PagamentoMapper;
import com.cati.tcc.model.Carrinho;
import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.Pagamento;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusPagamento;
import com.cati.tcc.model.enums.StatusPedido;

@Service
public class TransacaoService {
	
	private final CarrinhoService carrinhoService;
    private final PedidoService pedidoService;
    private final PagamentoService pagamentoService;
    private final PagamentoMapper pagamentoMapper;
    private final AluguelService aluguelService;
    private final AuthService authService;
	


	public TransacaoService(CarrinhoService carrinhoService, PedidoService pedidoService,
			PagamentoService pagamentoService, PagamentoMapper pagamentoMapper, AluguelService aluguelService,
			AuthService authService) {
		this.carrinhoService = carrinhoService;
		this.pedidoService = pedidoService;
		this.pagamentoService = pagamentoService;
		this.pagamentoMapper = pagamentoMapper;
		this.aluguelService = aluguelService;
		this.authService = authService;
	}


	@Transactional
    public Pedido finalizarLocacao() {
        
        UUID userId = authService.getAuthenticatedUserId();

        // 2. Pega o carrinho "aberto" desse usuário
        Carrinho carrinho = carrinhoService.buscarCarrinhoAtivo(userId);

        if (carrinho.getItens().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Carrinho vazio!");
        }

        // 3. O CORAÇÃO: Transforma Carrinho em Pedido
        // Aqui você chama o pedidoService para criar o registro oficial
        Pedido novoPedido = pedidoService.gerarPedidoDeCarrinho(carrinho);
        

        // 4. "Fecha" o carrinho para que o usuário comece um novo do zero
        carrinhoService.inativarCarrinho(carrinho.getId());

        // 5. Retorna o pedido para o Controller mostrar o "Sucesso" na tela
        return novoPedido;
    }
    
   
    @Transactional
    public Pagamento finalizarCheckout(PagamentoRequest request) {
        
        // 1. Recupera o Pedido (que já deve estar no banco como PENDENTE)
        Pedido pedido = pedidoService.buscarPorId(request.idPedido());

        // 2. Tenta realizar o pagamento
        // Aqui o PagamentoService faz o toEntity, chama o gateway e salva o Pagamento
        Pagamento pagamento = pagamentoService.processar(request, pedido);

        List<ItemPedido> itens = pedido.getItensPedidos();
        // 3. A GRANDE SACADA: 
        // Se o pagamento for aprovado, o maestro (Checkout) manda o AluguelService agir
        if (pagamento.getStatus() == StatusPagamento.APROVADO) {
            aluguelService.gerarAlugueisParaPedido(itens);
            pedidoService.atualizarStatus(pedido.getId(), StatusPedido.PAGO);
        }

        // 4. Retorna a resposta (Convertendo a entidade pagamento para Response)
        return pagamento;
    }
    
	

}
