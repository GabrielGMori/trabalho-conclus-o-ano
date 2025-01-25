package com.tca.model;

public class Pais {
    private Integer id;
    private String nome;

    public Pais(String nome) {
        this.nome = nome;
    }

    public Pais(Integer id, String nome) {
        this(nome);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
