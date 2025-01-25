package com.tca.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDate;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Passagem;

public interface PassagemDAO {
    Resultado criar(Passagem passagem) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado getPassagensFiltro(LocalDate dataCompraIncial, LocalDate dataCompraFinal, String assento, String cpfPassageiro, String numeroVoo, Integer idMetodoPagamento) throws SQLException;

    Resultado realizarCheckIn(Integer id) throws SQLException;

    Resultado verificarDisponibilidadeAssento(Integer id, String assento) throws SQLException;
}
