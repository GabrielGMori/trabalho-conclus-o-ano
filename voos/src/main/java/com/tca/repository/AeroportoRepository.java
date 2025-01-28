package com.tca.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.AeroportoDAOImpl;
import com.tca.model.Aeroporto;
import com.github.hugoperlin.results.Resultado;

public class AeroportoRepository {
    private AeroportoDAOImpl dao;
    private static AeroportoRepository instance;

    public static AeroportoRepository getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new AeroportoRepository();
        return instance;
    }

    public AeroportoRepository() {
        this.dao = new AeroportoDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(Aeroporto aeroporto) throws SQLException {
        Resultado resultado = dao.criar(aeroporto);
        return resultado;
    }

    public Resultado get(Integer id) throws SQLException {
        Resultado resultado = dao.get(id);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> aeroportos = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(aeroportos.stream().allMatch(element -> element instanceof Aeroporto))) {
                System.out.println("Nenhuma instância de Aeroporto foi encontrada");
                return null;
            }
            return aeroportos;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado atualizar(Integer id, Aeroporto aeroporto) throws SQLException {
        Resultado resultado = dao.atualizar(id, aeroporto);
        return resultado;
    }

    public Resultado deletar(Integer id) throws SQLException {
        Resultado resultado = dao.deletar(id);
        return resultado;
    }

    public ArrayList<?> getAeroportosFiltro(String nome, String localizacao) throws SQLException {
        Resultado resultado = dao.getAeroportosFiltro(nome, localizacao);
        if (resultado.foiSucesso()) {
            ArrayList<?> aeroportos = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(aeroportos.stream().allMatch(element -> element instanceof Aeroporto))) {
                System.out.println("Nenhuma instância de Aeroporto foi encontrada");
                return null;
            }
            return aeroportos;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado verificarAeroportoEmUso(Integer id) throws SQLException {
        Resultado resultado = dao.verificarAeroportoEmUso(id);
        return resultado;
    }
    
}
