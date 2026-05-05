package com.cati.tcc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cati.tcc.model.ItemPedido;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.User;
import com.cati.tcc.model.enums.StatusPedido;

public interface PedidoRepository extends JpaRepository<Pedido, UUID> {
	
	Page<ItemPedido> findById(UUID pedidoId, Pageable pageable);
	
	List<Pedido> findByStatusAndDataPedidoBefore(StatusPedido status, LocalDateTime data);
	
	Page<Pedido> findByUser(User user, Pageable pageable);
	
	Page<Pedido> findAll(Pageable pageable);
	
	@Query("SELECT p FROM Pedido p WHERE (:status IS NULL OR p.status = :status)")
	Page<Pedido> findPedidosCustom(StatusPedido status, Pageable pageable);


}
