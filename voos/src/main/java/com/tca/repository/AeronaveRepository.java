package com.tca.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.AeronaveDAOImpl;
import com.tca.model.Aeronave;
import com.github.hugoperlin.results.Resultado;

public class AeronaveRepository {
    private AeronaveDAOImpl dao;
    private static AeronaveRepository instance;

    public static AeronaveRepository getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new AeronaveRepository();
        return instance;
    }

    public AeronaveRepository() {
        this.dao = new AeronaveDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(Aeronave aeronave) throws SQLException {
        Resultado resultado = dao.criar(aeronave);
        return resultado;
    }

    public Resultado get(Integer id) throws SQLException {
        Resultado resultado = dao.get(id);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> aeronaves = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(aeronaves.stream().allMatch(element -> element instanceof Aeronave))) {
                System.out.println("Nenhuma instância de Aeronave foi encontrada");
                return null;
            }
            return aeronaves;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado atualizar(Integer id, Aeronave aeronave) throws SQLException {
        Resultado resultado = dao.atualizar(id, aeronave);
        return resultado;
    }

    public Resultado deletar(Integer id) throws SQLException {
        Resultado resultado = dao.deletar(id);
        return resultado;
    }

    public ArrayList<?> getAeronavesFiltro(String modelo, Integer capacidade, String companhia) throws SQLException {
        Resultado resultado = dao.getAeronavesFiltro(modelo, capacidade, companhia);
        if (resultado.foiSucesso()) {
            ArrayList<?> aeronaves = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(aeronaves.stream().allMatch(element -> element instanceof Aeronave))) {
                System.out.println("Nenhuma instância de Aeronave foi encontrada");
                return null;
            }
            return aeronaves;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

}
