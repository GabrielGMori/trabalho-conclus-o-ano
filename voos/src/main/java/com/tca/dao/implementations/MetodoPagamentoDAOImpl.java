package com.tca.dao.implementations;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.interfaces.MetodoPagamentoDAO;
import com.tca.model.MetodoPagamento;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class MetodoPagamentoDAOImpl implements MetodoPagamentoDAO {
    private FabricaConexoes fabrica;

    public MetodoPagamentoDAOImpl(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(MetodoPagamento metodoPagamento) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Metodo_Pagamento(metodo_pagamento) VALUES (?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setDate(1, Date.valueOf(metodoPagamento.getMetodo()));

            int ret = pstm.executeUpdate();

            if(ret == 1){
                int id = DBUtils.getLastId(pstm);
                metodoPagamento.setId(id);
                return Resultado.sucesso("Método de pagamento criado com sucesso", metodoPagamento);
            }

            return Resultado.erro("Algo deu errado, o método de pagamento não foi criado");

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
            pstm = con.prepareStatement("SELECT * FROM Metodo_Pagamento WHERE id_metodo_pagamento_pk = ?;");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String metodo = rs.getString("metodo_pagamento");

                MetodoPagamento metodoPagamento = new MetodoPagamento(id, metodo);
                return Resultado.sucesso("Método de pagamento carregado", metodoPagamento);
            }

            return Resultado.erro("Método de pagamento não encontrado");

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
            pstm = con.prepareStatement("SELECT * FROM Metodo_Pagamento;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<MetodoPagamento> metodosPagamento = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_metodo_pagamento_pk");
                String metodo = rs.getString("metodo_pagamento");

                MetodoPagamento metodoPagamento = new MetodoPagamento(id, metodo);
                metodosPagamento.add(metodoPagamento);
            }

            return Resultado.sucesso("Métodos de pagamento carregados", metodosPagamento);

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
