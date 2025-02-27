package com.tca.repository;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.VooDAOImpl;
import com.tca.model.Voo;
import com.github.hugoperlin.results.Resultado;

public class VooRepository {
    private VooDAOImpl dao;
    private static VooRepository instance;

    public static VooRepository getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new VooRepository();
        return instance;
    }

    public VooRepository() {
        this.dao = new VooDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(Voo voo) throws SQLException {
        Resultado resultado = dao.criar(voo);
        return resultado;
    }

    public Resultado get(Integer id) throws SQLException {
        Resultado resultado = dao.get(id);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> voos = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(voos.stream().allMatch(element -> element instanceof Voo))) {
                System.out.println("Nenhuma instância de Voo foi encontrada");
                return null;
            }
            return voos;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado atualizar(Integer id, Voo voo) throws SQLException {
        Resultado resultado = dao.atualizar(id, voo);
        return resultado;
    }

    public Resultado deletar(Integer id) throws SQLException {
        Resultado resultado = dao.deletar(id);
        return resultado;
        }

        public ArrayList<?> getVoosFiltro(String numero, String origem, String destino, LocalDateTime dataEmbarqueInicio, LocalDateTime dataEmbarqueFim, String aeroportoEmbarque, LocalDateTime dataDesembarqueInicio, LocalDateTime dataDesembarqueFim, String aeroportoDesembarque, String status) throws SQLException {
        Resultado resultado = dao.getVoosFiltro(numero, origem, destino, dataEmbarqueInicio, dataEmbarqueFim, aeroportoEmbarque, dataDesembarqueInicio, dataDesembarqueFim, aeroportoDesembarque, status);
        if (resultado.foiSucesso()) {
            ArrayList<?> voos = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(voos.stream().allMatch(element -> element instanceof Voo))) {
                System.out.println("Nenhuma instância de Voo foi encontrada");
                return null;
            }
            return voos;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado verificarVooLotado(Integer id) throws SQLException {
        Resultado resultado = dao.verificarVooLotado(id);
        return resultado;
    }
    
}
