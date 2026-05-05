package com.cati.tcc.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.cati.tcc.config.exceptions.NegocioException;
import com.cati.tcc.dto.request.ChecklistRequest;
import com.cati.tcc.mapper.ChecklistMapper;
import com.cati.tcc.model.Aluguel;
import com.cati.tcc.model.Checklist;
import com.cati.tcc.model.Midia;
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


	public Checklist entrada(Aluguel aluguel, ChecklistRequest request, List<MultipartFile> arquivos) {
        return criarChecklistComMidias(aluguel, request, TipoCheckList.ENTRADA, arquivos);
    }

    public Checklist saida(Aluguel aluguel, ChecklistRequest request, List<MultipartFile> arquivos) {
        return criarChecklistComMidias(aluguel, request, TipoCheckList.SAIDA, arquivos);
    }

    private Checklist criarChecklistComMidias(Aluguel aluguel, ChecklistRequest request, TipoCheckList tipo, List<MultipartFile> arquivos) {
        // 1. VALIDAÇÃO DE NEGÓCIO: Se não houver anexo, o sistema trava.
        if (arquivos == null || arquivos.isEmpty()) {
            throw new NegocioException("O checklist de " + tipo + " não pode ser salvo sem as fotos comprobatórias.");
        }

      
        Checklist checklist = checkMapper.toEntity(request);
        checklist.setTipoChecklist(tipo);
        checklist.setAluguel(aluguel);

       
        List<Midia> midiasSalvas = midiaService.salvarMidiasLocais(arquivos);
        
        // 4. Vincula cada mídia salva ao checklist
        midiasSalvas.forEach(checklist::adicionarMidia);

        // 5. Persistência final
        return checkRepository.save(checklist);
    }
}
	
	

