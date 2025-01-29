package com.tca.model;

import java.time.LocalDateTime;

import com.github.hugoperlin.results.Resultado;
import com.tca.repository.AeronaveRepository;

public class Aeronave {
    private Integer id;
    private String modelo;
    private Integer capacidade;
    private Integer idCompanhiaAerea;
    private AeronaveRepository aeronaveRepository;

    public Aeronave(String modelo, Integer capacidade, Integer idCompanhiaAerea) {
        this.modelo = modelo;
        this.capacidade = capacidade;
        this.idCompanhiaAerea = idCompanhiaAerea;
        aeronaveRepository = AeronaveRepository.getInstance();
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

    public Boolean verificarDisponibilidade(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        try {
            Resultado result = aeronaveRepository.verificarDisponibilidade(id, dataInicial, dataFinal);
            if (result.foiErro()) {
                System.out.println(result.comoErro().getMsg());
                return null;
            }
            return (Boolean) result.comoSucesso().getObj();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
