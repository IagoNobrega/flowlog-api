package com.iago.flowlog.model;

import java.time.LocalDateTime;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class FocusSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private int nivelFoco;

    @Column(nullable = false)
    private int tempoMinutos;

    @Column(nullable = false, length = 1000)
    private String comentario;

    private String categoria;

    @Column(nullable = false)
    private LocalDateTime registradoEm;

    public FocusSession() {
        // Default constructor for JPA
    }

    public FocusSession(int nivelFoco, int tempoMinutos, String comentario, String categoria, LocalDateTime registradoEm) {
        this.nivelFoco = nivelFoco;
        this.tempoMinutos = tempoMinutos;
        this.comentario = comentario;
        this.categoria = categoria;
        this.registradoEm = registradoEm;
    }

    public Long getId() {
        return id;
    }

    public int getNivelFoco() {
        return nivelFoco;
    }

    public void setNivelFoco(int nivelFoco) {
        this.nivelFoco = nivelFoco;
    }

    public int getTempoMinutos() {
        return tempoMinutos;
    }

    public void setTempoMinutos(int tempoMinutos) {
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
