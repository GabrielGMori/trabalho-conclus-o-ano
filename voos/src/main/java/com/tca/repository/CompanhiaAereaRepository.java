package com.tca.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.CompanhiaAereaDAOImpl;
import com.tca.model.CompanhiaAerea;
import com.github.hugoperlin.results.Resultado;

public class CompanhiaAereaRepository {
    private CompanhiaAereaDAOImpl dao;
    private static CompanhiaAereaRepository instance;

    public static CompanhiaAereaRepository getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new CompanhiaAereaRepository();
        return instance;
    }

    public CompanhiaAereaRepository() {
        this.dao = new CompanhiaAereaDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(CompanhiaAerea companhiaAerea) throws SQLException {
        Resultado resultado = dao.criar(companhiaAerea);
        return resultado;
    }

    public Resultado get(Integer id) throws SQLException {
        Resultado resultado = dao.get(id);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> companhiasAereas = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(companhiasAereas.stream().allMatch(element -> element instanceof CompanhiaAerea))) {
                System.out.println("Nenhuma instância de CompanhiaAerea foi encontrada");
                return null;
            }
            return companhiasAereas;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado atualizar(Integer id, CompanhiaAerea companhiaAerea) throws SQLException {
        Resultado resultado = dao.atualizar(id, companhiaAerea);
        return resultado;
    }

    public Resultado deletar(Integer id) throws SQLException {
        Resultado resultado = dao.deletar(id);
        return resultado;
    }

    public ArrayList<?> getCompanhiasFiltro(String codigoIcao, String nome) throws SQLException {
        Resultado resultado = dao.getCompanhiasFiltro(codigoIcao, nome);
        if (resultado.foiSucesso()) {
            ArrayList<?> companhiasAereas = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(companhiasAereas.stream().allMatch(element -> element instanceof CompanhiaAerea))) {
                System.out.println("Nenhuma instância de CompanhiaAerea foi encontrada");
                return null;
            }
            return companhiasAereas;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado verificarDisponibilidadeCompanhia(Integer id) throws SQLException {
        Resultado resultado = dao.verificarDisponibilidadeCompanhia(id);
        return resultado;
    }

}
