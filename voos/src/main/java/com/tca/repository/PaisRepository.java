package com.tca.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.PaisDAOImpl;
import com.tca.model.Pais;
import com.github.hugoperlin.results.Resultado;

public class PaisRepository {
    private PaisDAOImpl dao;

    public PaisRepository() {
        this.dao = new PaisDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(Pais pais) throws SQLException {
        Resultado resultado = dao.criar(pais);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> paises = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(paises.stream().allMatch(element -> element instanceof Pais))) {
                System.out.println("Nenhuma instância de Pais foi encontrada");
                return null;
            }
            return paises;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }
    
}
