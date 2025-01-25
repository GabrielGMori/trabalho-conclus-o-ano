package com.tca.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.tca.util.Env;

public class FabricaConexoes {

    private static int MAX_CONNECTIONS = 10;
    private final String URL_DB;
    private final String DB_NAME;
    private final String USER;
    private final String PASSWORD;
    private final String CON_STRING;

    private Connection[] conexoes;

    private static FabricaConexoes instance;

    private FabricaConexoes() {
        conexoes = new Connection[MAX_CONNECTIONS];
        // carregar dados de conexão do .env
        URL_DB = Env.get("URL_DB");
        DB_NAME = Env.get("DB_NAME");
        USER = Env.get("DB_USER");
        PASSWORD = Env.get("DB_PASSWORD");
        CON_STRING = URL_DB + "/" + DB_NAME + "?user=" + USER + "&password=" + PASSWORD + "&useUnicode=true&characterEncoding=UTF-8";
    }

    public static FabricaConexoes getInstance() {
        if (instance != null) {
            return instance;
        }
        instance = new FabricaConexoes();
        return instance;
    }

    public Connection getConnection() throws SQLException {

        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            if (instance.conexoes[i] == null || instance.conexoes[i].isClosed()) {
                instance.conexoes[i] = DriverManager.getConnection(CON_STRING, USER, PASSWORD);
                return instance.conexoes[i];
            }
        }
        throw new SQLException("Máximo de conexões");
    }

    public static void setNullable(PreparedStatement pstm, int index, Object valor) throws SQLException {
        if (valor == null) {
            pstm.setNull(index, java.sql.Types.NULL);
        } else {
            pstm.setObject(index, valor);
        }
    }
}