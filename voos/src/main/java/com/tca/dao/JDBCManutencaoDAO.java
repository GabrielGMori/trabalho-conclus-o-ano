package com.tca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.tca.dao.interfaces.ManutencaoDAO;
import com.tca.model.Manutencao;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCManutencaoDAO implements ManutencaoDAO {
    private FabricaConexoes fabrica;

    public JDBCManutencaoDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Manutencao manutencao) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Manutencao(descricao_manutencao, data_inicio_manutencao, data_fim_manutencao, status_manutencao, id_aeronave_manutencao_fk) VALUES (?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, manutencao.getDescricao());
            pstm.setDate(2, Date.valueOf(manutencao.getDataInicio()));
            pstm.setDate(3, Date.valueOf(manutencao.getDataFim()));
            pstm.setString(4, manutencao.getStatus());
            pstm.setInt(5, manutencao.getIdAeronave());

            int ret = pstm.executeUpdate();

            if(ret == 1){
                int id = DBUtils.getLastId(pstm);
                manutencao.setId(id);
                return Resultado.sucesso("Manutenção criada com sucesso", manutencao);
            }

            return Resultado.erro("Algo deu errado, a manutenção não foi criada");

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
            pstm = con.prepareStatement("SELECT * FROM Manutencao");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Manutencao> manutencoes = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_manutencao_pk");
                String descricao = rs.getString("descricao_manutencao");
                LocalDate dataInicio = rs.getDate("data_inicio_manutencao").toLocalDate();
                LocalDate dataFim = rs.getDate("data_fim_manutencao").toLocalDate();
                String status = rs.getString("status_manutencao");
                int idAeronave = rs.getInt("id_aeronave_manutencao_fk");

                Manutencao manutencao = new Manutencao(id, descricao, dataInicio, dataFim, status, idAeronave);
                manutencoes.add(manutencao);
            }

            return Resultado.sucesso("Manutenções carregadas", manutencoes);

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
    public Resultado atualizar(Integer id, Manutencao manutencao) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "UPDATE Manutencao SET descricao_manutencao = ?, data_inicio_manutencao = ?, data_fim_manutencao = ?, status_manutencao = ?, id_aeronave_manutencao_fk = ? WHERE id_manutencao_pk = ?;");

            pstm.setString(1, manutencao.getDescricao());
            pstm.setDate(2, Date.valueOf(manutencao.getDataInicio()));
            pstm.setDate(3, Date.valueOf(manutencao.getDataFim()));
            pstm.setString(4, manutencao.getStatus());
            pstm.setInt(5, manutencao.getIdAeronave());
            pstm.setInt(6, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Manutenção atualizada", manutencao);
            }

            return Resultado.erro("Algo deu errado, a manutenção não foi atualizada");

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
            pstm = con.prepareStatement("DELETE FROM Manutencao WHERE id_manutencao_pk = ?;");

            pstm.setInt(1, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Manutenção removida", null);
            }

            return Resultado.erro("Algo deu errado, a manutenção não foi removida");

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
    public Resultado getManutencoesFiltro(String descricaoFiltro, LocalDate dataInicioFiltro, LocalDate dataFimFiltro, String statusFiltro, Integer idAeronaveFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Manutencao WHERE (? IS NULL OR descricao_manutencao LIKE ?) AND (? IS NULL OR data_inicio_manutencao <= ?) AND (? IS NULL OR data_fim_manutencao >= ?) AND (? IS NULL OR status_manutencao = ?) AND (? IS NULL OR id_aeronave_manutencao_fk = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++) pstm.setObject(i, "%" + descricaoFiltro + "%");
            for (j+=2; i<j; i++) pstm.setObject(i, dataInicioFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, dataFimFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, statusFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, idAeronaveFiltro);

            ResultSet rs = pstm.executeQuery();

            ArrayList<Manutencao> manutencoes = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_manutencao_pk");
                String descricao = rs.getString("descricao_manutencao");
                LocalDate dataInicio = rs.getDate("data_inicio_manutencao").toLocalDate();
                LocalDate dataFim = rs.getDate("data_fim_manutencao").toLocalDate();
                String status = rs.getString("status_manutencao");
                int idAeronave = rs.getInt("id_aeronave_manutencao_fk");

                Manutencao manutencao = new Manutencao(id, descricao, dataInicio, dataFim, status, idAeronave);
                manutencoes.add(manutencao);
            }

            return Resultado.sucesso("Manutenções carregadas", manutencoes);

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