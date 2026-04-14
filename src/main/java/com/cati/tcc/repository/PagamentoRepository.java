package com.cati.tcc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cati.tcc.model.Pagamento;

public interface PagamentoRepository extends JpaRepository<Pagamento, UUID> {

}
