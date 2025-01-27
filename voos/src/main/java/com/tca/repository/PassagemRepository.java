package com.tca.repository;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.PassagemDAOImpl;
import com.tca.model.Passagem;
import com.github.hugoperlin.results.Resultado;

public class PassagemRepository {
    private PassagemDAOImpl dao;

    public PassagemRepository() {
        this.dao = new PassagemDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(Passagem passagem) throws SQLException {
        Resultado resultado = dao.criar(passagem);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> passagens = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(passagens.stream().allMatch(element -> element instanceof Passagem))) {
                System.out.println("Nenhuma instância de Passagem foi encontrada");
                return null;
            }
            return passagens;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public ArrayList<?> getPassagensFiltro(LocalDate dataCompraIncial, LocalDate dataCompraFinal, String assento, String cpfPassageiro, String numeroVoo, Integer idMetodoPagamento) throws SQLException {
        Resultado resultado = dao.getPassagensFiltro(dataCompraIncial, dataCompraFinal, assento, cpfPassageiro, numeroVoo, idMetodoPagamento);
        if (resultado.foiSucesso()) {
            ArrayList<?> passagens = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(passagens.stream().allMatch(element -> element instanceof Passagem))) {
                System.out.println("Nenhuma instância de Passagem foi encontrada");
                return null;
            }
            return passagens;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado realizarCheckIn(Integer id) throws SQLException {
        Resultado resultado = dao.realizarCheckIn(id);
        return resultado;
    }

}
