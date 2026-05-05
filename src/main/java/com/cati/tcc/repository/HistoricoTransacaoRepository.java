package com.cati.tcc.repository;

import java.util.UUID;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.cati.tcc.model.HistoricoTransacao;

public interface HistoricoTransacaoRepository extends JpaRepository<HistoricoTransacao, UUID> {
	
	// 1. FORMA LIMPA (Usando o campo 'user' que você acabou de criar)
    Page<HistoricoTransacao> findByUserIdOrderByDataEventoDesc(UUID userId, Pageable pageable);

    // 2. FORMA COM QUERY (Se você preferir deixar explícito, use esta e apague a de cima)
    @Query("SELECT h FROM HistoricoTransacao h WHERE h.user.id = :usuarioId")
    Page<HistoricoTransacao> buscarPorUsuarioId(@Param("usuarioId") UUID usuarioId, Pageable pageable);

    // 3. BUSCA GERAL (Para a Empresa)
    Page<HistoricoTransacao> findAllByOrderByDataEventoDesc(Pageable pageable);
	
}
