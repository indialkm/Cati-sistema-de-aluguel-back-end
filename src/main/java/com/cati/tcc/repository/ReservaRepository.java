package com.cati.tcc.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.Reserva;
import com.cati.tcc.model.enums.StatusEquipamento;
import com.cati.tcc.model.enums.StatusReserva;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Repository
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {
	
	@Query("SELECT r FROM Reserva r WHERE r.estoque.id = :idEstoque AND r.dataFinal > :agora")
    List<Reserva> findByEstoqueIdAndDataFinalAfter(
        @Param("idEstoque") UUID idEstoque, 
        @Param("agora") LocalDateTime agora
    );
	
	@Query("SELECT r FROM Reserva r WHERE r.estoque.id = :idEstoque " +
		       "AND r.dataFinal > :agora " +
		       "AND r.disponibilidade IN :status")
		List<Reserva> buscarReservasBloqueantes(
		    @Param("idEstoque") UUID idEstoque, 
		    @Param("agora") LocalDateTime agora, 
		    @Param("status") List<StatusReserva> status
		);

	List<Reserva> findByDisponibilidadeAndAuditoriaReservaBefore(StatusReserva aguardandoPagamento,
			LocalDateTime instanteCorte);
	
	@Query("SELECT COUNT(r) FROM Reserva r " +
		       "WHERE r.estoque.id = :id " +
		       "AND r.disponibilidade IN :statusBloqueantes " + 
		       "AND r.dataInicial < :fim AND r.dataFinal > :inicio")
		long contarReservasConflitantes(
		    @Param("id") UUID id, 
		    @Param("inicio") LocalDateTime inicio, 
		    @Param("fim") LocalDateTime fim,
		    @Param("statusBloqueantes") List<StatusReserva> statusBloqueantes
		);
}