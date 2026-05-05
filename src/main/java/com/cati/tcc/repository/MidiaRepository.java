package com.cati.tcc.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cati.tcc.model.Midia;

public interface MidiaRepository extends JpaRepository<Midia, UUID> {
	
	@Query("SELECT m FROM tb_midias m WHERE m.estoque.id = :idEstoque")
    List<Midia> buscaMidiasPorEstoque(@Param("idEstoque") UUID idEstoque);

}
