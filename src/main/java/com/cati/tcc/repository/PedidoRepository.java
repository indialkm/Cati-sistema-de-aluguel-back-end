package com.cati.tcc.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.Pedido;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
	
	Page<ItemPedido> findById(UUID pedidoId, Pageable pageable);


}
