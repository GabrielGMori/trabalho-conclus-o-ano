package com.tca.model;

import java.time.LocalDateTime;

import com.github.hugoperlin.results.Resultado;
import com.tca.repository.PassagemRepository;

public class Passagem {
    private Integer id;
    private LocalDateTime dataCompra;
    private String cpfPassageiro;
    private Integer idVoo;
    private Integer idMetodoPagamento;
    private Integer idCheckIn;
    private PassagemRepository passagemRepository;
    
    public Passagem(LocalDateTime dataCompra, String cpfPassageiro, Integer idVoo, Integer idMetodoPagamento) {
        this.dataCompra = dataCompra;
        this.cpfPassageiro = cpfPassageiro;
        this.idVoo = idVoo;
        this.idMetodoPagamento = idMetodoPagamento;
        passagemRepository = PassagemRepository.getInstance();
    }

    public Passagem(Integer id, LocalDateTime dataCompra, String cpfPassageiro, Integer idVoo, Integer idMetodoPagamento) {
        this(dataCompra, cpfPassageiro, idVoo, idMetodoPagamento);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public LocalDateTime getDataCompra() {
        return dataCompra;
    }

    public void setDataCompra(LocalDateTime dataCompra) {
        this.dataCompra = dataCompra;
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

    public Boolean realizarCheckIn() {
        try {
            Resultado result = passagemRepository.realizarCheckIn(id);
            if (result.foiErro()) {
                System.out.println(result.comoErro().getMsg());
                return false;
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
