package com.cati.tcc.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import com.cati.tcc.config.exceptions.NegocioException;
import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.mapper.ChecklistMapper;
import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.Checklist;
import com.cati.tcc.model.enums.StatusAluguel;
import com.cati.tcc.model.enums.TipoCheckList;
import com.cati.tcc.repository.ChecklistRepository;

@Service
public class ChecklistService {
	
	private final ChecklistMapper checkMapper;
	private final ChecklistRepository checkRepository;
	
	private final MidiaService midiaService;
	

	public ChecklistService(ChecklistMapper checkMapper, ChecklistRepository checkRepository, MidiaService midiaService) {
		this.checkMapper = checkMapper;
		this.checkRepository = checkRepository;
		this.midiaService = midiaService;
		
	}


	public Checklist entrada(ChecklistRequest request) {
		
		Checklist checklist = checkMapper.toEntity(request);
		checklist.setMidias(midiaService.criarMidia(request.midias()));
		checklist.setTipoChecklist(TipoCheckList.ENTRADA);
		
		checkRepository.save(checklist);
		return checklist;
	}
	
	
	public Checklist saida(ChecklistRequest request) {

		Checklist checklist = checkMapper.toEntity(request);
		checklist.setMidias(midiaService.criarMidia(request.midias()));
		checklist.setTipoChecklist(TipoCheckList.SAIDA);
		
		checkRepository.save(checklist);
		return checklist;
	}
	
	

}
