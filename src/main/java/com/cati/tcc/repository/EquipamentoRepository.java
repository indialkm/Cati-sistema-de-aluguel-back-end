package com.cati.tcc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.cati.tcc.model.Equipamento;
import com.cati.tcc.model.enums.StatusEquipamento;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, UUID>{
	// Pesquisar por nome (Contendo a palavra, ignorando case) com paginação
	Page<Equipamento> findByModeloContainingIgnoreCase(String modelo, Pageable pageable);

    // O nome do método deve seguir o campo: statusEquipamento
    Page<Equipamento> findByStatusEquipamento(StatusEquipamento status, Pageable pageable);
    
    Page<Equipamento> findByEstoqueId(UUID estoqueId, Pageable pageable);

    
}
