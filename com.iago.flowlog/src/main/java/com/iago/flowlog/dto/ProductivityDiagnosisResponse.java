package com.iago.flowlog.dto;

public class ProductivityDiagnosisResponse {

    private double mediaNivelFoco;
    private int tempoTotalMinutos;
    private long totalRegistros;
    private String feedback;

    public ProductivityDiagnosisResponse(double mediaNivelFoco, int tempoTotalMinutos, long totalRegistros, String feedback) {
        this.mediaNivelFoco = mediaNivelFoco;
        this.tempoTotalMinutos = tempoTotalMinutos;
        this.totalRegistros = totalRegistros;
        this.feedback = feedback;
    }

    public double getMediaNivelFoco() {
        return mediaNivelFoco;
    }

    public int getTempoTotalMinutos() {
        return tempoTotalMinutos;
    }

    public long getTotalRegistros() {
        return totalRegistros;
    }

    public String getFeedback() {
        return feedback;
    }
}
