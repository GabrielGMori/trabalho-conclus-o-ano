package com.tca.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.interfaces.AdminstradorDAO;
import com.tca.model.Administrador;
import com.github.hugoperlin.results.Resultado;

public class AdministradorDAOImpl implements AdminstradorDAO {
    private FabricaConexoes fabrica;

    public AdministradorDAOImpl(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Administrador administrador) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Administrador(cpf_administrador_pk, nome_administrador, senha_administrador) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, administrador.getCpf());
            pstm.setString(2, administrador.getNome());
            pstm.setString(3, administrador.getSenha());

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Administrador criado com sucesso", administrador);
            }

            return Resultado.erro("Algo deu errado, o administrador não foi criado");

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    @Override
    public Resultado get(String cpf) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT * FROM Administrador WHERE cpf_administrador_pk = ?;");

            pstm.setString(1, cpf);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome_administrador");
                String senha = rs.getString("senha_administrador");

                Administrador administrador = new Administrador(cpf, nome, senha);
                return Resultado.sucesso("Administrador carregado", administrador);
            }

            return Resultado.erro("Administrador não encontrado");

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    @Override
    public Resultado listar() throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT * FROM Administrador;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Administrador> administradores = new ArrayList<>();
            while (rs.next()) {
                String cpf = rs.getString("cpf_administrador_pk");
                String nome = rs.getString("nome_administrador");
                String senha = rs.getString("senha_administrador");

                Administrador administrador = new Administrador(cpf, nome, senha);
                administradores.add(administrador);
            }

            return Resultado.sucesso("Administradores carregados", administradores);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    @Override
    public Resultado atualizar(String cpf, Administrador administrador) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "UPDATE Administrador SET cpf_administrador_pk = ?, nome_administrador = ?, senha_administrador = ? WHERE cpf_administrador_pk = ?;");

            pstm.setString(1, administrador.getCpf());
            pstm.setString(2, administrador.getNome());
            pstm.setString(3, administrador.getSenha());
            pstm.setString(4, cpf);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Administrador atualizado", administrador);
            }

            return Resultado.erro("Algo deu errado, o administrador não foi atualizado");

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    @Override
    public Resultado deletar(String cpf) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("DELETE FROM Administrador WHERE cpf_administrador_pk = ?;");

            pstm.setString(1, cpf);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Administrador removido", null);
            }

            return Resultado.erro("Algo deu errado, o administrador não foi removido");

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    @Override
    public Resultado getAdministradoresFiltro(String cpfFiltro, String nomeFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Administrador WHERE (? IS NULL OR cpf_administrador_pk = ?) AND (? IS NULL OR nome_administrador = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++)  {if (cpfFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, cpfFiltro); } 
            for (j+=2; i<j; i++)  {if (nomeFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, nomeFiltro); }

            ResultSet rs = pstm.executeQuery();

            ArrayList<Administrador> administradores = new ArrayList<>();
            while (rs.next()) {
                String cpf = rs.getString("cpf_administrador_pk");
                String nome = rs.getString("nome_administrador");
                String senha = rs.getString("senha_administrador");

                Administrador administradorFiltrado = new Administrador(cpf, nome, senha);
                administradores.add(administradorFiltrado);
            }

            return Resultado.sucesso("Administradores carregados", administradores);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

}
