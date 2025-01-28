package com.tca.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Manutencao;

public interface ManutencaoDAO {
    Resultado criar(Manutencao manutencao) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(Integer id, Manutencao manutencao) throws SQLException;

    Resultado deletar(Integer id) throws SQLException;

    Resultado getManutencoesFiltro(String descricao, LocalDateTime dataInicioInicial, LocalDateTime dataInicioFinal, LocalDateTime dataFimInicial, LocalDateTime dataFimFinal, String status, Integer idAeronave) throws SQLException;
}
