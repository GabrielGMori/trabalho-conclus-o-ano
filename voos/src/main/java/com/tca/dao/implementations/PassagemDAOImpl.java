package com.tca.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.interfaces.PassagemDAO;
import com.tca.model.Passagem;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class PassagemDAOImpl implements PassagemDAO {
    private FabricaConexoes fabrica;

    public PassagemDAOImpl(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Passagem passagem) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Passagem(data_compra_passagem, cpf_passageiro_passagem_fk, id_voo_passagem_fk, id_metodo_pagamento_passagem_fk) VALUES (?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setTimestamp(1, Timestamp.valueOf(passagem.getDataCompra()));
            pstm.setString(2, passagem.getCpfPassageiro());
            pstm.setInt(3, passagem.getIdVoo());
            pstm.setInt(4, passagem.getIdMetodoPagamento());

            int ret = pstm.executeUpdate();

            if(ret == 1){
                int id = DBUtils.getLastId(pstm);
                passagem.setId(id);
                return Resultado.sucesso("Passagem criada com sucesso", passagem);
            }

            return Resultado.erro("Algo deu errado, a passagem não foi criada");

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
            pstm = con.prepareStatement("SELECT * FROM Passagem WHERE id_passagem_pk = ?;");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                LocalDateTime dataCompra = rs.getTimestamp("data_compra_passagem").toLocalDateTime();
                String cpf = rs.getString("cpf_passageiro_passagem_fk");
                int idVoo = rs.getInt("id_voo_passagem_fk");
                int idMetodoPagamento = rs.getInt("id_metodo_pagamento_passagem_fk");
                int idCheckIn = rs.getInt("id_checkin_passagem_fk");

                Passagem passagem = new Passagem(id, dataCompra, cpf, idVoo, idMetodoPagamento);
                passagem.setIdCheckIn(idCheckIn);
                return Resultado.sucesso("Passagem carregada", passagem);
            }

            return Resultado.erro("Passagem não encontrada");

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
            pstm = con.prepareStatement("SELECT * FROM Passagem;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Passagem> passagens = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_passagem_pk");
                LocalDateTime dataCompra = rs.getTimestamp("data_compra_passagem").toLocalDateTime();
                String cpf = rs.getString("cpf_passageiro_passagem_fk");
                int idVoo = rs.getInt("id_voo_passagem_fk");
                int idMetodoPagamento = rs.getInt("id_metodo_pagamento_passagem_fk");
                int idCheckIn = rs.getInt("id_checkin_passagem_fk");

                Passagem passagem = new Passagem(id, dataCompra, cpf, idVoo, idMetodoPagamento);
                passagem.setIdCheckIn(idCheckIn);
                passagens.add(passagem);
            }

            return Resultado.sucesso("Passagens carregadas", passagens);

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
    public Resultado getPassagensFiltro(Integer idVooFiltro, String numeroVooFiltro, String origemFiltro, String destinoFiltro, String cpfPassageiroFiltro, Integer idMetodoPagamentoFiltro, LocalDateTime dataInicioFiltro, LocalDateTime dataFimFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Passagem passagem JOIN Voo voo ON passagem.id_voo_passagem_fk = voo.id_voo_pk WHERE (? IS NULL OR id_voo_passagem_fk = ?) AND (? IS NULL OR voo.numero_voo = ?)  AND (? IS NULL OR voo.origem_voo = ?) AND (? IS NULL OR voo.destino_voo = ?) AND (? IS NULL OR cpf_passageiro_passagem_fk = ?) AND (? IS NULL OR id_metodo_pagamento_passagem_fk = ?) AND (? IS NULL OR data_compra_passagem >= ?) AND (? IS NULL OR data_compra_passagem <= ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++)  {if (idVooFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setInt(i, idVooFiltro); } 
            for (j+=2; i<j; i++)  {if (numeroVooFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, numeroVooFiltro); } 
            for (j+=2; i<j; i++)  {if (origemFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, origemFiltro); }
            for (j+=2; i<j; i++)  {if (destinoFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, destinoFiltro); }
            for (j+=2; i<j; i++)  {if (cpfPassageiroFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, cpfPassageiroFiltro); }
            for (j+=2; i<j; i++)  {if (idMetodoPagamentoFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setInt(i, idMetodoPagamentoFiltro); }
            for (j+=2; i<j; i++)  {if (dataInicioFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(dataInicioFiltro)); }
            for (j+=2; i<j; i++)  {if (dataFimFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(dataFimFiltro)); }

            ResultSet rs = pstm.executeQuery();

            ArrayList<Passagem> passagens = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_passagem_pk");
                LocalDateTime dataCompra = rs.getTimestamp("data_compra_passagem").toLocalDateTime();
                String cpf = rs.getString("cpf_passageiro_passagem_fk");
                int idVoo = rs.getInt("id_voo_passagem_fk");
                int idMetodoPagamento = rs.getInt("id_metodo_pagamento_passagem_fk");
                int idCheckIn = rs.getInt("id_checkin_passagem_fk");

                Passagem passagem = new Passagem(id, dataCompra, cpf, idVoo, idMetodoPagamento);
                passagem.setIdCheckIn(idCheckIn);
                passagens.add(passagem);
            }

            return Resultado.sucesso("Passagens carregadas", passagens);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    public Resultado realizarCheckIn(Integer id) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("CALL realizarCheckInProc(?);");
            
            pstm.setInt(1, id);

            int ret = pstm.executeUpdate();

            if(ret == 1){
                return Resultado.sucesso("Check-in criado com sucesso", null);
            }

            return Resultado.erro("Algo deu errado, o check-in não foi criado");

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
