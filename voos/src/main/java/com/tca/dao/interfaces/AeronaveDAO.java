package com.tca.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDate;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Aeronave;

public interface AeronaveDAO {
    Resultado criar(Aeronave aeronave) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(Integer id, Aeronave aeronave) throws SQLException;

    Resultado deletar(Integer id) throws SQLException;

    Resultado getAeronavesFiltro(String modelo, Integer capacidade, Integer assentosPorFilero, Integer idCompanhia) throws SQLException;

    Resultado verificarDisponibilidade(Integer id, LocalDate dataInicial, LocalDate dataFinal) throws SQLException;
}
