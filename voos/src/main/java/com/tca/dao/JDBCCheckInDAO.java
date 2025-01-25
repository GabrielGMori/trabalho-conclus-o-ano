package com.tca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.tca.dao.interfaces.CheckInDAO;
import com.tca.model.CheckIn;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCCheckInDAO implements CheckInDAO {
    private FabricaConexoes fabrica;

    public JDBCCheckInDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(CheckIn checkIn) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO CheckIn(data_checkin) VALUES (?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setDate(1, Date.valueOf(checkIn.getData()));

            int ret = pstm.executeUpdate();

            if(ret == 1){
                int id = DBUtils.getLastId(pstm);
                checkIn.setId(id);
                return Resultado.sucesso("Check-in criado com sucesso", checkIn);
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

    @Override
    public Resultado listar() throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT * FROM CheckIn;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<CheckIn> checkIns = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_checkIn_pk");
                LocalDate data = rs.getDate("data_checkin").toLocalDate();

                CheckIn checkIn = new CheckIn(id, data);
                checkIns.add(checkIn);
            }

            return Resultado.sucesso("Check-ins carregados", checkIns);

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