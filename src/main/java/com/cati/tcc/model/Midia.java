package com.cati.tcc.model;

import java.util.UUID;

import com.cati.tcc.config.exceptions.NegocioException;
import com.cati.tcc.model.enums.TipoMidia;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter @NoArgsConstructor
@Entity(name="tb_midias")
public class Midia {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String url; 
    private String nomeArquivo;
    
    @Enumerated(EnumType.STRING)
    private TipoMidia tipo; 

    @ManyToOne
    @JoinColumn(name = "checklist_id")
    private Checklist checklist;
    
    @ManyToOne
    @JoinColumn(name = "estoque_id") 
    private Estoque estoque;
    
    
    public void definirMidia(String url) {
        if (url == null || !url.contains(".")) {
            throw new NegocioException("URL inválida ou sem extensão.");
        }

        String urlLimpa = url.split("\\?")[0];
        String extensao = urlLimpa.substring(urlLimpa.lastIndexOf(".") + 1).toLowerCase();

        switch (extensao) {
            case "jpg", "jpeg", "png", "webp" -> this.tipo = TipoMidia.FOTO;
            case "mp4", "mkv", "mov", "avi" -> this.tipo = TipoMidia.VIDEO;
            default -> throw new NegocioException("Formato de arquivo '." + extensao + "' não é suportado pelo sistema.");
        }
    }
    
    
}
