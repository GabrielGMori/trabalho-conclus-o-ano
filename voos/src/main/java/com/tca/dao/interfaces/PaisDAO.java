package com.tca.dao.interfaces;

import java.sql.SQLException;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.Pais;

public interface PaisDAO {
    Resultado criar(Pais pais) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;
}
