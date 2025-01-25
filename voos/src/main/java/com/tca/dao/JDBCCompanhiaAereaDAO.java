package com.tca.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tca.dao.interfaces.CompanhiaAereaDAO;
import com.tca.model.CompanhiaAerea;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCCompanhiaAereaDAO implements CompanhiaAereaDAO {
    private FabricaConexoes fabrica;

    public JDBCCompanhiaAereaDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(CompanhiaAerea companhiaAerea) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Companhia_Aerea(codigo_icao_companhia, nome_companhia) VALUES (?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, companhiaAerea.getCodigoIcao());
            pstm.setString(2, companhiaAerea.getNome());

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                int id = DBUtils.getLastId(pstm);
                companhiaAerea.setId(id);
                return Resultado.sucesso("Companhia aérea criada com sucesso", companhiaAerea);
            }

            return Resultado.erro("Algo deu errado, a companhia aérea não foi criada");

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
            pstm = con.prepareStatement("SELECT * FROM Companhia_Aerea;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<CompanhiaAerea> companhiasAereas = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_companhia_pk");
                String codigoIcao = rs.getString("codigo_icao_companhia");
                String nome = rs.getString("nome_companhia");

                CompanhiaAerea companhiaAerea = new CompanhiaAerea(id, codigoIcao, nome);
                companhiasAereas.add(companhiaAerea);
            }

            return Resultado.sucesso("Companhia aéreas carregadas", companhiasAereas);

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
    public Resultado atualizar(Integer id, CompanhiaAerea companhiaAerea) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "UPDATE Companhia_Aerea SET codigo_icao_companhia = ?, nome_companhia = ? WHERE id_companhia_pk = ?;");

            pstm.setString(1, companhiaAerea.getCodigoIcao());
            pstm.setString(2, companhiaAerea.getNome());
            pstm.setInt(3, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Companhia aérea atualizada", companhiaAerea);
            }

            return Resultado.erro("Algo deu errado, a companhia aérea não foi atualizada");

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
            pstm = con.prepareStatement("DELETE FROM Companhia_Aerea WHERE id_companhia_pk = ?;");

            pstm.setInt(1, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("CompanhiaAerea removida", null);
            }

            return Resultado.erro("Algo deu errado, a companhia aérea  não foi removida");

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
    public Resultado getCompanhiasFiltro(String codigoIcaoFiltro, String nomeFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Companhia_Aerea WHERE (? IS NULL OR codigo_icao_companhia = ?) AND (? IS NULL OR nome_companhia LIKE ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++) pstm.setObject(i, codigoIcaoFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, "%" + nomeFiltro + "%");

            ResultSet rs = pstm.executeQuery();

            ArrayList<CompanhiaAerea> companhiasAereas = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_companhia_pk");
                String codigoIcao = rs.getString("codigo_icao_companhia");
                String nome = rs.getString("nome_companhia");

                CompanhiaAerea companhiaAerea = new CompanhiaAerea(id, codigoIcao, nome);
                companhiasAereas.add(companhiaAerea);
            }

            return Resultado.sucesso("Companhia aéreas carregadas", companhiasAereas);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    public Resultado verificarDisponibilidadeCompanhia(Integer id) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT verificarCompanhiaDisponibilidadeAeronavesFunc(?);");
            
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