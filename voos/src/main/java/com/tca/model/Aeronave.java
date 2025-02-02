package com.tca.model;

public class Aeronave {
    private Integer id;
    private String modelo;
    private Integer capacidade;
    private Integer idCompanhiaAerea;

    public Aeronave(String modelo, Integer capacidade, Integer idCompanhiaAerea) {
        this.modelo = modelo;
        this.capacidade = capacidade;
        this.idCompanhiaAerea = idCompanhiaAerea;
    }

    public Aeronave(Integer id, String modelo, Integer capacidade, Integer idCompanhiaAerea) {
        this(modelo, capacidade, idCompanhiaAerea);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Integer getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(Integer capacidade) {
        this.capacidade = capacidade;
    }

    public Integer getIdCompanhiaAerea() {
        return idCompanhiaAerea;
    }

    public void setIdCompanhiaAerea(Integer idCompanhiaAerea) {
        this.idCompanhiaAerea = idCompanhiaAerea;
    }
}
