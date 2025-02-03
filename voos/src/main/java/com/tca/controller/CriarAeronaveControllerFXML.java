package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Aeronave;
import com.tca.model.CompanhiaAerea;
import com.tca.repository.AeronaveRepository;
import com.tca.repository.CompanhiaAereaRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CriarAeronaveControllerFXML implements Initializable {
    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();
    private CompanhiaAereaRepository companhiaAereaRepository = CompanhiaAereaRepository.getInstance();
    private ArrayList<?> companhias;

    @FXML
    private ChoiceBox<String> companhiaChoiceBox;

    @FXML
    private TextField modeloTextField;

    @FXML
    private Text warningText;

    @FXML
    private TextField capacidadeTextField;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoAeronaves");
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false) {
            try {
                Resultado result = aeronaveRepository.criar(getAeronave());
                if (result.foiErro()) {
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            App.setRoot("visualizacaoAeronaves");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            companhias = companhiaAereaRepository.listar();
            if (companhias.isEmpty()) {
                warningText.setText("Não será possível criar uma aeronave pois nenhuma companhia está registrado no sistema");
            }
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        } 

        for (int i=0; i<companhias.size(); i++) {
            CompanhiaAerea companhiaAerea = (CompanhiaAerea) companhias.get(i);
            companhiaChoiceBox.getItems().add(companhiaAerea.getId() + " " + companhiaAerea.getNome());
        }
    }

    private Boolean verificarCamposVazios() {
        if (modeloTextField.getText().isEmpty() ||
            capacidadeTextField.getText().isEmpty() ||
            companhiaChoiceBox.getSelectionModel().getSelectedIndex() < 0) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Aeronave getAeronave() {
        String modelo = modeloTextField.getText().trim();
        Integer capacidade = Integer.valueOf(StringFormatter.formatNumericData(capacidadeTextField.getText()));
        Integer idCompanhia = ((CompanhiaAerea) companhias.get(companhiaChoiceBox.getSelectionModel().getSelectedIndex())).getId();

        return new Aeronave(modelo, capacidade, idCompanhia);
    }

}
