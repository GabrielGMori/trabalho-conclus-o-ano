package com.tca.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDate;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Manutencao;

public interface ManutencaoDAO {
    Resultado criar(Manutencao manutencao) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(Integer id, Manutencao manutencao) throws SQLException;

    Resultado deletar(Integer id) throws SQLException;

    Resultado getManutencoesFiltro(String descricao, LocalDate dataInicio, LocalDate dataFim, String status, Integer idAeronave) throws SQLException;
}
