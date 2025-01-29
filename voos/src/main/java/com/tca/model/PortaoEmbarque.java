package com.tca.model;

import java.time.LocalDateTime;

import com.github.hugoperlin.results.Resultado;
import com.tca.repository.PortaoEmbarqueRepository;

public class PortaoEmbarque {
    private Integer id;
    private String codigo;
    private Boolean disponivel;
    private Integer idAeroporto;
    private PortaoEmbarqueRepository portaoEmbarqueRepository;

    public PortaoEmbarque(String codigo, Boolean disponivel, Integer idAeroporto) {
        this.codigo = codigo;
        this.disponivel = disponivel;
        this.idAeroporto = idAeroporto;
        portaoEmbarqueRepository = PortaoEmbarqueRepository.getInstance();
    }

    public PortaoEmbarque(Integer id, String codigo, Boolean disponivel, Integer idAeroporto) {
        this(codigo, disponivel, idAeroporto);
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }  

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public Boolean getDisponivel() {
        return disponivel;
    }

    public void setDisponivel(Boolean disponivel) {
        this.disponivel = disponivel;
    }

    public Integer getIdAeroporto() {
        return idAeroporto;
    }

    public void setIdAeroporto(Integer idAeroporto) {
        this.idAeroporto = idAeroporto;
    }

    public Boolean emUso(LocalDateTime dataInicial, LocalDateTime dataFinal) {
        try {
            Resultado result = portaoEmbarqueRepository.verificarEmUso(id, dataInicial, dataFinal);
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
