package com.tca.controller;

import java.io.IOException;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Voo;
import com.tca.repository.AeronaveRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ComprarPassagemControllerFXML {
    private static Voo voo;

    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();
    private String assentoSelecionado;

    @FXML
    private Pane comprarPassagemPane;

    @FXML
    private VBox assentosVBox;

    @FXML
    private Pane desejaReservarAssentoPane;

    @FXML
    private Text selecionadoText;

    @FXML
    private ChoiceBox<?> metodoPagamentoChoiceBox;

    @FXML
    private Pane reservarAssentoPane;

    @FXML
    void cancelarEscolherAssento(ActionEvent event) throws IOException {
        App.setRoot("verVooPassageiro");
    }

    @FXML
    void naoEscolherAssento(ActionEvent event) {
        assentoSelecionado = null;
        desejaReservarAssentoPane.setVisible(false);
        comprarPassagemPane.setVisible(true);
    }

    @FXML
    void simEscolherAssento(ActionEvent event) {
        assentoSelecionado = null;

        try {
            Resultado result = aeronaveRepository.get(voo.getIdAeronave());
            if (result.foiErro()) {

            }
        } catch (Exception e) {
            e.printStackTrace();
            selecionadoText.setText("");
        }

        

        desejaReservarAssentoPane.setVisible(false);
        reservarAssentoPane.setVisible(true);
    }

    @FXML
    void cancelarAssento(ActionEvent event) throws IOException {
        App.setRoot("verVooPassageiro");
    }

    @FXML
    void confirmarAssento(ActionEvent event) {

    }

    @FXML
    void cancelarPagamento(ActionEvent event) throws IOException {
        App.setRoot("verVooPassageiro");
    }

    @FXML
    void confirmarPagamento(ActionEvent event) {

    }

    public static void setVoo(Voo novoVoo) {
        voo = novoVoo;
    }

}