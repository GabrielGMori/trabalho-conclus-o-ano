package com.tca.dao.interfaces;

import java.sql.SQLException;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Aeroporto;

public interface AeroportoDAO {
    Resultado criar(Aeroporto aeroporto) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(Integer id, Aeroporto aeroporto) throws SQLException;

    Resultado deletar(Integer id) throws SQLException;

    Resultado getAeroportosFiltro(String nome, String localizacao) throws SQLException;

    Resultado verificarAeroportoEmUso(Integer id) throws SQLException;
}
