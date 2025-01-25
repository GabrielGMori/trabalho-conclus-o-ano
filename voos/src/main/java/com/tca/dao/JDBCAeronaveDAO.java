package com.tca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.tca.dao.interfaces.AeronaveDAO;
import com.tca.model.Aeronave;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCAeronaveDAO implements AeronaveDAO {
    private FabricaConexoes fabrica;

    public JDBCAeronaveDAO(FabricaConexoes fabrica) {
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
                    "SELECT * FROM Aeronave WHERE (? IS NULL OR modelo_aeronave LIKE ?) AND (? IS NULL OR capacidade_aeronave = ?) AND (? IS NULL OR assentos_por_fileira_aeronave = ?) AND (? IS NULL OR id_companhia_aeronave_fk = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++) pstm.setObject(i, "%" + modeloFiltro + "%");
            for (j+=2; i<j; i++) pstm.setObject(i, capacidadeFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, assentosPorFileroFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, idCompanhiaFiltro);

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

    public Resultado verificarDisponibilidade(Integer id, LocalDate dataInicial, LocalDate dataFinal) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT verificarDisponibilidadeAeronaveFunc(?, ?, ?);");
            
            pstm.setInt(1, id);
            pstm.setDate(2, Date.valueOf(dataInicial));
            pstm.setDate(3, Date.valueOf(dataFinal));

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