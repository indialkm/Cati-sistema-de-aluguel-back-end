package com.cati.tcc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cati.tcc.model.Midia;

public interface MidiaRepository extends JpaRepository<Midia, UUID> {

}
