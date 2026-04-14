package com.cati.tcc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.MidiaRequest;
import com.cati.tcc.mapper.MidiaMapper;
import com.cati.tcc.model.Midia;
import com.cati.tcc.repository.MidiaRepository;

@Service
public class MidiaService {

	private final MidiaMapper midiaMapper;
	private final MidiaRepository midiaRepository;
	private final AuthService auth;
	
	
	public MidiaService(MidiaMapper midiaMapper, MidiaRepository midiaRepository, AuthService auth) {
		this.midiaMapper = midiaMapper;
		this.midiaRepository = midiaRepository;
		this.auth = auth;
	}


	public List<Midia> criarMidia(List<MidiaRequest> request)
	{
		
		
		return request.stream()
			    .map(dto -> {
			        Midia entity = midiaMapper.toEntity(dto);
			        entity.definirMidia(entity.getUrl());
			        return entity;
			    })
			    .toList();
		
	}
	
	
	
}
