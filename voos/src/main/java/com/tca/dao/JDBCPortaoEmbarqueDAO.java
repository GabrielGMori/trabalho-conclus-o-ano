package com.tca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.tca.dao.interfaces.PortaoEmbarqueDAO;
import com.tca.model.PortaoEmbarque;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCPortaoEmbarqueDAO implements PortaoEmbarqueDAO {
    private FabricaConexoes fabrica;

    public JDBCPortaoEmbarqueDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(PortaoEmbarque portaoEmbarque) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Portao_Embarque(codigo_portao, disponivel_portao, id_aeroporto_portao_fk) VALUES (?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, portaoEmbarque.getCodigo());
            pstm.setBoolean(2, portaoEmbarque.getDisponivel());
            pstm.setInt(2, portaoEmbarque.getIdAeroporto());

            int ret = pstm.executeUpdate();

            if(ret == 1){
                int id = DBUtils.getLastId(pstm);
                portaoEmbarque.setId(id);
                return Resultado.sucesso("Portão de embarque criado com sucesso", portaoEmbarque);
            }

            return Resultado.erro("Algo deu errado, o portão de mbarque não foi criado");

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
            pstm = con.prepareStatement("SELECT * FROM Portao_Embarque;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<PortaoEmbarque> portoesEmbarque = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_portao_pk");
                String codigo = rs.getString("codigo_portao");
                Boolean disponivel = rs.getBoolean("disponivel_portao");
                int idAeroporto = rs.getInt("id_aeroporto_portao_fk");

                PortaoEmbarque portaoEmbarque = new PortaoEmbarque(id, codigo, disponivel, idAeroporto);
                portoesEmbarque.add(portaoEmbarque);
            }

            return Resultado.sucesso("Portões de embarque carregados", portoesEmbarque);

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
    public Resultado atualizar(Integer id, PortaoEmbarque portaoEmbarque) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "UPDATE Portao_Embarque SET codigo_portao = ?, disponivel_portao = ?, id_aeroporto_portao_fk = ? WHERE id_portao_pk = ?;");

            pstm.setString(1, portaoEmbarque.getCodigo());
            pstm.setBoolean(2, portaoEmbarque.getDisponivel());
            pstm.setInt(3, portaoEmbarque.getIdAeroporto());
            pstm.setInt(4, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Portão de mbarque atualizado", portaoEmbarque);
            }

            return Resultado.erro("Algo deu errado, o portão de mbarque não foi atualizado");

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
            pstm = con.prepareStatement("DELETE FROM Portao_Embarque WHERE id_portaoEmbarque_pk = ?;");

            pstm.setInt(1, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Portão de embarque removido", null);
            }

            return Resultado.erro("Algo deu errado, o portão de embarque não foi removido");

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
    public Resultado getPortoesEmbarqueFiltro(String codigoFiltro, Boolean disponivelFiltro, Integer idAeroportoFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Portao_Embarque WHERE (? IS NULL OR codigo_portao LIKE ?) AND (? IS NULL OR disponivel_portao = ?) AND (? IS NULL OR id_aeroporto_portao_fk = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++) pstm.setObject(i, "%" + codigoFiltro + "%");
            for (j+=2; i<j; i++) pstm.setObject(i, disponivelFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, idAeroportoFiltro);

            ResultSet rs = pstm.executeQuery();

            ArrayList<PortaoEmbarque> portoesEmbarque = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_portao_pk");
                String codigo = rs.getString("codigo_portao");
                Boolean disponivel = rs.getBoolean("disponivel_portao");
                int idAeroporto = rs.getInt("id_aeroporto_portao_fk");

                PortaoEmbarque portaoEmbarque = new PortaoEmbarque(id, codigo, disponivel, idAeroporto);
                portoesEmbarque.add(portaoEmbarque);
            }

            return Resultado.sucesso("Portões de embarque carregados", portoesEmbarque);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    public Resultado verificarEmUso(Integer id, LocalDate dataInicial, LocalDate dataFinal) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT verificarPortaoEmbarqueEmUsoFunc(?, ?, ?);");
            
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