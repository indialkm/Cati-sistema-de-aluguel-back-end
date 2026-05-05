package com.cati.tcc.config.utils;

import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.cati.tcc.model.Reserva;
import com.cati.tcc.model.enums.StatusReserva;
import com.cati.tcc.repository.CarrinhoRepository;
import com.cati.tcc.repository.ReservaRepository;
import com.cati.tcc.service.CarrinhoService;
import com.cati.tcc.service.ReservaService;

import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
@EnableScheduling
public class SchedulingService {

    private static final Logger logger = LoggerFactory.getLogger(SchedulingService.class);
    private final ReservaService reservaService;

    public SchedulingService(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    // Roda a cada 15 minutos para manter o estoque fresco
    @Scheduled(fixedDelay = 900000) 
    public void verificarReservasExpiradas() {
        logger.info("Iniciando rotina de limpeza de reservas vencidas...");
        
        try {
           
            int removidas = reservaService.limparReservasAguardandoPagamento(5);
            
            if (removidas > 0) {
                logger.info("Sucesso: {} reservas expiradas foram removidas do sistema.", removidas);
            } else {
                logger.info("Nenhuma reserva expirada encontrada nesta rodada.");
            }
        } catch (Exception e) {
            logger.error("Erro ao executar a limpeza de reservas: {}", e.getMessage());
        }
    }
}