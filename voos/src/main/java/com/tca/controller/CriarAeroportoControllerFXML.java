package com.tca.controller;

import java.io.IOException;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Aeroporto;
import com.tca.repository.AeroportoRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CriarAeroportoControllerFXML {
    private AeroportoRepository aeroportoRepository = AeroportoRepository.getInstance();

    @FXML
    private TextField localizacaoTextField;

    @FXML
    private TextField nomeTextField;

    @FXML
    private Text warningText;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoAeroportos");
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false) {
            try {
                Resultado result = aeroportoRepository.criar(getAeroporto());
                if (result.foiErro()) {
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            App.setRoot("visualizacaoAeroportos");
        }
    }

    private Boolean verificarCamposVazios() {
        if (nomeTextField.getText().isEmpty() ||
            localizacaoTextField.getText().isEmpty()) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Aeroporto getAeroporto() {
        String nome = StringFormatter.capitalize(nomeTextField.getText());
        String localizacao = StringFormatter.capitalize(localizacaoTextField.getText());

        return new Aeroporto(nome, localizacao);
    }

}
