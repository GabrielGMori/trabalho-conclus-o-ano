package com.tca.dao.interfaces;

import java.sql.SQLException;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Administrador;

public interface AdminstradorDAO {
    Resultado criar(Administrador administrador) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(String cpf, Administrador administrador) throws SQLException;

    Resultado deletar(String cpf) throws SQLException;

    Resultado getAdministradoresFiltro(String cpf, String nome) throws SQLException;
}
