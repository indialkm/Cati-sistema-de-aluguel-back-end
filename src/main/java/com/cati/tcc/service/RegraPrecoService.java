package com.cati.tcc.service;

import com.cati.tcc.model.ItemCarrinho;
import com.cati.tcc.model.Estoque;
import com.cati.tcc.model.Reserva;
import com.cati.tcc.model.enums.TipoEstoque;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegraPrecoService {

	//Calcular o total do carrinho
    public Double calcularTotalCarrinho(List<ItemCarrinho> itens) {
        
    	if (itens == null || itens.isEmpty()) {
            return 0.0;
        }
        return itens.stream()
                    .mapToDouble(this::calcularSubtotalItem)
                    .sum();
    }

    
    //Calcular cada item
    public Double calcularSubtotalItem(ItemCarrinho item) {
        Estoque estoque = item.getEstoque();
        Reserva reserva = item.getReserva();

        
        if (estoque == null || reserva == null) {
            return 0.0;
        }

        double precoBase = estoque.getPrecoBase();
      
        Long dias = reserva.getQuantidadeDiasAluguel();

        // Unitario
        if (estoque.getTipoEstoque() == TipoEstoque.UNITARIO) {
            
            return (precoBase * item.getEstoque().getQuantidade()) * dias;
        }

        // Metragem
        if (estoque.getTipoEstoque() == TipoEstoque.METRAGEM) {
            
            double m2 = item.getEstoque().getAltura() * item.getEstoque().getLargura();
            double valorArea = m2 * precoBase;
            double taxaAdicional = precoBase * 0.2;

            return valorArea + (taxaAdicional * dias);
        }

        return 0.0;
    }
    
    
}