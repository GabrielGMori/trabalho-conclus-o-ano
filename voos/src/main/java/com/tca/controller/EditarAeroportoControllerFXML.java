package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Aeroporto;
import com.tca.repository.AeroportoRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditarAeroportoControllerFXML implements Initializable {
    private Boolean confirmar = false;

    private static Integer idAeroporto;
    private Aeroporto aeroporto;

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
    void deletar(ActionEvent event) {
        if (confirmar == false) {
            confirmar = true;
            warningText.setText("Clique novamente para confirmar\n(Todos os portões de embarque serão removidos do sistema!)");
            return;
        }
        warningText.setText("");
        confirmar = false;
        try {
            Resultado result = aeroportoRepository.deletar(idAeroporto);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            App.setRoot("visualizacaoAeroportos");
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
        }
    }

    @FXML
    void confirmar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false) {
            try {
                Resultado result = aeroportoRepository.atualizar(idAeroporto, getAeroporto());
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

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Resultado result = aeroportoRepository.get(idAeroporto);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            aeroporto = (Aeroporto) result.comoSucesso().getObj();
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        carregarDados();
    }

    private Boolean verificarCamposVazios() {
        if (nomeTextField.getText().isEmpty() ||
            localizacaoTextField.getText().isEmpty()) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private void carregarDados() {
        nomeTextField.setText(aeroporto.getNome());
        localizacaoTextField.setText(aeroporto.getLocalizacao());
    }

    public static void setIdAeroporto(int id) {
        idAeroporto = id;
    }

    private Aeroporto getAeroporto() {
        String nome = StringFormatter.capitalize(nomeTextField.getText());
        String localizacao = StringFormatter.capitalize(localizacaoTextField.getText());

        return new Aeroporto(nome, localizacao);
    }

}
