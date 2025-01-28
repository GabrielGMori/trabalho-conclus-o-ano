package com.tca.dao.interfaces;

import java.sql.SQLException;
import java.time.LocalDateTime;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Voo;

public interface VooDAO {
    Resultado criar(Voo voo) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(Integer id, Voo voo) throws SQLException;

    Resultado deletar(Integer id) throws SQLException;

    Resultado getVoosFiltro(String numero,  String status, String origem, String destino, LocalDateTime horarioEmbarqueInicial, LocalDateTime horarioEmbarqueFinal, LocalDateTime horarioDesembarqueInicial, LocalDateTime horarioDesembarqueFinal, Integer idAeronave, Integer idPortaoEmbarque, String AeroportoEmbarque, String AeroportoChegada) throws SQLException;

    Resultado verificarVooLotado(Integer id) throws SQLException;

    Resultado verificarDisponibilidadeAssento(Integer id, String assento) throws SQLException;
}
