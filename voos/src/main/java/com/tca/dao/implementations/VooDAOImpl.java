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
import com.tca.dao.interfaces.VooDAO;
import com.tca.model.Voo;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class VooDAOImpl implements VooDAO {
    private FabricaConexoes fabrica;

    public VooDAOImpl(FabricaConexoes fabrica) {
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
            pstm.setTimestamp(5, Timestamp.valueOf(voo.getHorarioEmbarque()));
            pstm.setTimestamp(6, Timestamp.valueOf(voo.getHorarioDesembarque()));
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
            pstm = con.prepareStatement("SELECT * FROM Voo ORDER BY horario_embarque_voo;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Voo> voos = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_voo_pk");
                String numero = rs.getString("numero_voo");
                String status = rs.getString("status_voo");
                String origem = rs.getString("origem_voo");
                String destino = rs.getString("destino_voo");
                LocalDateTime horarioEmbarque = rs.getTimestamp("horario_embarque_voo").toLocalDateTime();
                LocalDateTime horarioDesembarque = rs.getTimestamp("horario_desembarque_voo").toLocalDateTime();
                int idAeronave = rs.getInt("id_aeronave_voo_fk");
                int idPortaoEmbarque = rs.getInt("id_portao_embarque_voo_fk");
                int idAeroportoChegada = rs.getInt("id_aeroporto_chegada_voo_fk");

                Voo voo = new Voo(id, numero, status, origem, destino, horarioEmbarque, horarioDesembarque, idAeronave, idPortaoEmbarque, idAeroportoChegada);
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
    public Resultado get(Integer id) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT * FROM Voo WHERE id_voo_pk = ?;");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String numero = rs.getString("numero_voo");
                String status = rs.getString("status_voo");
                String origem = rs.getString("origem_voo");
                String destino = rs.getString("destino_voo");
                LocalDateTime horarioEmbarque = rs.getTimestamp("horario_embarque_voo").toLocalDateTime();
                LocalDateTime horarioDesembarque = rs.getTimestamp("horario_desembarque_voo").toLocalDateTime();
                int idAeronave = rs.getInt("id_aeronave_voo_fk");
                int idPortaoEmbarque = rs.getInt("id_portao_embarque_voo_fk");
                int idAeroportoChegada = rs.getInt("id_aeroporto_chegada_voo_fk");

                Voo voo = new Voo(id, numero, status, origem, destino, horarioEmbarque, horarioDesembarque, idAeronave,
                        idPortaoEmbarque, idAeroportoChegada);
                return Resultado.sucesso("Voo carregado", voo);
            }

            return Resultado.erro("Voo não encontrado");

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
            pstm.setTimestamp(5, Timestamp.valueOf(voo.getHorarioEmbarque()));
            pstm.setTimestamp(6, Timestamp.valueOf(voo.getHorarioDesembarque()));
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
    public Resultado getVoosFiltro(String numeroFiltro, String statusFiltro, String origemFiltro, String destinoFiltro, LocalDateTime horarioEmbarqueInicialFiltro, LocalDateTime horarioEmbarqueFinalFiltro, LocalDateTime horarioDesembarqueInicialFiltro, LocalDateTime horarioDesembarqueFinalFiltro, Integer idAeronaveFiltro, Integer idPortaoEmbarqueFiltro, String AeroportoEmbarqueFiltro, String AeroportoChegadaFiltro) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "SELECT * FROM Voo voo JOIN Portao_Embarque portao ON voo.id_portao_embarque_voo_fk = portao.id_portao_pk JOIN Aeroporto aeroporto_embarque ON portao.id_aeroporto_portao_fk = aeroporto_embarque.id_aeroporto_pk JOIN Aeroporto aeroporto_desembarque ON voo.id_aeroporto_chegada_voo_fk = aeroporto_desembarque.id_aeroporto_pk WHERE (? IS NULL OR numero_voo = ?) AND (? IS NULL OR status_voo = ?) AND (? IS NULL OR origem_voo = ?) AND (? IS NULL OR destino_voo = ?) AND (? IS NULL OR horario_embarque_voo >= ?) AND (? IS NULL OR horario_embarque_voo <= ?) AND (? IS NULL OR horario_desembarque_voo >= ?) AND (? IS NULL OR horario_desembarque_voo <= ?) AND (? IS NULL OR id_aeronave_voo_fk = ?) AND (? IS NULL OR id_portao_embarque_voo_fk = ?) AND (? IS NULL OR aeroporto_embarque.nome_aeroporto = ?) AND (? IS NULL OR aeroporto_desembarque.nome_aeroporto = ?) ORDER BY horario_embarque_voo;");
            
            int i = 1;
            int j = i;
            for (j+=2; i<j; i++)  {if (numeroFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, numeroFiltro); } 
            for (j+=2; i<j; i++)  {if (statusFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, statusFiltro); }
            for (j+=2; i<j; i++)  {if (origemFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, origemFiltro); }
            for (j+=2; i<j; i++)  {if (destinoFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, destinoFiltro); }
            for (j+=2; i<j; i++)  {if (horarioEmbarqueInicialFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(horarioEmbarqueInicialFiltro)); }
            for (j+=2; i<j; i++)  {if (horarioEmbarqueFinalFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(horarioEmbarqueFinalFiltro)); }
            for (j+=2; i<j; i++)  {if (horarioDesembarqueInicialFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(horarioDesembarqueInicialFiltro)); }
            for (j+=2; i<j; i++)  {if (horarioDesembarqueFinalFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setTimestamp(i, Timestamp.valueOf(horarioDesembarqueFinalFiltro)); }
            for (j+=2; i<j; i++)  {if (idAeronaveFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setInt(i, idAeronaveFiltro); }
            for (j+=2; i<j; i++)  {if (idPortaoEmbarqueFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setInt(i, idPortaoEmbarqueFiltro); }
            for (j+=2; i<j; i++)  {if (AeroportoEmbarqueFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, AeroportoEmbarqueFiltro); }
            for (j+=2; i<j; i++)  {if (AeroportoChegadaFiltro == null) pstm.setNull(i, java.sql.Types.NULL); else pstm.setString(i, AeroportoChegadaFiltro); }

            ResultSet rs = pstm.executeQuery();

            ArrayList<Voo> voos = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_voo_pk");
                String numero = rs.getString("numero_voo");
                String status = rs.getString("status_voo");
                String origem = rs.getString("origem_voo");
                String destino = rs.getString("destino_voo");
                LocalDateTime horarioEmbarque = rs.getTimestamp("horario_embarque_voo").toLocalDateTime();
                LocalDateTime horarioDesembarque = rs.getTimestamp("horario_desembarque_voo").toLocalDateTime();
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

    public Resultado verificarDisponibilidadeAssento(Integer id, String assento) throws SQLException {
        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement("SELECT verificarDisponibilidadeAssentoFunc(?, ?);");
            
            pstm.setInt(1, id);
            pstm.setString(2, assento);

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
