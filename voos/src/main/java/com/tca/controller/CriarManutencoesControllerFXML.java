package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Aeronave;
import com.tca.model.Manutencao;
import com.tca.repository.AeronaveRepository;
import com.tca.repository.ManutencaoRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CriarManutencoesControllerFXML implements Initializable {
    private ManutencaoRepository manutencaoRepository = ManutencaoRepository.getInstance();
    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();
    
    private ArrayList<?> aeronaves;
    private ArrayList<String> statuses;

    @FXML
    private TextField inicioDateTextField;

    @FXML
    private TextField inicioTimeTextField;

    @FXML
    private TextField fimTimeTextField;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private ChoiceBox<String> aeronaveChoiceBox;

    @FXML
    private TextField fimDateTextField;

    @FXML
    private Text warningText;

    @FXML
    private TextField descricaoTextField;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoManutencoes");
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false && validarDados() == true) {
            try {
                Resultado result = manutencaoRepository.criar(getManutencao());
                if (result.foiErro()) {
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            App.setRoot("visualizacaoManutencoes");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            aeronaves = aeronaveRepository.listar();
            if (aeronaves.isEmpty()) {
                warningText.setText("Não será possível criar uma manutenção pois nenhuma aeronave está registrada no sistema");
            }
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }  

        statuses = new ArrayList<>(Arrays.asList("Agendada", "Atrasada", "Em andamento", "Cancelada", "Finalizada"));
        for (int i=0; i<statuses.size(); i++) statusChoiceBox.getItems().add(statuses.get(i));

        for (int i=0; i<aeronaves.size(); i++) {
            Aeronave aeronave = (Aeronave) aeronaves.get(i);
            aeronaveChoiceBox.getItems().add(aeronave.getId() + " " + aeronave.getModelo());
        }
    }

    private Boolean verificarCamposVazios() {
        if (inicioDateTextField.getText().isEmpty() ||
            inicioTimeTextField.getText().isEmpty() ||
            fimDateTextField.getText().isEmpty() ||
            fimTimeTextField.getText().isEmpty() ||
            descricaoTextField.getText().isEmpty() ||
            aeronaveChoiceBox.getSelectionModel().getSelectedIndex() < 0) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Boolean validarDados() {
        try {
            if (!StringFormatter.formatNumericData(inicioDateTextField.getText()).matches("\\d{8}") ||
            !StringFormatter.formatNumericData(fimDateTextField.getText()).matches("\\d{8}") ||
            !StringFormatter.formatNumericData(inicioTimeTextField.getText()).matches("\\d{3,4}") ||
            !StringFormatter.formatNumericData(fimTimeTextField.getText()).matches("\\d{3,4}")) {
            warningText.setText("Alguma data digitada é inválida");
            return false;
            }

            String horarioFormatadoInicio = StringFormatter.formatNumericData(inicioTimeTextField.getText());
            if (horarioFormatadoInicio.length() == 3) horarioFormatadoInicio = "0" + horarioFormatadoInicio;
            LocalDateTime.parse(StringFormatter.formatNumericData(inicioDateTextField.getText()) + horarioFormatadoInicio, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));

            String horarioFormatadoFim = StringFormatter.formatNumericData(fimTimeTextField.getText());
            if (horarioFormatadoFim.length() == 3) horarioFormatadoFim = "0" + horarioFormatadoFim;
            LocalDateTime.parse(StringFormatter.formatNumericData(fimDateTextField.getText()) + horarioFormatadoFim, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Alguma data digitada é inválida");
            return false;
        }
        return true;
    }

    private Manutencao getManutencao() {
        String horarioFormatado = StringFormatter.formatNumericData(inicioTimeTextField.getText());
        if (horarioFormatado.length() == 3) horarioFormatado = "0" + horarioFormatado;
        LocalDateTime dataInicio = LocalDateTime.parse(
                StringFormatter.formatNumericData(inicioDateTextField.getText()) + horarioFormatado, 
                DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));

        horarioFormatado = StringFormatter.formatNumericData(fimTimeTextField.getText());
        if (horarioFormatado.length() == 3) horarioFormatado = "0" + horarioFormatado;
        LocalDateTime dataFim = LocalDateTime.parse(
            StringFormatter.formatNumericData(fimDateTextField.getText()) + horarioFormatado, 
            DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));

        String descricao = descricaoTextField.getText().trim();
        Integer idAeronave = ((Aeronave) aeronaves.get(aeronaveChoiceBox.getSelectionModel().getSelectedIndex())).getId();
        String status = statuses.get(statusChoiceBox.getSelectionModel().getSelectedIndex());

        return new Manutencao(descricao, dataInicio, dataFim, status, idAeronave);
    }

}
