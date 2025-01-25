package com.tca.dao.interfaces;

import java.sql.SQLException;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Passageiro;

public interface PassageiroDAO {
    Resultado criar(Passageiro passageiro) throws SQLException;

    Resultado listar() throws SQLException;

    Resultado atualizar(String cpf, Passageiro passageiro) throws SQLException;

    Resultado deletar(String cpf) throws SQLException;

    Resultado getPassageirosFiltro(String cpf, String email, String nome, String passaporte, String telefone, Integer idPais) throws SQLException;
}
