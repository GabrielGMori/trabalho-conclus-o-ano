package com.tca.model;

public class MetodoPagamento {
    private Integer id;
    private String metodo;
    
    public MetodoPagamento(String metodo) {
        this.metodo = metodo;
    }

    public MetodoPagamento(Integer id, String metodo) {
        this(metodo);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public String getMetodo() {
        return metodo;
    }

    public void setMetodo(String metodo) {
        this.metodo = metodo;
    }    
}
