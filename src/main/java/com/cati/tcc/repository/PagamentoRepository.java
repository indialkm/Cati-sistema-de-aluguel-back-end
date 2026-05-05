package com.cati.tcc.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cati.tcc.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {
	
	Optional<Pagamento> findBytransacaoGatewayId(String transacaoGatewayId);
	
	Optional<Pagamento> findByPedidoId(UUID idPedido);

}
