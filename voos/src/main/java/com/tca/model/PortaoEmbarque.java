package com.tca.model;

public class PortaoEmbarque {
    private Integer id;
    private String codigo;
    private Boolean disponivel;
    private Integer idAeroporto;

    public PortaoEmbarque(String codigo, Boolean disponivel, Integer idAeroporto) {
        this.codigo = codigo;
        this.disponivel = disponivel;
        this.idAeroporto = idAeroporto;
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
}
