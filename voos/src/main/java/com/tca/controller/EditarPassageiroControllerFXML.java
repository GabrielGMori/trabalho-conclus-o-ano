package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Pais;
import com.tca.model.Passageiro;
import com.tca.repository.PaisRepository;
import com.tca.repository.PassageiroRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class EditarPassageiroControllerFXML implements Initializable {
    private Boolean confirmar = false;

    private static String cpfPassageiro;
    private Passageiro passageiro;

    private PassageiroRepository passageiroRepository = PassageiroRepository.getInstance();
    private PaisRepository paisRepository = PaisRepository.getInstance();
    private ArrayList<?> paises;

    @FXML
    private TextField senhaTextField;

    @FXML
    private ChoiceBox<String> nacionalidadeChoiceBox;

    @FXML
    private TextField passaporteTextField;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField telefoneTextField;

    @FXML
    private TextField cpfTextField;

    @FXML
    private Text warningText;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoPassageiros");
    }

    @FXML
    void deletar(ActionEvent event) {
        if (confirmar == false) {
            confirmar = true;
            warningText.setText("Clique novamente para confirmar\n(Todas as passagens do passageiro serão removidas do sistema!)");
            return;
        }
        warningText.setText("");
        confirmar = false;
        try {
            Resultado result = passageiroRepository.deletar(cpfPassageiro);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            App.setRoot("visualizacaoPassageiros");
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
                Resultado result = passageiroRepository.atualizar(cpfPassageiro, getPassageiro());
                if (result.foiErro()) {
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            App.setRoot("visualizacaoPassageiros");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Resultado result = passageiroRepository.get(cpfPassageiro);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            passageiro = (Passageiro) result.comoSucesso().getObj();

            paises = paisRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        for (int i=0; i<paises.size(); i++) {
            Pais pais = (Pais) paises.get(i);
            nacionalidadeChoiceBox.getItems().add(pais.getNome());
            if (pais.getId() == passageiro. getIdPais()) nacionalidadeChoiceBox.getSelectionModel().select(i);
        }

        carregarDados();
    }

    private Boolean verificarCamposVazios() {
        if (nomeTextField.getText().isEmpty() ||
            emailTextField.getText().isEmpty() ||
            senhaTextField.getText().isEmpty() ||
            cpfTextField.getText().isEmpty() ||
            passaporteTextField.getText().isEmpty() ||
            telefoneTextField.getText().isEmpty() ||
            nacionalidadeChoiceBox.getSelectionModel().getSelectedIndex() < 0) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Boolean validarDados() {
        if (!StringFormatter.formatNumericData(cpfTextField.getText()).matches("\\d{11}") ||
            !StringFormatter.formatNumericData(telefoneTextField.getText()).matches("\\d{11}") ||
            !emailTextField.getText().trim().contains("@") ||
            !StringFormatter.formatNumericData(passaporteTextField.getText()).matches("[A-Za-z]{2}\\d{6}")) {
            warningText.setText("Algum dado digitado é inválido");
            return false;
        }
        return true;
    }

    private void carregarDados() {
        emailTextField.setText(passageiro.getEmail());
        nomeTextField.setText(passageiro.getNome());
        senhaTextField.setText(passageiro.getSenha());
        cpfTextField.setText(passageiro.getCpf());
        passaporteTextField.setText(passageiro.getPassaporte());
        telefoneTextField.setText(passageiro.getTelefone());
    }

    public static void setCpfPassageiro(String cpf) {
        cpfPassageiro = cpf;
    }

    private Passageiro getPassageiro() {
        String email = emailTextField.getText().trim().toLowerCase();
        String nome = StringFormatter.capitalize(nomeTextField.getText());
        String senha = senhaTextField.getText().trim();
        String cpf = StringFormatter.formatNumericData(cpfTextField.getText());
        String passaporte = StringFormatter.formatNumericData(passaporteTextField.getText()).toUpperCase();
        String telefone = StringFormatter.formatNumericData(telefoneTextField.getText());
        Integer idPais = ((Pais) paises.get(nacionalidadeChoiceBox.getSelectionModel().getSelectedIndex())).getId();

        return new Passageiro(cpf, email, nome, senha, passaporte, telefone, idPais);
    }

}
