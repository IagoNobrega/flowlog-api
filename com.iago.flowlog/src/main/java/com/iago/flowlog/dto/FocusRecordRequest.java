package com.iago.flowlog.dto;

import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class FocusRecordRequest {

    @NotNull(message = "nivel_foco é obrigatório")
    @Min(value = 1, message = "nivel_foco deve ser entre 1 e 5")
    @Max(value = 5, message = "nivel_foco deve ser entre 1 e 5")
    private Integer nivelFoco;

    @NotNull(message = "tempo_minutos é obrigatório")
    @Min(value = 1, message = "tempo_minutos deve ser maior que 0")
    private Integer tempoMinutos;

    @NotBlank(message = "comentario é obrigatório")
    private String comentario;

    private String categoria;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime registradoEm;

    public Integer getNivelFoco() {
        return nivelFoco;
    }

    public void setNivelFoco(Integer nivelFoco) {
        this.nivelFoco = nivelFoco;
    }

    public Integer getTempoMinutos() {
        return tempoMinutos;
    }

    public void setTempoMinutos(Integer tempoMinutos) {
        this.tempoMinutos = tempoMinutos;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public LocalDateTime getRegistradoEm() {
        return registradoEm;
    }

    public void setRegistradoEm(LocalDateTime registradoEm) {
        this.registradoEm = registradoEm;
    }
}
