package com.tca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tca.dao.interfaces.PassageiroDAO;
import com.tca.model.Passageiro;
import com.github.hugoperlin.results.Resultado;

public class JDBCPassageiroDAO implements PassageiroDAO {
    private FabricaConexoes fabrica;

    public JDBCPassageiroDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Passageiro passageiro) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Passageiro(cpf_passageiro_pk, email_passageiro, nome_passageiro, senha_passageiro, passaporte_passageiro, telefone_passageiro, id_pais_passageiro_fk) VALUES (?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, passageiro.getCpf());
            pstm.setString(2, passageiro.getEmail());
            pstm.setString(3, passageiro.getNome());
            pstm.setString(4, passageiro.getSenha());
            pstm.setString(5, passageiro.getPassaporte());
            pstm.setString(6, passageiro.getTelefone());
            pstm.setInt(7, passageiro.getIdPais());

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Passageiro criado com sucesso", passageiro);
            }

            return Resultado.erro("Algo deu errado, o passageiro não foi criado");

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
            pstm = con.prepareStatement("SELECT * FROM Passageiro;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Passageiro> passageiros = new ArrayList<>();
            while (rs.next()) {
                String cpf = rs.getString("cpf_passageiro_pk");
                String email = rs.getString("email_passageiro");
                String nome = rs.getString("nome_passageiro");
                String senha = rs.getString("senha_passageiro");
                String passaporte = rs.getString("passaporte_passageiro");
                String telefone = rs.getString("telefone_passageiro");
                int idPais = rs.getInt("id_pais_passageiro_fk");

                Passageiro passageiro = new Passageiro(cpf, email, nome, senha, passaporte, telefone, idPais);
                passageiros.add(passageiro);
            }

            return Resultado.sucesso("Passageiros carregados", passageiros);

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
    public Resultado atualizar(String cpf, Passageiro passageiro) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "UPDATE Passageiro SET cpf_passageiro_pk = ?, email_passageiro = ?, nome_passageiro = ?, senha_passageiro = ?, passaporte_passageiro = ?, telefone_passageiro = ?, id_pais_passageiro_fk = ? WHERE cpf_passageiro_pk = ?;");

            pstm.setString(1, passageiro.getCpf());
            pstm.setString(2, passageiro.getEmail());
            pstm.setString(3, passageiro.getNome());
            pstm.setString(4, passageiro.getSenha());
            pstm.setString(5, passageiro.getPassaporte());
            pstm.setString(6, passageiro.getTelefone());
            pstm.setInt(7, passageiro.getIdPais());
            pstm.setString(8, cpf);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Passageiro atualizado", passageiro);
            }

            return Resultado.erro("Algo deu errado, o passageiro não foi atualizado");

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
            pstm = con.prepareStatement("DELETE FROM Passageiro WHERE cpf_passageiro_pk = ?;");

            pstm.setString(1, cpf);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Passageiro removido", null);
            }

            return Resultado.erro("Algo deu errado, o passageiro não foi removido");

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
    public Resultado getPassageirosFiltro(String cpfFiltro, String emailFiltro, String nomeFiltro, String passaporteFiltro, String telefoneFiltro, Integer idPaisFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Passageiro WHERE (? IS NULL OR cpf_passageiro_pk = ?) AND (? IS NULL OR email_passageiro LIKE ?) AND (? IS NULL OR nome_passageiro LIKE ?) AND (? IS NULL OR passaporte_passageiro LIKE ?) AND (? IS NULL OR telefone_passageiro LIKE ?) AND (? IS NULL OR id_pais_passageiro_fk = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++) pstm.setObject(i, cpfFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, "%" + emailFiltro + "%");
            for (j+=2; i<j; i++) pstm.setObject(i, "%" + nomeFiltro + "%");
            for (j+=2; i<j; i++) pstm.setObject(i, passaporteFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, telefoneFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, idPaisFiltro);

            ResultSet rs = pstm.executeQuery();

            ArrayList<Passageiro> passageiros = new ArrayList<>();
            while (rs.next()) {
                String cpf = rs.getString("cpf_passageiro_pk");
                String email = rs.getString("email_passageiro");
                String nome = rs.getString("nome_passageiro");
                String senha = rs.getString("senha_passageiro");
                String passaporte = rs.getString("passaporte_passageiro");
                String telefone = rs.getString("telefone_passageiro");
                int idPais = rs.getInt("id_pais_passageiro_fk");

                Passageiro passageiro = new Passageiro(cpf, email, nome, senha, passaporte, telefone, idPais);
                passageiros.add(passageiro);
            }

            return Resultado.sucesso("Passageiros carregados", passageiros);

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