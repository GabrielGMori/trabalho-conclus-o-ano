package com.tca;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

import com.tca.model.Administrador;
import com.tca.model.Passageiro;

public class App extends Application {
    private static Administrador administradorLogado;
    private static Passageiro passageiroLogado;

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("voosPassageiro"), 997, 794);
        stage.setScene(scene);
        stage.show();
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
