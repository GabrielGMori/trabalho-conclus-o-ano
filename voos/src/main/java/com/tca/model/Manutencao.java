package com.tca.model;

import java.time.LocalDateTime;

public class Manutencao {
    private Integer id;
    private String descricao;
    private LocalDateTime dataInicio;
    private LocalDateTime dataFim;
    private String status;
    private Integer idAeronave;
    
    public Manutencao(String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, String status, Integer idAeronave) {
        this.descricao = descricao;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.status = status;
        this.idAeronave = idAeronave;
    }

    public Manutencao(Integer id, String descricao, LocalDateTime dataInicio, LocalDateTime dataFim, String status, Integer idAeronave) {
        this(descricao, dataInicio, dataFim, status, idAeronave);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public LocalDateTime getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDateTime dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDateTime getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDateTime dataFim) {
        this.dataFim = dataFim;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getIdAeronave() {
        return idAeronave;
    }

    public void setIdAeronave(Integer idAeronave) {
        this.idAeronave = idAeronave;
    }   
}