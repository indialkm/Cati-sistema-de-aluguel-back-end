package com.cati.tcc.mapper;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import com.cati.tcc.dto.request.MidiaRequest;
import com.cati.tcc.dto.response.MidiaResponse;
import com.cati.tcc.model.Midia;

@Component
public class MidiaMapper {

    // No TCC, explique que usamos @Value para não "chumbar" a URL no código.
    // Se você mudar de porta ou subir para produção, altera apenas no application.properties.
    @Value("${app.api.base-url:http://localhost:8080/cati/api}")
    private String serverUrl;

    /**
     * Converte a Entidade para Response (Saída para o Frontend)
     */
    public MidiaResponse toResponse(Midia midia) {
        if (midia == null) return null;

        return new MidiaResponse(
            midia.getId(),
            construirUrlCompleta(midia.getUrl()), // Transforma /imagens/xxx em http://localhost:8080/imagens/xxx
            midia.getNomeArquivo(),
            midia.getTipo()
        );
    }

    /**
     * Converte o Request para Entidade (Persistência no Banco)
     */
    public Midia toEntity(MidiaRequest request) {
        if (request == null) return null;

        Midia midia = new Midia();
        midia.setUrl(request.url());
        midia.setNomeArquivo(request.nomeArquivo());
        // O tipo e a lógica de definição ficam na Model ou Service
        return midia;
    }

    /**
     * Método auxiliar para garantir que o Frontend receba um link funcional.
     */
    private String construirUrlCompleta(String urlOriginal) {
        if (urlOriginal == null) return null;
        
        // Se já for uma URL completa (ex: link externo), retorna ela mesma
        if (urlOriginal.startsWith("http")) {
            return urlOriginal;
        }

        // Garante que o caminho comece com /
        String path = urlOriginal.startsWith("/") ? urlOriginal : "/" + urlOriginal;
        
        return serverUrl + path;
    }
}