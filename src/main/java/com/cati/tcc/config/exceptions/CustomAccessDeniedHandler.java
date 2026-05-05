package com.cati.tcc.config.exceptions;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);

        Map<String, Object> data = new LinkedHashMap<>();
        data.put("timestamp", LocalDateTime.now().toString());
        data.put("status", 403);
        data.put("erro", "Acesso Negado");
        data.put("mensagem", "Você não tem permissão para realizar esta operação. Somente administradores (OWNER) podem acessar este recurso.");
        data.put("path", request.getRequestURI());

        try (OutputStream out = response.getOutputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(out, data);
            out.flush();
        }
    }
}