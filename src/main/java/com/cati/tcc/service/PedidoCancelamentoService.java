package com.cati.tcc.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.enums.StatusPedido;
import com.cati.tcc.repository.PedidoRepository;

import jakarta.transaction.Transactional;

@Service
public class PedidoCancelamentoService {

    private final PedidoRepository pedidoRepository;
    private final EstoqueService estoqueService;

    public PedidoCancelamentoService(PedidoRepository pedidoRepository, EstoqueService estoqueService) {
        this.pedidoRepository = pedidoRepository;
        this.estoqueService = estoqueService;
    }

    // Roda a cada 30 minutos (valor em milissegundos)
    @Scheduled(fixedRate = 1800000) 
    @Transactional
    public void cancelarPedidosExpirados() {
        LocalDateTime limite = LocalDateTime.now().minusHours(5);
        
        // 1. Busca pedidos que ainda estão "AGUARDANDO_PAGAMENTO" e foram criados há mais de 5h
        List<Pedido> pedidosExpirados = pedidoRepository
            .findByStatusAndDataPedidoBefore(StatusPedido.AGUARDANDO_PAGAMENTO, limite);

        for (Pedido pedido : pedidosExpirados) {
            // 2. Muda o status para CANCELADO
            pedido.setStatus(StatusPedido.CANCELADO);
            
            // 3. DEVOLVE OS ITENS AO ESTOQUE
            // Percorre os itens do pedido e avisa o estoque que eles estão livres
            pedido.getItensPedidos().forEach(item -> {
                estoqueService.retornarEstoque(item.getEstoque().getId(), 1);
            });
            
            pedidoRepository.save(pedido);
            System.out.println("Pedido " + pedido.getId() + " cancelado por falta de pagamento.");
        }
    }
}
