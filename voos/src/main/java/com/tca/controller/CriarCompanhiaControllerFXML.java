package com.tca.controller;

import java.io.IOException;
import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.CompanhiaAerea;
import com.tca.repository.CompanhiaAereaRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CriarCompanhiaControllerFXML {
    private CompanhiaAereaRepository companhiaAereaRepository = CompanhiaAereaRepository.getInstance();

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField codigoIcaoTextField;

    @FXML
    private Text warningText;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoCompanhias");
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false && validarDados() == true) {
            try {
                Resultado result = companhiaAereaRepository.criar(getCompanhia());
                if (result.foiErro()) {
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            App.setRoot("visualizacaoCompanhias");
        }
    }

    private Boolean verificarCamposVazios() {
        if (codigoIcaoTextField.getText().isEmpty() ||
            nomeTextField.getText().isEmpty()) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Boolean validarDados() {
        if (StringFormatter.formatNumericData(codigoIcaoTextField.getText()).length() > 4) {
            warningText.setText("O código ICAO deve ter no máximo 4 caracteres");
            return false;
        }
        return true;
    }

    private CompanhiaAerea getCompanhia() {
        String icao = codigoIcaoTextField.getText().trim().toUpperCase();
        String nome = StringFormatter.capitalize(nomeTextField.getText());

        return new CompanhiaAerea(icao, nome);
    }

}
