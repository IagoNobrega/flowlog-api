package com.iago.flowlog.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.OptionalDouble;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.iago.flowlog.dto.FocusRecordRequest;
import com.iago.flowlog.dto.ProductivityDiagnosisResponse;
import com.iago.flowlog.model.FocusSession;
import com.iago.flowlog.repository.FocusSessionRepository;

@Service
public class FocusService {

    private final FocusSessionRepository repository;

    public FocusService(FocusSessionRepository repository) {
        this.repository = repository;
    }

    @Transactional
    public FocusSession saveSession(FocusRecordRequest request) {
        LocalDateTime registradoEm = Optional.ofNullable(request.getRegistradoEm())
                .orElse(LocalDateTime.now());

        FocusSession session = new FocusSession(
                request.getNivelFoco(),
                request.getTempoMinutos(),
                request.getComentario(),
                request.getCategoria(),
                registradoEm);

        return repository.save(session);
    }

    @Transactional(readOnly = true)
    public ProductivityDiagnosisResponse diagnose() {
        List<FocusSession> sessions = repository.findAll();
        if (sessions.isEmpty()) {
            return new ProductivityDiagnosisResponse(0.0, 0, 0,
                    "Nenhum registro encontrado. Comece um bloco de foco para receber um diagnóstico.");
        }

        int totalMinutos = sessions.stream()
                .mapToInt(FocusSession::getTempoMinutos)
                .sum();

        OptionalDouble averageOptional = sessions.stream()
                .mapToInt(FocusSession::getNivelFoco)
                .average();

        double mediaNivelFoco = averageOptional.orElse(0.0);
        String feedback = buildFeedback(mediaNivelFoco, totalMinutos, sessions.size());

        return new ProductivityDiagnosisResponse(mediaNivelFoco, totalMinutos, sessions.size(), feedback);
    }

    private String buildFeedback(double mediaNivelFoco, int totalMinutos, int registros) {
        if (registros == 0) {
            return "Nenhum bloco de foco registrado ainda.";
        }

        if (mediaNivelFoco < 2.5) {
            return "Seu foco está baixo. Considere fazer pausas regulares e reduzir interrupções.";
        }

        if (mediaNivelFoco < 4.0) {
            return "Bom progresso! Mantenha a disciplina e tente elevar a qualidade do foco.";
        }

        if (totalMinutos >= 120) {
            return "Você está em uma maratona produtiva de alto nível. Excelente trabalho!";
        }

        return "O foco está excelente. Continue este ritmo para alcançar grandes resultados.";
    }
}
