package com.cati.tcc.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.cati.tcc.model.Equipamento;
import com.cati.tcc.model.Estoque;

@Repository
public interface EstoqueRepository extends JpaRepository<Estoque, UUID>{
	
	Optional<Estoque> findByNome(String nome);
	
	Boolean existsByNome(String nome);
	
	@Query("SELECT e FROM Estoque e WHERE e.id = :id " + 
		       "AND (e.quantidade > (SELECT COUNT(r) FROM Reserva r " +
		       "WHERE r.estoque.id = e.id " +
		       "AND r.dataInicial < :fim AND r.dataFinal > :inicio))")
		List<Estoque> buscarEstoquesDisponiveis( 
		    @Param("id") UUID id, 
		    @Param("inicio") LocalDateTime inicio, 
		    @Param("fim") LocalDateTime fim
		);
	
	@Query("SELECT e FROM Estoque e WHERE " +
	           "(:categoria IS NULL OR e.categoria = :categoria) AND " +
	           "(:modelo IS NULL OR e.nome LIKE %:modelo%) AND " +
	           "(:largura IS NULL OR e.largura = :largura) AND " +
	           "(:largura IS NULL OR e.largura = :largura) AND " +
	           "(e.quantidade >= :qtdMinima)")
	    List<Estoque> buscarComFiltrosOpcionais(
	        String categoria, 
	        String modelo, 
	        Double largura, 
	        int qtdMinima
	    );
	
	// Mude de findByCategoriaNomeIgnoreCase para:
	Page<Estoque> findByCategoriaContainingIgnoreCase(String categoria, Pageable pageable);
	
	Page<Estoque> findAll(Pageable pageable);

	@Query("SELECT e FROM Estoque e LEFT JOIN FETCH e.equipamentos WHERE e.id = :id")
	Optional<Estoque> findByIdWithEquipamentos(@Param("id") UUID id);
	
	@Query("SELECT e FROM Estoque s JOIN s.equipamentos e WHERE s.id = :estoqueId")
    Page<Equipamento> buscarEquipamentosPorEstoqueId(@Param("estoqueId") UUID estoqueId, Pageable pageable);
}
	


