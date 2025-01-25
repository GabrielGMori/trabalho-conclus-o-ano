package com.tca.model;

import java.util.Date;

public class Passagem {
    private Integer id;
    private Date dataCompra;
    private String assento;
    private String cpfPassageiro;
    private Integer idVoo;
    private Integer idMetodoPagamento;
    private Integer idCheckIn;
    
    public Passagem(Date dataCompra, String assento, String cpfPassageiro, Integer idVoo, Integer idMetodoPagamento) {
        this.dataCompra = dataCompra;
        this.assento = assento;
        this.cpfPassageiro = cpfPassageiro;
        this.idVoo = idVoo;
        this.idMetodoPagamento = idMetodoPagamento;
    }

    public Passagem(Integer id, Date dataCompra, String assento, String cpfPassageiro, Integer idVoo, Integer idMetodoPagamento) {
        this(dataCompra, assento, cpfPassageiro, idVoo, idMetodoPagamento);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public Date getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(Date dataCompra) {
        this.dataCompra = dataCompra;
    }

    public String getAssento() {
        return assento;
    }

    public void setAssento(String assento) {
        this.assento = assento;
    }

    public String getCpfPassageiro() {
        return cpfPassageiro;
    }

    public void setCpfPassageiro(String cpfPassageiro) {
        this.cpfPassageiro = cpfPassageiro;
    }

    public Integer getIdVoo() {
        return idVoo;
    }

    public void setIdVoo(Integer idVoo) {
        this.idVoo = idVoo;
    }

    public Integer getIdMetodoPagamento() {
        return idMetodoPagamento;
    }

    public void setIdMetodoPagamento(Integer idMetodoPagamento) {
        this.idMetodoPagamento = idMetodoPagamento;
    }

    public Integer getIdCheckIn() {
        return idCheckIn;
    }

    public void setIdCheckIn(Integer idCheckIn) {
        this.idCheckIn = idCheckIn;
    }

    
}
