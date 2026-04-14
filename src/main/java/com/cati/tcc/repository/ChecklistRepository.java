package com.cati.tcc.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.cati.tcc.model.Checklist;

public interface ChecklistRepository extends JpaRepository<Checklist, UUID> {

}
