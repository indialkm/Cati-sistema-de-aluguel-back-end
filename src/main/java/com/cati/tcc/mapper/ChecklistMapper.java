package com.cati.tcc.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.dto.response.ChecklistResponse;
import com.cati.tcc.dto.response.MidiaResponse;
import com.cati.tcc.model.Checklist;
import com.cati.tcc.model.Midia;

@Component
public class ChecklistMapper {
	
	private final MidiaMapper midiaMapper;
	
	
	
	
	public ChecklistMapper(MidiaMapper midiaMapper) {
		this.midiaMapper = midiaMapper;
	}


	public ChecklistResponse toResponse(Checklist checklist) {
		
		List<MidiaResponse> midias = checklist.getMidias().stream()
				.map(i -> midiaMapper.toResponse(i))
				.toList();
		
		return new ChecklistResponse(
				
				checklist.getId(),
				checklist.getObservacoes(),
				midias,
				checklist.getDataRegistro()
				
				);
		
		
	}
	
	
	public Checklist toEntity(ChecklistRequest request) {
	
		Checklist check = new Checklist();
		
		check.setObservacoes(request.observacoes());
		check.setUrlContrato(request.urlContrato());
		
		return check;
		
	}

}
