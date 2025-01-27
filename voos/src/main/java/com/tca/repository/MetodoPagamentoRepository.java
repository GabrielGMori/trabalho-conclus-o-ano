package com.tca.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.MetodoPagamentoDAOImpl;
import com.tca.model.MetodoPagamento;
import com.github.hugoperlin.results.Resultado;

public class MetodoPagamentoRepository {
    private MetodoPagamentoDAOImpl dao;

    public MetodoPagamentoRepository() {
        this.dao = new MetodoPagamentoDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(MetodoPagamento metodoPagamento) throws SQLException {
        Resultado resultado = dao.criar(metodoPagamento);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> metodosPagamento = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(metodosPagamento.stream().allMatch(element -> element instanceof MetodoPagamento))) {
                System.out.println("Nenhuma instância de MetodoPagamento foi encontrada");
                return null;
            }
            return metodosPagamento;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }
    
}
