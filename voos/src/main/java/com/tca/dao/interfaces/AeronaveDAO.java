package com.tca.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Aeronave;

public interface AeronaveDAO {
    Resultado criar(Aeronave aeronave) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(Integer id, Aeronave aeronave) throws SQLException;

    Resultado deletar(Integer id) throws SQLException;

    Resultado getAeronavesFiltro(String modelo, Integer capacidade, Integer idCompanhia) throws SQLException;

    Resultado verificarDisponibilidade(Integer id, LocalDateTime dataInicial, LocalDateTime dataFinal) throws SQLException;
}
