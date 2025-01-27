package com.tca.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.CheckInDAOImpl;
import com.tca.model.CheckIn;
import com.github.hugoperlin.results.Resultado;

public class CheckInRepository {
    private CheckInDAOImpl dao;

    public CheckInRepository() {
        this.dao = new CheckInDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(CheckIn checkIn) throws SQLException {
        Resultado resultado = dao.criar(checkIn);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> checkIns = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(checkIns.stream().allMatch(element -> element instanceof CheckIn))) {
                System.out.println("Nenhuma instância de CheckIn foi encontrada");
                return null;
            }
            return checkIns;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }
    
}
