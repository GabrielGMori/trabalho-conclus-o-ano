package com.tca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

import com.tca.model.Administrador;
import com.tca.model.MetodoPagamento;
import com.tca.model.Pais;
import com.tca.model.Passageiro;
import com.tca.repository.MetodoPagamentoRepository;
import com.tca.repository.PaisRepository;

public class App extends Application {
    private static Administrador administradorLogado;
    private static Passageiro passageiroLogado;

    private static Scene scene;

    private MetodoPagamentoRepository metodoPagamentoRepository = MetodoPagamentoRepository.getInstance();
    private PaisRepository paisRepository = PaisRepository.getInstance();

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("visualizacaoVoos"), 997, 794);
        stage.setScene(scene);
        stage.show();
    }

    @Override
    public void init() throws SQLException {
        setUsuario(new Administrador("00000000001", "Administrador 1", "admin123"));

        try {
            if (metodoPagamentoRepository.listar().isEmpty()) {
                metodoPagamentoRepository.criar(new MetodoPagamento("Cartão de crédito"));
                metodoPagamentoRepository.criar(new MetodoPagamento("Pix"));
                metodoPagamentoRepository.criar(new MetodoPagamento("Boleto"));
            }

            if (paisRepository.listar().isEmpty()) {
                paisRepository.criar(new Pais("Brasil"));
                paisRepository.criar(new Pais("Estados Unidos"));
                paisRepository.criar(new Pais("Canadá"));
                paisRepository.criar(new Pais("Alemanha"));
                paisRepository.criar(new Pais("França"));
                paisRepository.criar(new Pais("Japão"));
                paisRepository.criar(new Pais("China"));
                paisRepository.criar(new Pais("Austrália"));
                paisRepository.criar(new Pais("Índia"));
                paisRepository.criar(new Pais("Reino Unido"));
                paisRepository.criar(new Pais("Argentina"));
                paisRepository.criar(new Pais("Chile"));
                paisRepository.criar(new Pais("Uruguai"));
                paisRepository.criar(new Pais("Paraguai"));
                paisRepository.criar(new Pais("Peru"));
                paisRepository.criar(new Pais("Colômbia"));
                paisRepository.criar(new Pais("Venezuela"));
                paisRepository.criar(new Pais("Bolívia"));
                paisRepository.criar(new Pais("Equador"));
                paisRepository.criar(new Pais("México"));
                paisRepository.criar(new Pais("Itália"));
                paisRepository.criar(new Pais("Espanha"));
                paisRepository.criar(new Pais("Portugal"));
                paisRepository.criar(new Pais("Rússia"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("fxml/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    public static void setUsuario(Administrador administrador) {
        administradorLogado = administrador;
        passageiroLogado = null;
    }

    public static void setUsuario(Passageiro passageiro) {
        administradorLogado = null;
        passageiroLogado = passageiro;
    }

    public static Administrador getAdministradorLogado() {
        return administradorLogado;
    }

    public static Passageiro getPassageiroLogado() {
        return passageiroLogado;
    }

    public static void deslogar() {
        administradorLogado = null;
        passageiroLogado = null;
    }

}
