package com.tca.dao.implementations;

import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
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
                    "INSERT INTO Aeronave(modelo_aeronave, capacidade_aeronave, assentos_por_fileira_aeronave, id_companhia_aeronave_fk) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, aeronave.getModelo());
            pstm.setInt(2, aeronave.getCapacidade());
            pstm.setInt(3, aeronave.getAssentosPorFileira());
            pstm.setInt(4, aeronave.getIdCompanhiaAerea());

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
                int assentosPorFileira = rs.getInt("assentos_por_fileira_aeronave");
                int idCompanhiaAerea = rs.getInt("id_companhia_aeronave_fk");

                Aeronave aeronave = new Aeronave(id, modelo, capacidade, assentosPorFileira, idCompanhiaAerea);
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
                int assentosPorFileira = rs.getInt("assentos_por_fileira_aeronave");
                int idCompanhiaAerea = rs.getInt("id_companhia_aeronave_fk");

                Aeronave aeronave = new Aeronave(id, modelo, capacidade, assentosPorFileira, idCompanhiaAerea);
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
                    "UPDATE Aeronave SET modelo_aeronave = ?, capacidade_aeronave = ?, assentos_por_fileira_aeronave = ?, id_companhia_aeronave_fk = ? WHERE id_aeronave_pk = ?;");

            pstm.setString(1, aeronave.getModelo());
            pstm.setInt(2, aeronave.getCapacidade());
            pstm.setInt(3, aeronave.getAssentosPorFileira());
            pstm.setInt(4, aeronave.getIdCompanhiaAerea());
            pstm.setInt(5, id);

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
    public Resultado getAeronavesFiltro(String modeloFiltro, Integer capacidadeFiltro, Integer assentosPorFileroFiltro, Integer idCompanhiaFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Aeronave WHERE (? IS NULL OR modelo_aeronave = ?) AND (? IS NULL OR capacidade_aeronave = ?) AND (? IS NULL OR assentos_por_fileira_aeronave = ?) AND (? IS NULL OR id_companhia_aeronave_fk = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++)  {if (modeloFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, modeloFiltro); } 
            for (j+=2; i<j; i++)  {if (capacidadeFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setInt(i, capacidadeFiltro); }
            for (j+=2; i<j; i++)  {if (assentosPorFileroFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setInt(i, assentosPorFileroFiltro); }
            for (j+=2; i<j; i++)  {if (idCompanhiaFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setInt(i, idCompanhiaFiltro); }

            ResultSet rs = pstm.executeQuery();

            ArrayList<Aeronave> aeronaves = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_aeronave_pk");
                String modelo = rs.getString("modelo_aeronave");
                int capacidade = rs.getInt("capacidade_aeronave");
                int assentosPorFileira = rs.getInt("assentos_por_fileira_aeronave");
                int idCompanhiaAerea = rs.getInt("id_companhia_aeronave_fk");

                Aeronave aeronave = new Aeronave(id, modelo, capacidade, assentosPorFileira, idCompanhiaAerea);
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

    public Resultado verificarDisponibilidade(Integer id, LocalDateTime dataInicial, LocalDateTime dataFinal) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT verificarDisponibilidadeAeronaveFunc(?, ?, ?);");
            
            pstm.setInt(1, id);
            pstm.setTimestamp(2, Timestamp.valueOf(dataInicial));
            pstm.setTimestamp(3, Timestamp.valueOf(dataFinal));

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
