package com.tca.repository;

import java.sql.SQLException;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.implementations.PassageiroDAOImpl;
import com.tca.model.Passageiro;
import com.github.hugoperlin.results.Resultado;

public class PassageiroRepository {
    private PassageiroDAOImpl dao;

    public PassageiroRepository() {
        this.dao = new PassageiroDAOImpl(FabricaConexoes.getInstance());
    }

    public Resultado criar(Passageiro passageiro) throws SQLException {
        Resultado resultado = dao.criar(passageiro);
        return resultado;
    }

    public ArrayList<?> listar() throws SQLException {
        Resultado resultado = dao.listar();
        if (resultado.foiSucesso()) {
            ArrayList<?> passageiros = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(passageiros.stream().allMatch(element -> element instanceof Passageiro))) {
                System.out.println("Nenhuma instância de Passageiro foi encontrada");
                return null;
            }
            return passageiros;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

    public Resultado atualizar(String cpf, Passageiro passageiro) throws SQLException {
        Resultado resultado = dao.atualizar(cpf, passageiro);
        return resultado;
    }

    public Resultado deletar(String cpf) throws SQLException {
        Resultado resultado = dao.deletar(cpf);
        return resultado;
    }

    public ArrayList<?> getPassageirosFiltro(String cpf, String email, String nome, String passaporte, String telefone, Integer idPais) throws SQLException {
        Resultado resultado = dao.getPassageirosFiltro(cpf, email, nome, passaporte, telefone, idPais);
        if (resultado.foiSucesso()) {
            ArrayList<?> passageiros = (ArrayList<?>) resultado.comoSucesso().getObj();
            if (!(passageiros.stream().allMatch(element -> element instanceof Passageiro))) {
                System.out.println("Nenhuma instância de Passageiro foi encontrada");
                return null;
            }
            return passageiros;
        }
        System.out.println(resultado.comoErro().getMsg());
        return null;
    }

}
