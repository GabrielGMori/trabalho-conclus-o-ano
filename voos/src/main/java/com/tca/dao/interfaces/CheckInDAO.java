package com.tca.dao.interfaces;

import java.sql.SQLException;

import com.github.hugoperlin.results.Resultado;
import com.tca.model.CheckIn;

public interface CheckInDAO {
    Resultado criar(CheckIn checkIn) throws SQLException;

    Resultado get(Integer id) throws SQLException;

    Resultado listar() throws SQLException;
}
