package com.tca.dao.implementations;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.tca.dao.FabricaConexoes;
import com.tca.dao.interfaces.PaisDAO;
import com.tca.model.Pais;
import com.tca.util.DBUtils;
import com.github.hugoperlin.results.Resultado;

public class PaisDAOImpl implements PaisDAO {
    private FabricaConexoes fabrica;

    public PaisDAOImpl(FabricaConexoes fabrica) {
        this.fabrica = fabrica;
    }

    @Override
    public Resultado criar(Pais pais) throws SQLException {

        Connection con = null;
        PreparedStatement pstm = null;
        try {
            con = fabrica.getConnection();
            pstm = con.prepareStatement(
                    "INSERT INTO Pais(nome_pais) VALUES (?);",
                    Statement.RETURN_GENERATED_KEYS);

            pstm.setString(1, pais.getNome());

            int ret = pstm.executeUpdate();

            if(ret == 1){
                int id = DBUtils.getLastId(pstm);
                pais.setId(id);
                return Resultado.sucesso("País criado com sucesso", pais);
            }

            return Resultado.erro("Algo deu errado, o país não foi criado");

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
            pstm = con.prepareStatement("SELECT * FROM Pais WHERE id_pais_pk = ?;");

            pstm.setInt(1, id);

            ResultSet rs = pstm.executeQuery();

            if (rs.next()) {
                String nome = rs.getString("nome_pais");

                Pais pais = new Pais(id, nome);
                return Resultado.sucesso("País carregado", pais);
            }

            return Resultado.erro("País não encontrado");

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
            pstm = con.prepareStatement("SELECT * FROM Pais;");

            ResultSet rs = pstm.executeQuery();

            ArrayList<Pais> paises = new ArrayList<>();
            while (rs.next()) {
                int id = rs.getInt("id_pais_pk");
                String nome = rs.getString("nome_pais");

                Pais pais = new Pais(id, nome);
                paises.add(pais);
            }

            return Resultado.sucesso("Países carregados", paises);

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
