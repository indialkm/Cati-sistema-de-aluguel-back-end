package com.cati.tcc.model;

import java.util.UUID;

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

@Entity
@Getter @Setter @NoArgsConstructor
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
    
    
    public void definirMidia(String url) {
        if (url == null || !url.contains(".")) {
            this.tipo = TipoMidia.DESCONHECIDO;
        }

       
        String urlLimpa = url.split("\\?")[0];

        
        String extensao = urlLimpa.substring(urlLimpa.lastIndexOf(".") + 1).toLowerCase();

        
        switch (extensao) {
            case "jpg":
            case "jpeg":
            case "png":
            case "webp":
               this.tipo = TipoMidia.FOTO;
               break;
            case "mp4":
            case "mkv":
            case "mov":
            case "avi":
               this.tipo = TipoMidia.VIDEO;
               break;
            default:
                this.tipo = TipoMidia.DESCONHECIDO;
                break;
        }
    }
}
