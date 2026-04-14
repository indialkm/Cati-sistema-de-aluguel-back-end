package com.cati.tcc.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.cati.tcc.model.Avaliacao;

public interface AvaliacaoRepository extends JpaRepository<Avaliacao, Long> {
	
	Page<Avaliacao> findByEstoque_Id(UUID idEstoque, Pageable pageable);

	@Query("SELECT AVG(a.nota) FROM Avaliacao a WHERE a.estoque.id = :idEstoque")
    Double getMediaDasNotas(UUID idEstoque);
}
