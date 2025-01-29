package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
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

public class RegistroPassageiroControllerFXML implements Initializable {
    private PassageiroRepository passageiroRepository = PassageiroRepository.getInstance();
    private PaisRepository paisRepository = PaisRepository.getInstance();
    private ArrayList<?> paises;

    @FXML
    private TextField senhaTextField;

    @FXML
    private TextField passaporteTextField;

    @FXML
    private ChoiceBox<String> nacionalidadeChoiceBox;

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
    void voltar(ActionEvent event) throws IOException {
        App.setRoot("escolherLogin");
    }

    @FXML
    void jaTenhoConta(ActionEvent event) throws IOException {
        App.setRoot("loginPassageiro");
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            paises = paisRepository.listar();
            if (paises == null || paises.isEmpty() || !(paises.stream().allMatch(element -> element instanceof Pais))) {
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        for (int i=0; i<paises.size(); i++) {
            Pais pais = (Pais) paises.get(i);
            nacionalidadeChoiceBox.getItems().add(pais.getNome());
        }
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false && validarDados() == true && passageiroJaExiste() == false) {
            Passageiro passageiroNovo;
            try {
                ArrayList<?> params = getDados();
                passageiroNovo = new Passageiro(
                    (String) params.get(0),
                    (String) params.get(1),
                    (String) params.get(2),
                    (String) params.get(3),
                    (String) params.get(4),
                    (String) params.get(5),
                    (Integer) params.get(6)
                );
                Resultado result = passageiroRepository.criar(passageiroNovo);
                if (result.foiErro()) {
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
                passageiroNovo = (Passageiro) result.comoSucesso().getObj();
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            App.setUsuario(passageiroNovo);
            App.setRoot("voosPassageiro");
        }
    }

    private Boolean verificarCamposVazios() {
        ArrayList<TextField> textFields = new ArrayList<>(Arrays.asList(emailTextField, nomeTextField, senhaTextField, cpfTextField, passaporteTextField, telefoneTextField));

        for (int i=0; i<textFields.size(); i++) {
            if (textFields.get(i).getText().trim().isEmpty()) {
                warningText.setText("Por favor, preencha todos os campos");
                return true;
            }
        }

        if (nacionalidadeChoiceBox.getSelectionModel().getSelectedIndex() < 0) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }

        return false;
    }

    private Boolean validarDados() {
        if (!(emailTextField.getText().contains("@"))) {
            warningText.setText("O e-mail inserido não é válido");
            return false;
        } else if (!StringFormatter.formatNumericData(cpfTextField.getText()).matches("\\d{11}")) {
            warningText.setText("O CPF inserido não é válido");
            return false;
        } else if (!StringFormatter.formatNumericData(passaporteTextField.getText()).matches("[A-Za-z]{2}\\d{6}")) {
            warningText.setText("O passaporte inserido não é válido");
            return false;
        } else if (!StringFormatter.formatNumericData(telefoneTextField.getText()).matches("\\d{11}")) {
            warningText.setText("O telefone inserido não é válido");
            return false;
        }
        return true;
    }

    private Boolean passageiroJaExiste() {
        try {
            ArrayList<?> passageirosEmail = passageiroRepository.getPassageirosFiltro(null, emailTextField.getText().trim().toLowerCase(), null, null, null, null);
            ArrayList<?> passageirosCpf = passageiroRepository.getPassageirosFiltro(StringFormatter.formatNumericData(cpfTextField.getText()), null, null, null, null, null);
            if (passageirosEmail == null || passageirosCpf == null) {
                warningText.setText("Um erro ocorreu, tente novamente");
                return true;
            }
            if (!passageirosCpf.isEmpty()) {
                warningText.setText("Um passageiro já foi registrado com esse CPF");
                return true;
            }
            if (!passageirosEmail.isEmpty())  {
                warningText.setText("Um passageiro já foi registrado com esse e-mail");
                return true;
            }
        } catch(Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return true;
        }
        return false;
    }

    private ArrayList<?> getDados() {
        Pais pais = (Pais) paises.get(nacionalidadeChoiceBox.getSelectionModel().getSelectedIndex());

        String cpf = StringFormatter.formatNumericData(cpfTextField.getText());
        String email = emailTextField.getText().trim().toLowerCase();
        String nome = StringFormatter.capitalize(nomeTextField.getText().toLowerCase());
        String senha = senhaTextField.getText().trim();
        String passaporte = StringFormatter.formatNumericData(passaporteTextField.getText().toUpperCase());
        String telefone = StringFormatter.formatNumericData(telefoneTextField.getText().trim());
        Integer paisId = pais.getId();

        return new ArrayList<>(Arrays.asList(cpf, email, nome, senha, passaporte, telefone, paisId));
    }

}
