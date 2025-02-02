package com.tca.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.interfaces.PortaoEmbarqueDAO;
import com.tca.model.PortaoEmbarque;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class PortaoEmbarqueDAOImpl implements PortaoEmbarqueDAO {
    private FabricaConexoes fabrica;

    public PortaoEmbarqueDAOImpl(FabricaConexoes fabrica) {
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
            pstm.setInt(3, portaoEmbarque.getIdAeroporto());

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
    public Resultado get(Integer id) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT * FROM Portao_Embarque WHERE id_portao_pk = ?;");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String codigo = rs.getString("codigo_portao");
                Boolean disponivel = rs.getBoolean("disponivel_portao");
                int idAeroporto = rs.getInt("id_aeroporto_portao_fk");

                PortaoEmbarque portaoEmbarque = new PortaoEmbarque(id, codigo, disponivel, idAeroporto);
                return Resultado.sucesso("Portão de embarque carregado", portaoEmbarque);
            }

            return Resultado.erro("Portão de embarque não encontrado");

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
            pstm = con.prepareStatement("DELETE FROM Portao_Embarque WHERE id_portao_pk = ?;");

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
                    "SELECT * FROM Portao_Embarque WHERE (? IS NULL OR codigo_portao = ?) AND (? IS NULL OR disponivel_portao = ?) AND (? IS NULL OR id_aeroporto_portao_fk = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++)  {if (codigoFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, codigoFiltro); } 
            for (j+=2; i<j; i++)  {if (disponivelFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setBoolean(i, disponivelFiltro); }
            for (j+=2; i<j; i++)  {if (idAeroportoFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setInt(i, idAeroportoFiltro); }

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

}
