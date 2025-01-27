package com.tca.repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.PortaoEmbarqueDAOImpl;
import com.tca.model.PortaoEmbarque;
import com.github.hugoperlin.results.Resultado;

public class PortaoEmbarqueRepository {
    private PortaoEmbarqueDAOImpl dao;

    public PortaoEmbarqueRepository() {
        this.dao = new PortaoEmbarqueDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(PortaoEmbarque portaoEmbarque) throws SQLException {
        Resultado resultado = dao.criar(portaoEmbarque);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> portoesEmbarque = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(portoesEmbarque.stream().allMatch(element -> element instanceof PortaoEmbarque))) {
                System.out.println("Nenhuma instância de PortaoEmbarque foi encontrada");
                return null;
            }
            return portoesEmbarque;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado atualizar(Integer id, PortaoEmbarque portaoEmbarque) throws SQLException {
        Resultado resultado = dao.atualizar(id, portaoEmbarque);
        return resultado;
    }

    public Resultado deletar(Integer id) throws SQLException {
        Resultado resultado = dao.deletar(id);
        return resultado;
    }

    public ArrayList<?> getPortoesEmbarqueFiltro(String codigo, Boolean disponivel, Integer idAeroporto) throws SQLException {
        Resultado resultado = dao.getPortoesEmbarqueFiltro(codigo, disponivel, idAeroporto);
        if (resultado.foiSucesso()) {
            ArrayList<?> portoesEmbarque = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(portoesEmbarque.stream().allMatch(element -> element instanceof PortaoEmbarque))) {
                System.out.println("Nenhuma instância de PortaoEmbarque foi encontrada");
                return null;
            }
            return portoesEmbarque;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado verificarEmUso(Integer id, LocalDate dataInicial, LocalDate dataFinal) throws SQLException {
        Resultado resultado = dao.verificarEmUso(id, dataInicial, dataFinal);
        return resultado;
    }
    
}
