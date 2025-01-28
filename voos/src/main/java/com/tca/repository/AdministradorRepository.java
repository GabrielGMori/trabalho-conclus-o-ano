package com.tca.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.AdministradorDAOImpl;
import com.tca.model.Administrador;
import com.github.hugoperlin.results.Resultado;

public class AdministradorRepository {
    private AdministradorDAOImpl dao;
    private static AdministradorRepository instance;

    public static AdministradorRepository getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new AdministradorRepository();
        return instance;
    }

    public AdministradorRepository() {
        this.dao = new AdministradorDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(Administrador administrador) throws SQLException {
        Resultado resultado = dao.criar(administrador);
        return resultado;
    }

    public Resultado get(String cpf) throws SQLException {
        Resultado resultado = dao.get(cpf);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> administradores = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(administradores.stream().allMatch(element -> element instanceof Administrador))) {
                System.out.println("Nenhuma instância de Administrador foi encontrada");
                return null;
            }
            return administradores;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado atualizar(String cpf, Administrador administrador) throws SQLException {
        Resultado resultado = dao.atualizar(cpf, administrador);
        return resultado;
    }

    public Resultado deletar(String cpf) throws SQLException {
        Resultado resultado = dao.deletar(cpf);
        return resultado;
    }

    public ArrayList<?> getAdministradoresFiltro(String cpf, String nome) throws SQLException {
        Resultado resultado = dao.getAdministradoresFiltro(cpf, nome);
        if (resultado.foiSucesso()) {
            ArrayList<?> administradores = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(administradores.stream().allMatch(element -> element instanceof Administrador))) {
                System.out.println("Nenhuma instância de Administrador foi encontrada");
                return null;
            }
            return administradores;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }
}
