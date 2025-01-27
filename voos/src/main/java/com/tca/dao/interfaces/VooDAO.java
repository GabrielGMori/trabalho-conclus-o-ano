package com.tca.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDate;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Voo;

public interface VooDAO {
    Resultado criar(Voo voo) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(Integer id, Voo voo) throws SQLException;

    Resultado deletar(Integer id) throws SQLException;

    Resultado getVoosFiltro(String numero,  String status, String origem, String destino, LocalDate horarioEmbarqueInicial, LocalDate horarioEmbarqueFinal, LocalDate horarioDesembarqueInicial, LocalDate horarioDesembarqueFinal, Integer idAeronave, Integer idPortaoEmbarque, Integer idAeroportoChegada) throws SQLException;

    Resultado verificarVooLotado(Integer id) throws SQLException;

    Resultado verificarDisponibilidadeAssento(Integer id, String assento) throws SQLException;
}
