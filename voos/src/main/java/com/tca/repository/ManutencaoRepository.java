package com.tca.repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.ManutencaoDAOImpl;
import com.tca.model.Manutencao;
import com.github.hugoperlin.results.Resultado;

public class ManutencaoRepository {
    private ManutencaoDAOImpl dao;

    public ManutencaoRepository() {
        this.dao = new ManutencaoDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(Manutencao manutencao) throws SQLException {
        Resultado resultado = dao.criar(manutencao);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> manutencoes = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(manutencoes.stream().allMatch(element -> element instanceof Manutencao))) {
                System.out.println("Nenhuma instância de Manutencao foi encontrada");
                return null;
            }
            return manutencoes;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado atualizar(Integer id, Manutencao manutencao) throws SQLException {
        Resultado resultado = dao.atualizar(id, manutencao);
        return resultado;
    }

    public Resultado deletar(Integer id) throws SQLException {
        Resultado resultado = dao.deletar(id);
        return resultado;
    }

    public ArrayList<?> getManutencaosFiltro(String descricao, LocalDate dataInicioInicial, LocalDate dataInicioFinal, LocalDate dataFimInicial, LocalDate dataFimFinal, String status, Integer idAeronave) throws SQLException {
        Resultado resultado = dao.getManutencoesFiltro(descricao, dataInicioInicial, dataInicioFinal, dataFimInicial, dataFimFinal, status, idAeronave);
        if (resultado.foiSucesso()) {
            ArrayList<?> manutencoes = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(manutencoes.stream().allMatch(element -> element instanceof Manutencao))) {
                System.out.println("Nenhuma instância de Manutencao foi encontrada");
                return null;
            }
            return manutencoes;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

}
