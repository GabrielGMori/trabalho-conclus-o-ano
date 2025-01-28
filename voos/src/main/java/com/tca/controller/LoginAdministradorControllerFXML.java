package com.tca.controller;

import java.io.IOException;
import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Administrador;
import com.tca.repository.AdministradorRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class LoginAdministradorControllerFXML {
    private AdministradorRepository administradorRepository = AdministradorRepository.getInstance();

    @FXML
    private TextField senhaTextField;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField cpfTextField;

    @FXML
    private Text warningText;

    @FXML
    void voltar(ActionEvent event) throws IOException {
        App.setRoot("escolherLogin");
    }

    @FXML
    void login(ActionEvent event) throws IOException{
        warningText.setText("");
        if (verificarCamposVazios() == false && validarDados() == true) {
            Administrador administrador = encontrarAdministrador();
            if (administrador == null) return;
            App.setUsuario(administrador);
            App.setRoot("visualizacaoVoos");
        }
    }

    private Boolean verificarCamposVazios() {
        if (senhaTextField.getText().isEmpty() || nomeTextField.getText().isEmpty() || cpfTextField.getText().isEmpty()) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Boolean validarDados() {
        if (cpfTextField.getText().length() != 11) {
            warningText.setText("O CPF digitado é inválido");
            return false;
        }
        return true;
    }

    private Administrador encontrarAdministrador() {
        try {
            Resultado result = administradorRepository.get(StringFormatter.formatNumericData(cpfTextField.getText()));
            if (result.foiErro()) {
                warningText.setText(result.comoErro().getMsg());
                return null;
            }
            Object obj = result.comoSucesso().getObj();
            Administrador administrador = (Administrador) obj;
            String nome = StringFormatter.capitalize(nomeTextField.getText().trim().toLowerCase());
            String senha = senhaTextField.getText().trim();
            if (!administrador.getNome().equals(nome) || !administrador.getSenha().equals(senha)) {
                warningText.setText("Algum dado está incorreto");
                return null;
            }
            return administrador;
        } catch(Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return null;
        }
    }

}
