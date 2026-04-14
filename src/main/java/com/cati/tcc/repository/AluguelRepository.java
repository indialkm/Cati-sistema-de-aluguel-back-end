package com.cati.tcc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cati.tcc.model.Aluguel;

public interface AluguelRepository extends JpaRepository<Aluguel, UUID> {

}
