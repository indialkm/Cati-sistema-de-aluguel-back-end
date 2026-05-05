package com.cati.tcc.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.cati.tcc.dto.request.MidiaRequest;
import com.cati.tcc.mapper.MidiaMapper;
import com.cati.tcc.model.Midia;
import com.cati.tcc.repository.MidiaRepository;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.IOException;
import java.util.UUID;

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


	private final String DIRETORIO_RAIZ = "D:/programacao/TCC/fotos-tcc-cati/";

    // CENÁRIO A: O usuário mandou links de fora (JSON)
    public List<Midia> prepararMidias(List<MidiaRequest> requests) {
        if (requests == null || requests.isEmpty()) return new ArrayList<>();

        return requests.stream()
            .map(dto -> {
                Midia entity = midiaMapper.toEntity(dto);
                entity.definirMidia(entity.getUrl()); 
                return entity;
            })
            .toList();
    }

    // CENÁRIO B: O usuário fez upload de arquivos (Multipart)
    public List<Midia> salvarMidiasLocais(List<MultipartFile> arquivos) {
        if (arquivos == null || arquivos.isEmpty()) return new ArrayList<>();

        List<Midia> midiasSalvas = new ArrayList<>();
        for (MultipartFile arquivo : arquivos) {
            try {
                String nomeNovo = fazerUploadNoDisco(arquivo); 

                Midia midia = new Midia();
                midia.setNomeArquivo(arquivo.getOriginalFilename());
                midia.setUrl("imagens/" + nomeNovo);
                midia.definirMidia(midia.getUrl());

                midiasSalvas.add(midia);
            } catch (IOException e) {
                throw new RuntimeException("Erro ao salvar arquivo", e);
            }
        }
        return midiasSalvas;
    }


    private String fazerUploadNoDisco(MultipartFile arquivo) throws IOException {
        String nomeOriginal = arquivo.getOriginalFilename();
        String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
        String nomeNovo = UUID.randomUUID().toString() + extensao;
        
        Path caminhoDestino = Paths.get(DIRETORIO_RAIZ + nomeNovo);
        Files.copy(arquivo.getInputStream(), caminhoDestino);
        
        return nomeNovo;
    }
    
    public List<Midia> exibirMidia(UUID idEstoque){
 
    	return midiaRepository.buscaMidiasPorEstoque(idEstoque);
    	
    }
	
	
	
}
