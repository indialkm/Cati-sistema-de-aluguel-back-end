package com.cati.tcc.service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.UUID;

@Service
public class ArquivoService {

    private final String CAMINHO_PASTA = "D:/programacao/TCC/fotos-tcc-cati/";

    public String salvarArquivo(MultipartFile arquivo) {
        try {
            String nomeOriginal = arquivo.getOriginalFilename();
            String extensao = nomeOriginal.substring(nomeOriginal.lastIndexOf("."));
            String novoNome = UUID.randomUUID().toString() + extensao;

            Path destino = Paths.get(CAMINHO_PASTA + novoNome);
            Files.copy(arquivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);

            return novoNome; 
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo", e);
        }
    }
    
    public void deletarArquivoFisico(String nomeArquivo) {
        try {
            Path caminho = Paths.get("D:/programacao/TCC/fotos-tcc-cati/" + nomeArquivo);
            Files.deleteIfExists(caminho);
        } catch (IOException e) {
         
            System.err.println("Aviso: Não foi possível remover o arquivo físico: " + nomeArquivo);
        }
    }
}