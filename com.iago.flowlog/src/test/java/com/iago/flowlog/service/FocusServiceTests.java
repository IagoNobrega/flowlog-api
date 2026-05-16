package com.iago.flowlog.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.iago.flowlog.dto.FocusRecordRequest;
import com.iago.flowlog.dto.ProductivityDiagnosisResponse;
import com.iago.flowlog.model.FocusSession;
import com.iago.flowlog.repository.FocusSessionRepository;

@ExtendWith(MockitoExtension.class)
class FocusServiceTests {

    @Mock
    private FocusSessionRepository repository;

    @InjectMocks
    private FocusService service;

    @Test
    void shouldSaveSessionWhenRegisteredDateIsProvided() {
        FocusRecordRequest request = new FocusRecordRequest();
        request.setNivelFoco(4);
        request.setTempoMinutos(60);
        request.setComentario("Sessão de foco bem-sucedida");
        request.setCategoria("development");
        request.setRegistradoEm(LocalDateTime.of(2026, 1, 1, 10, 0));

        when(repository.save(any(FocusSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        FocusSession saved = service.saveSession(request);

        assertThat(saved.getNivelFoco()).isEqualTo(4);
        assertThat(saved.getTempoMinutos()).isEqualTo(60);
        assertThat(saved.getComentario()).isEqualTo("Sessão de foco bem-sucedida");
        assertThat(saved.getCategoria()).isEqualTo("development");
        assertThat(saved.getRegistradoEm()).isEqualTo(LocalDateTime.of(2026, 1, 1, 10, 0));

        verify(repository).save(any(FocusSession.class));
    }

    @Test
    void shouldAssignCurrentTimestampWhenRegisteredDateIsNull() {
        FocusRecordRequest request = new FocusRecordRequest();
        request.setNivelFoco(3);
        request.setTempoMinutos(30);
        request.setComentario("Pausa de atualização de estado");
        request.setCategoria("planning");
        request.setRegistradoEm(null);

        when(repository.save(any(FocusSession.class))).thenAnswer(invocation -> invocation.getArgument(0));

        LocalDateTime beforeSave = LocalDateTime.now().minusSeconds(5);
        FocusSession saved = service.saveSession(request);
        LocalDateTime afterSave = LocalDateTime.now().plusSeconds(5);

        assertThat(saved.getRegistradoEm()).isNotNull();
        assertThat(saved.getRegistradoEm()).isBetween(beforeSave, afterSave);
        assertThat(saved.getNivelFoco()).isEqualTo(3);
        assertThat(saved.getTempoMinutos()).isEqualTo(30);

        verify(repository).save(any(FocusSession.class));
    }

    @Test
    void shouldReturnEmptyDiagnosisWhenNoSessionsExist() {
        when(repository.findAll()).thenReturn(List.of());

        ProductivityDiagnosisResponse response = service.diagnose();

        assertThat(response.getMediaNivelFoco()).isEqualTo(0.0);
        assertThat(response.getTempoTotalMinutos()).isEqualTo(0);
        assertThat(response.getTotalRegistros()).isEqualTo(0);
        assertThat(response.getFeedback()).contains("Nenhum registro encontrado");
    }

    @Test
    void shouldReturnLowFocusFeedbackWhenAverageBelowThreshold() {
        when(repository.findAll()).thenReturn(List.of(
                createSession(1, 30),
                createSession(2, 20)));

        ProductivityDiagnosisResponse response = service.diagnose();

        assertThat(response.getMediaNivelFoco()).isEqualTo(1.5);
        assertThat(response.getFeedback()).isEqualTo("Seu foco está baixo. Considere fazer pausas regulares e reduzir interrupções.");
    }

    @Test
    void shouldReturnGoodProgressFeedbackWhenAverageBetweenThresholds() {
        when(repository.findAll()).thenReturn(List.of(
                createSession(3, 10),
                createSession(3, 20)));

        ProductivityDiagnosisResponse response = service.diagnose();

        assertThat(response.getMediaNivelFoco()).isEqualTo(3.0);
        assertThat(response.getFeedback()).isEqualTo("Bom progresso! Mantenha a disciplina e tente elevar a qualidade do foco.");
    }

    @Test
    void shouldReturnMarathonFeedbackWhenTotalMinutesMeetsHighThreshold() {
        when(repository.findAll()).thenReturn(List.of(
                createSession(4, 60),
                createSession(4, 60)));

        ProductivityDiagnosisResponse response = service.diagnose();

        assertThat(response.getTempoTotalMinutos()).isEqualTo(120);
        assertThat(response.getFeedback()).isEqualTo("Você está em uma maratona produtiva de alto nível. Excelente trabalho!");
    }

    @Test
    void shouldReturnExcellentFocusFeedbackWhenHighAverageBelowMarathonThreshold() {
        when(repository.findAll()).thenReturn(List.of(
                createSession(5, 30),
                createSession(4, 30)));

        ProductivityDiagnosisResponse response = service.diagnose();

        assertThat(response.getMediaNivelFoco()).isEqualTo(4.5);
        assertThat(response.getTempoTotalMinutos()).isEqualTo(60);
        assertThat(response.getFeedback()).isEqualTo("O foco está excelente. Continue este ritmo para alcançar grandes resultados.");
    }

    private FocusSession createSession(int nivelFoco, int tempoMinutos) {
        return new FocusSession(nivelFoco, tempoMinutos, "Comentário de sessão", "categoria", LocalDateTime.now());
    }
}
