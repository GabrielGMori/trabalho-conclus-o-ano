package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Administrador;
import com.tca.repository.AdministradorRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditarAdministradorControllerFXML implements Initializable {
    private Boolean confirmar = false;

    private static String cpfAdministrador;
    private Administrador administrador;

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
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoAdministradores");
    }

    @FXML
    void deletar(ActionEvent event) {
        String warning = "Clique novamente para confirmar";
        if (confirmar == false) {
            confirmar = true;
            if (App.getAdministradorLogado().getCpf().equals(administrador.getCpf())) warning += "\n(Você será deslogado da conta)";
            warningText.setText(warning);
            return;
        }
        warningText.setText("");
        confirmar = false;
        try {
            Resultado result = administradorRepository.deletar(cpfAdministrador);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            if (App.getAdministradorLogado().getCpf().equals(administrador.getCpf())) {
                App.deslogar();
                App.setRoot("escolherLogin");
            } else {
                App.setRoot("visualizacaoAdministradores");
            }
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
        }
    }

    @FXML
    void confirmar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false && validarDados() == true && cpfEmUso() == false) {
            try {
                Resultado result = administradorRepository.atualizar(cpfAdministrador, getAdministrador());
                if (result.foiErro()) {
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            if (App.getAdministradorLogado().getCpf().equals(administrador.getCpf())) App.setUsuario(getAdministrador());
            App.setRoot("visualizacaoAdministradores");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Resultado result = administradorRepository.get(cpfAdministrador);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            administrador = (Administrador) result.comoSucesso().getObj();
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        carregarDados();
    }

    private Boolean verificarCamposVazios() {
        if (cpfTextField.getText().isEmpty() ||
            nomeTextField.getText().isEmpty() ||
            senhaTextField.getText().isEmpty()) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Boolean validarDados() {
        if (!StringFormatter.formatNumericData(cpfTextField.getText()).matches("\\d{11}")) {
            warningText.setText("Algum dado digitado é inválido");
            return false;
        }
        return true;
    }

    private Boolean cpfEmUso() {
        Administrador administrador = getAdministrador();
        try {
            ArrayList<?> administradores = administradorRepository.getAdministradoresFiltro(administrador.getCpf(), null);
            if (!administradores.isEmpty()) {
                warningText.setText("CPF já registrado");
                return true;
            }
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return true;
        }
    }

    private void carregarDados() {
        cpfTextField.setText(administrador.getCpf());
        nomeTextField.setText(administrador.getNome());
        senhaTextField.setText(administrador.getSenha());
    }

    public static void setCpfAdministrador(String cpf) {
        cpfAdministrador = cpf;
    }

    private Administrador getAdministrador() {
        String cpf = StringFormatter.formatNumericData(cpfTextField.getText());
        String nome = StringFormatter.capitalize(nomeTextField.getText());
        String senha = senhaTextField.getText().trim();

        return new Administrador(cpf, nome, senha);
    }

}
