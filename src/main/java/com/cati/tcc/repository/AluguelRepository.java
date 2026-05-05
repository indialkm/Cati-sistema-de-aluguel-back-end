package com.cati.tcc.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.Checklist;
import com.cati.tcc.model.Pedido;
import com.cati.tcc.model.User;
import com.cati.tcc.model.enums.StatusAluguel;

public interface AluguelRepository extends JpaRepository<Aluguel, UUID> {

	Page<List<Aluguel>> findByUser(User user, Pageable pageable); 
	Optional<List<Aluguel>> findByIdPedido(UUID idPedido);
	Page<Aluguel> findByStatusIn(List<StatusAluguel> status, Pageable pageable);
	
	@Query("SELECT a.checklistEntrada FROM Aluguel a WHERE a.id = :idAluguel")
	Optional<Checklist> buscarChecklistEntradaPorAluguelId(@Param("idAluguel") UUID idAluguel);
	
	@Query("SELECT a.checklistSaida FROM Aluguel a WHERE a.id = :idAluguel")
	Optional<Checklist> buscarChecklistSaidaPorAluguelId(@Param("idAluguel") UUID idAluguel);

}
