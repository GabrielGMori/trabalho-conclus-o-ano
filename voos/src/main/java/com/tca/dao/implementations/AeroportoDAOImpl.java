package com.tca.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.interfaces.AeroportoDAO;
import com.tca.model.Aeroporto;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class AeroportoDAOImpl implements AeroportoDAO {
    private FabricaConexoes fabrica;

    public AeroportoDAOImpl(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Aeroporto aeroporto) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Aeroporto(nome_aeroporto, localizacao_aeroporto) VALUES (?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, aeroporto.getNome());
            pstm.setString(2, aeroporto.getLocalizacao());

            int ret = pstm.executeUpdate();

            if(ret == 1){
                int id = DBUtils.getLastId(pstm);
                aeroporto.setId(id);
                return Resultado.sucesso("Aeroporto criado com sucesso", aeroporto);
            }

            return Resultado.erro("Algo deu errado, o aeroporto não foi criado");

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
    public Resultado get(Integer id) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT * FROM Aeroporto WHERE id_aeroporto_pk = ?;");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome_aeroporto");
                String localizacao = rs.getString("localizacao_aeroporto");

                Aeroporto aeroporto = new Aeroporto(id, nome, localizacao);
                return Resultado.sucesso("Aeroporto carregado", aeroporto);
            }

            return Resultado.erro("Aeroporto não encontrado");

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
            pstm = con.prepareStatement("SELECT * FROM Aeroporto;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Aeroporto> aeroportos = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_aeroporto_pk");
                String nome = rs.getString("nome_aeroporto");
                String localizacao = rs.getString("localizacao_aeroporto");

                Aeroporto aeroporto = new Aeroporto(id, nome, localizacao);
                aeroportos.add(aeroporto);
            }

            return Resultado.sucesso("Aeroportos carregados", aeroportos);

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
    public Resultado atualizar(Integer id, Aeroporto aeroporto) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "UPDATE Aeroporto SET nome_aeroporto = ?, localizacao_aeroporto = ? WHERE id_aeroporto_pk = ?;");

            pstm.setString(1, "%" + aeroporto.getNome() + "%");
            pstm.setString(2, "%" + aeroporto.getLocalizacao() + "%");
            pstm.setInt(3, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Aeroporto atualizado", aeroporto);
            }

            return Resultado.erro("Algo deu errado, o aeroporto não foi atualizado");

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
    public Resultado deletar(Integer id) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("DELETE FROM Aeroporto WHERE id_aeroporto_pk = ?;");

            pstm.setInt(1, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Aeroporto removido", null);
            }

            return Resultado.erro("Algo deu errado, o aeroporto não foi removido");

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
    public Resultado getAeroportosFiltro(String nomeFiltro, String localizacaoFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Aeroporto WHERE (? IS NULL OR nome_aeroporto = ?) AND (? IS NULL OR localizacao_aeroporto = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++)  {if (nomeFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, nomeFiltro); } 
            for (j+=2; i<j; i++)  {if (localizacaoFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, localizacaoFiltro); }

            ResultSet rs = pstm.executeQuery();

            ArrayList<Aeroporto> aeroportos = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_aeroporto_pk");
                String nome = rs.getString("nome_aeroporto");
                String localizacao = rs.getString("localizacao_aeroporto");

                Aeroporto aeroporto = new Aeroporto(id, nome, localizacao);
                aeroportos.add(aeroporto);
            }

            return Resultado.sucesso("Aeronaves carregadas", aeroportos);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    public Resultado verificarAeroportoEmUso(Integer id) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT verificarAeroportoPortoesEmUsoFunc(?);");
            
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                boolean disponivel = rs.getBoolean(1);
                return Resultado.sucesso("Disponibilidade verificada", disponivel);
            }

            return Resultado.erro("Algo deu errado, a disponibilidade não pode ser verificada");

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
