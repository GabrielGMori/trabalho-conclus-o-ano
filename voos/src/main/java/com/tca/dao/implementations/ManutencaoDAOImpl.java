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
import com.tca.dao.interfaces.ManutencaoDAO;
import com.tca.model.Manutencao;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class ManutencaoDAOImpl implements ManutencaoDAO {
    private FabricaConexoes fabrica;

    public ManutencaoDAOImpl(FabricaConexoes fabrica) {
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
            pstm.setTimestamp(2, Timestamp.valueOf(manutencao.getDataInicio()));
            pstm.setTimestamp(3, Timestamp.valueOf(manutencao.getDataFim()));
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
    public Resultado get(Integer id) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT * FROM Manutencao WHERE id_manutencao_pk = ?;");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String descricao = rs.getString("descricao_manutencao");
                LocalDateTime dataInicio = rs.getTimestamp("data_inicio_manutencao").toLocalDateTime();
                LocalDateTime dataFim = rs.getTimestamp("data_fim_manutencao").toLocalDateTime();
                String status = rs.getString("status_manutencao");
                int idAeronave = rs.getInt("id_aeronave_manutencao_fk");
                
                Manutencao manutencao = new Manutencao(id, descricao, dataInicio, dataFim, status, idAeronave);
                return Resultado.sucesso("Manutenção carregada", manutencao);
            }

            return Resultado.erro("Manutenção não encontrada");

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
                LocalDateTime dataInicio = rs.getTimestamp("data_inicio_manutencao").toLocalDateTime();
                LocalDateTime dataFim = rs.getTimestamp("data_fim_manutencao").toLocalDateTime();
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
            pstm.setTimestamp(2, Timestamp.valueOf(manutencao.getDataInicio()));
            pstm.setTimestamp(3, Timestamp.valueOf(manutencao.getDataFim()));
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
    public Resultado getManutencoesFiltro(String descricaoFiltro, LocalDateTime dataInicioInicialFiltro, LocalDateTime dataInicioFinalFiltro, LocalDateTime dataFimInicialFiltro, LocalDateTime dataFimFinalFiltro, String aeronaveFiltro, String statusFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Manutencao manutencao JOIN Aeronave aeronave ON manutencao.id_aeronave_manutencao_fk = aeronave.id_aeronave_pk WHERE (? IS NULL OR descricao_manutencao LIKE ?) AND (? IS NULL OR data_inicio_manutencao >= ?) AND (? IS NULL OR data_inicio_manutencao <= ?) AND (? IS NULL OR data_fim_manutencao >= ?) AND (? IS NULL OR data_fim_manutencao <= ?) AND (? IS NULL OR aeronave.modelo_aeronave = ?) AND (? IS NULL OR status_manutencao = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++)  {if (descricaoFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, "%" + descricaoFiltro + "%"); } 
            for (j+=2; i<j; i++)  {if (dataInicioInicialFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(dataInicioInicialFiltro)); } 
            for (j+=2; i<j; i++)  {if (dataInicioFinalFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(dataInicioFinalFiltro)); }
            for (j+=2; i<j; i++)  {if (dataFimInicialFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(dataFimInicialFiltro)); }
            for (j+=2; i<j; i++)  {if (dataFimFinalFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(dataFimFinalFiltro)); }
            for (j+=2; i<j; i++)  {if (aeronaveFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, aeronaveFiltro); }
            for (j+=2; i<j; i++)  {if (statusFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, statusFiltro); }

            ResultSet rs = pstm.executeQuery();

            ArrayList<Manutencao> manutencoes = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_manutencao_pk");
                String descricao = rs.getString("descricao_manutencao");
                LocalDateTime dataInicio = rs.getTimestamp("data_inicio_manutencao").toLocalDateTime();
                LocalDateTime dataFim = rs.getTimestamp("data_fim_manutencao").toLocalDateTime();
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
