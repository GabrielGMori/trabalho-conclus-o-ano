package com.tca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

import com.tca.dao.interfaces.VooDAO;
import com.tca.model.Voo;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class JDBCVooDAO implements VooDAO {
    private FabricaConexoes fabrica;

    public JDBCVooDAO(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Voo voo) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Voo(numero_voo, status_voo, origem_voo, destino_voo, horario_embarque_voo, horario_desembarque_voo, id_aeronave_voo_fk, id_portao_embarque_voo_fk, id_aeroporto_chegada_voo_fk) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, voo.getNumero());
            pstm.setString(2, voo.getStatus());
            pstm.setString(3, voo.getOrigem());
            pstm.setString(4, voo.getDestino());
            pstm.setDate(5, Date.valueOf(voo.getHorarioEmbarque()));
            pstm.setDate(6, Date.valueOf(voo.getHorarioDesembarque()));
            pstm.setInt(7, voo.getIdAeronave());
            pstm.setInt(8, voo.getIdPortaoEmbarque());
            pstm.setInt(9, voo.getIdAeroportoChegada());

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                int id = DBUtils.getLastId(pstm);
                voo.setId(id);
                return Resultado.sucesso("Voo criado com sucesso", voo);
            }

            return Resultado.erro("Algo deu errado, o voo não foi criado");

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
            pstm = con.prepareStatement("SELECT * FROM Voo;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Voo> voos = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_voo_pk");
                String numero = rs.getString("numero_voo");
                String status = rs.getString("status_voo");
                String origem = rs.getString("origem_voo");
                String destino = rs.getString("destino_voo");
                LocalDate horarioEmbarque = rs.getDate("horario_embarque_voo").toLocalDate();
                LocalDate horarioDesembarque = rs.getDate("horario_desembarque_voo").toLocalDate();
                int idAeronave = rs.getInt("id_aeronave_voo_fk");
                int idPortaoEmbarque = rs.getInt("id_portao_embarque_voo_fk");
                int idAeroportoChegada = rs.getInt("id_aeroporto_chegada_voo_fk");

                Voo voo = new Voo(id, numero, status, origem, destino, horarioEmbarque, horarioDesembarque, idAeronave,
                        idPortaoEmbarque, idAeroportoChegada);
                voos.add(voo);
            }

            return Resultado.sucesso("Voos carregados", voos);

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
    public Resultado atualizar(Integer id, Voo voo) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "UPDATE Voo SET numero_voo = ?, status_voo = ?, origem_voo = ?, destino_voo = ?, horario_embarque_voo = ?, horario_desembarque_voo = ?, id_aeronave_voo_fk = ?, id_portao_embarque_voo_fk = ?, id_aeroporto_chegada_voo_fk = ? WHERE id_voo_pk = ?;");

            pstm.setString(1, voo.getNumero());
            pstm.setString(2, voo.getStatus());
            pstm.setString(3, voo.getOrigem());
            pstm.setString(4, voo.getDestino());
            pstm.setDate(5, Date.valueOf(voo.getHorarioEmbarque()));
            pstm.setDate(6, Date.valueOf(voo.getHorarioDesembarque()));
            pstm.setInt(7, voo.getIdAeronave());
            pstm.setInt(8, voo.getIdPortaoEmbarque());
            pstm.setInt(9, voo.getIdAeroportoChegada());
            pstm.setInt(10, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Voo atualizado", voo);
            }

            return Resultado.erro("Algo deu errado, o voo não foi atualizado");

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
            pstm = con.prepareStatement("DELETE FROM Voo WHERE id_voo_pk = ?;");

            pstm.setInt(1, id);

            int ret = pstm.executeUpdate();

            if (ret == 1) {
                return Resultado.sucesso("Voo removido", null);
            }

            return Resultado.erro("Algo deu errado, o voo não foi removido");

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
    public Resultado getVoosFiltro(String numeroFiltro, String statusFiltro, String origemFiltro, String destinoFiltro, LocalDate horarioInicialIntervaloFiltro, LocalDate horarioFinalIntervaloFiltro, Integer idAeronaveFiltro, Integer idPortaoEmbarqueFiltro, Integer idAeroportoChegadaFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Aeroporto WHERE (? IS NULL OR numero_voo LIKE ?) AND (? IS NULL OR status_voo = ?) AND (? IS NULL OR origem_voo LIKE ?) AND (? IS NULL OR destino_voo LIKE ?) AND (? IS NULL OR horario_embarque_voo <= ?) AND (? IS NULL OR horario_desembarque_voo >= ?) AND (? IS NULL OR id_aeronave_voo_fk = ?) AND (? IS NULL OR id_portao_embarque_voo_fk = ?) AND (? IS NULL OR id_aeroporto_chegada_voo_fk = ?);");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++) pstm.setObject(i, numeroFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, statusFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, origemFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, destinoFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, horarioInicialIntervaloFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, horarioFinalIntervaloFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, idAeronaveFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, idPortaoEmbarqueFiltro);
            for (j+=2; i<j; i++) pstm.setObject(i, idAeroportoChegadaFiltro);

            ResultSet rs = pstm.executeQuery();

            ArrayList<Voo> voos = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_voo_pk");
                String numero = rs.getString("numero_voo");
                String status = rs.getString("status_voo");
                String origem = rs.getString("origem_voo");
                String destino = rs.getString("destino_voo");
                LocalDate horarioEmbarque = rs.getDate("horario_embarque_voo").toLocalDate();
                LocalDate horarioDesembarque = rs.getDate("horario_desembarque_voo").toLocalDate();
                int idAeronave = rs.getInt("id_aeronave_voo_fk");
                int idPortaoEmbarque = rs.getInt("id_portao_embarque_voo_fk");
                int idAeroportoChegada = rs.getInt("id_aeroporto_chegada_voo_fk");

                Voo voo = new Voo(id, numero, status, origem, destino, horarioEmbarque, horarioDesembarque, idAeronave,
                        idPortaoEmbarque, idAeroportoChegada);
                voos.add(voo);
            }

            return Resultado.sucesso("Voos carregados", voos);

        } catch (SQLException e) {
            return Resultado.erro(e.getMessage());

        } finally {
            if (pstm != null)
                pstm.close();
            if (con != null)
                con.close();
        }
    }

    public Resultado verificarVooLotado(Integer id) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT verificarVooLotado(?);");
            
            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                boolean disponivel = rs.getBoolean(1);
                return Resultado.sucesso("Verificação completa", disponivel);
            }

            return Resultado.erro("Algo deu errado, a verificação não pode ser completa");

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