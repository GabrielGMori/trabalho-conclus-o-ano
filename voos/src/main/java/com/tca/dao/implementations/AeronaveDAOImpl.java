package com.tca.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.interfaces.AeronaveDAO;
import com.tca.model.Aeronave;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class AeronaveDAOImpl implements AeronaveDAO {
    private FabricaConexoes fabrica;

    public AeronaveDAOImpl(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Aeronave aeronave) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Aeronave(modelo_aeronave, capacidade_aeronave, id_companhia_aeronave_fk) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, aeronave.getModelo());
            pstm.setInt(2, aeronave.getCapacidade());
            pstm.setInt(3, aeronave.getIdCompanhiaAerea());

            int ret = pstm.executeUpdate();

            if(ret == 1){
                int id = DBUtils.getLastId(pstm);
                aeronave.setId(id);
                return Resultado.sucesso("Aeronave criada com sucesso", aeronave);
            }

            return Resultado.erro("Algo deu errado, a aeronave não foi criada");

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
            pstm = con.prepareStatement("SELECT * FROM Aeronave WHERE id_aeronave_pk = ?;");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String modelo = rs.getString("modelo_aeronave");
                int capacidade = rs.getInt("capacidade_aeronave");
                int idCompanhiaAerea = rs.getInt("id_companhia_aeronave_fk");

                Aeronave aeronave = new Aeronave(id, modelo, capacidade, idCompanhiaAerea);
                return Resultado.sucesso("Aeronave carregada", aeronave);
            }

            return Resultado.erro("Aeronave não encontrada");

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
            pstm = con.prepareStatement("SELECT * FROM Aeronave;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Aeronave> aeronaves = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_aeronave_pk");
                String modelo = rs.getString("modelo_aeronave");
                int capacidade = rs.getInt("capacidade_aeronave");
                int idCompanhiaAerea = rs.getInt("id_companhia_aeronave_fk");

                Aeronave aeronave = new Aeronave(id, modelo, capacidade, idCompanhiaAerea);
                aeronaves.add(aeronave);
            }

            return Resultado.sucesso("Aeronaves carregadas", aeronaves);

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
    public Resultado atualizar(Integer id, Aeronave aeronave) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "UPDATE Aeronave SET modelo_aeronave = ?, capacidade_aeronave = ?, id_companhia_aeronave_fk = ? WHERE id_aeronave_pk = ?;");

            pstm.setString(1, aeronave.getModelo());
            pstm.setInt(2, aeronave.getCapacidade());
            pstm.setInt(3, aeronave.getIdCompanhiaAerea());
            pstm.setInt(4, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Aeronave atualizada", aeronave);
            }

            return Resultado.erro("Algo deu errado, a aeronave não foi atualizada");

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
            pstm = con.prepareStatement("DELETE FROM Aeronave WHERE id_aeronave_pk = ?;");

            pstm.setInt(1, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Aeronave removida", null);
            }

            return Resultado.erro("Algo deu errado, a aeronave não foi removida");

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
    public Resultado getAeronavesFiltro(String modeloFiltro, Integer capacidadeFiltro, String companhiaFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Aeronave aeronave JOIN Companhia_Aerea companhia ON aeronave.id_companhia_aeronave_fk = companhia.id_companhia_pk WHERE (? IS NULL OR modelo_aeronave = ?) AND (? IS NULL OR capacidade_aeronave = ?) AND (? IS NULL OR companhia.nome_companhia = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++)  {if (modeloFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, modeloFiltro); } 
            for (j+=2; i<j; i++)  {if (capacidadeFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setInt(i, capacidadeFiltro); }
            for (j+=2; i<j; i++)  {if (companhiaFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, companhiaFiltro); }

            ResultSet rs = pstm.executeQuery();

            ArrayList<Aeronave> aeronaves = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_aeronave_pk");
                String modelo = rs.getString("modelo_aeronave");
                int capacidade = rs.getInt("capacidade_aeronave");
                int idCompanhiaAerea = rs.getInt("id_companhia_aeronave_fk");

                Aeronave aeronave = new Aeronave(id, modelo, capacidade, idCompanhiaAerea);
                aeronaves.add(aeronave);
            }

            return Resultado.sucesso("Aeronaves carregadas", aeronaves);

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
