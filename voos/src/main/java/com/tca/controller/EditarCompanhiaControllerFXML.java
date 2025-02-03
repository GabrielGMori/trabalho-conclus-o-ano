package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.CompanhiaAerea;
import com.tca.repository.CompanhiaAereaRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditarCompanhiaControllerFXML implements Initializable {
    private Boolean confirmar = false;

    private static Integer idCompanhia;
    private CompanhiaAerea companhiaAerea;

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
    void deletar(ActionEvent event) {
        if (confirmar == false) {
            confirmar = true;
            warningText.setText("Clique novamente para confirmar\n(Todas as aeronaves da companhia serão removidas do sistema!)");
            return;
        }
        warningText.setText("");
        confirmar = false;
        try {
            Resultado result = companhiaAereaRepository.deletar(idCompanhia);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            App.setRoot("visualizacaoCompanhias");
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
        }
    }

    @FXML
    void confirmar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false && validarDados() == true) {
            try {
                Resultado result = companhiaAereaRepository.atualizar(idCompanhia, getCompanhia());
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Resultado result = companhiaAereaRepository.get(idCompanhia);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            companhiaAerea = (CompanhiaAerea) result.comoSucesso().getObj();
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        carregarDados();
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

    private void carregarDados() {
        codigoIcaoTextField.setText(companhiaAerea.getCodigoIcao());
        nomeTextField.setText(companhiaAerea.getNome());
    }

    public static void setIdCompanhia(int id) {
        idCompanhia = id;
    }

    private CompanhiaAerea getCompanhia() {
        String icao = codigoIcaoTextField.getText().trim().toUpperCase();
        String nome = StringFormatter.capitalize(nomeTextField.getText());

        return new CompanhiaAerea(icao, nome);
    }

}
