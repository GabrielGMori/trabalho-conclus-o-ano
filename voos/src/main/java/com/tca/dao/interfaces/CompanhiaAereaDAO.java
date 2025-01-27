package com.tca.dao.interfaces;

import java.sql.SQLException;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.CompanhiaAerea;

public interface CompanhiaAereaDAO {
    Resultado criar(CompanhiaAerea companhiaAerea) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(Integer id, CompanhiaAerea companhiaAerea) throws SQLException;

    Resultado deletar(Integer id) throws SQLException;

    Resultado getCompanhiasFiltro(String codigoIcao, String nome) throws SQLException;

    Resultado verificarDisponibilidadeCompanhia(Integer id) throws SQLException;
}
