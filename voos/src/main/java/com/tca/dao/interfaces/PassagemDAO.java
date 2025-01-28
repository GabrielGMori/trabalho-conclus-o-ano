package com.tca.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Passagem;

public interface PassagemDAO {
    Resultado criar(Passagem passagem) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado getPassagensFiltro(LocalDateTime dataCompraIncial, LocalDateTime dataCompraFinal, String assento, String cpfPassageiro, String numeroVoo, Integer idMetodoPagamento) throws SQLException;

    Resultado realizarCheckIn(Integer id) throws SQLException;

}
