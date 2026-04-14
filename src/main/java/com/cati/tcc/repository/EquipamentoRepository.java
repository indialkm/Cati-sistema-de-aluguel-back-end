package com.cati.tcc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.cati.tcc.model.Equipamento;

@Repository
public interface EquipamentoRepository extends JpaRepository<Equipamento, UUID>{

}
