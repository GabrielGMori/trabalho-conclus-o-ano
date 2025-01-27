package com.tca.model;

import java.time.LocalDate;

import com.github.hugoperlin.results.Resultado;
import com.tca.repository.VooRepository;

public class Voo {
    private Integer id;
    private String numero;
    private String status;
    private String origem;
    private String destino;
    private LocalDate horarioEmbarque;
    private LocalDate horarioDesembarque;
    private Integer idAeronave;
    private Integer idPortaoEmbarque;
    private Integer idAeroportoChegada;
    private VooRepository vooRepository;

    public Voo(String numero, String status, String origem, String destino, LocalDate horarioEmbarque,
            LocalDate horarioDesembarque, Integer idAeronave, Integer idPortaoEmbarque, Integer idAeroportoChegada) {
        this.numero = numero;
        this.status = status;
        this.origem = origem;
        this.destino = destino;
        this.horarioEmbarque = horarioEmbarque;
        this.horarioDesembarque = horarioDesembarque;
        this.idAeronave = idAeronave;
        this.idPortaoEmbarque = idPortaoEmbarque;
        this.idAeroportoChegada = idAeroportoChegada;
        vooRepository = new VooRepository();
    }

    public Voo(Integer id, String numero, String status, String origem, String destino, LocalDate horarioEmbarque,
            LocalDate horarioDesembarque, Integer idAeronave, Integer idPortaoEmbarque, Integer idAeroportoChegada) {
        this(numero, status, origem, destino, horarioEmbarque, horarioDesembarque, idAeronave, idPortaoEmbarque,idAeroportoChegada);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrigem() {
        return origem;
    }

    public void setOrigem(String origem) {
        this.origem = origem;
    }

    public String getDestino() {
        return destino;
    }

    public void setDestino(String destino) {
        this.destino = destino;
    }

    public LocalDate getHorarioEmbarque() {
        return horarioEmbarque;
    }

    public void setHorarioEmbarque(LocalDate horarioEmbarque) {
        this.horarioEmbarque = horarioEmbarque;
    }

    public LocalDate getHorarioDesembarque() {
        return horarioDesembarque;
    }

    public void setHorarioDesembarque(LocalDate horarioDesembarque) {
        this.horarioDesembarque = horarioDesembarque;
    }

    public Integer getIdAeronave() {
        return idAeronave;
    }

    public void setIdAeronave(Integer idAeronave) {
        this.idAeronave = idAeronave;
    }

    public Integer getIdPortaoEmbarque() {
        return idPortaoEmbarque;
    }

    public void setIdPortaoEmbarque(Integer idPortaoEmbarque) {
        this.idPortaoEmbarque = idPortaoEmbarque;
    }

    public Integer getIdAeroportoChegada() {
        return idAeroportoChegada;
    }

    public void setIdAeroportoChegada(Integer idAeroportoChegada) {
        this.idAeroportoChegada = idAeroportoChegada;
    }

    public Boolean verificarCheio() {
        try {
            Resultado result = vooRepository.verificarVooLotado(id);
            if (result.foiErro()) {
                return null;
            }
            return (Boolean) result.comoSucesso().getObj();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
