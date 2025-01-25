package com.tca.model;

public class CompanhiaAerea {
    private Integer id;
    private String codigoIcao;
    private String nome;
    
    public CompanhiaAerea(String codigoIcao, String nome) {
        this.codigoIcao = codigoIcao;
        this.nome = nome;
    }

    public CompanhiaAerea(Integer id, String codigoIcao, String nome) {
        this(codigoIcao, nome);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public String getCodigoIcao() {
        return codigoIcao;
    }

    public void setCodigoIcao(String codigoIcao) {
        this.codigoIcao = codigoIcao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
