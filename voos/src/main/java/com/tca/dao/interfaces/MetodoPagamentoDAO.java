package com.tca.dao.interfaces;

import java.sql.SQLException;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.MetodoPagamento;

public interface MetodoPagamentoDAO {
    Resultado criar(MetodoPagamento metodoPagamento) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;
}
