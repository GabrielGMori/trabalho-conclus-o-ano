package com.tca.dao.interfaces;

import java.sql.SQLException;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.PortaoEmbarque;

public interface PortaoEmbarqueDAO {
    Resultado criar(PortaoEmbarque portaoEmbarque) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(Integer id, PortaoEmbarque portaoEmbarque) throws SQLException;

    Resultado deletar(Integer id) throws SQLException;

    Resultado getPortoesEmbarqueFiltro(String codigo, Boolean disponivel, Integer idAeroporto) throws SQLException;
}
