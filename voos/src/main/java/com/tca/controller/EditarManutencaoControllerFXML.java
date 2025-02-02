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

public class EditarManutencaoControllerFXML implements Initializable {
    private Boolean confirmar = false;

    private static Integer idManutencao;
    private Manutencao manutencao;

    private ManutencaoRepository manutencaoRepository = ManutencaoRepository.getInstance();
    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();

    private ArrayList<?> aeronaves;
    private ArrayList<String> statuses;

    @FXML
    private TextField inicioDateTextField;

    @FXML
    private TextField fimTimeTextField;

    @FXML
    private TextField inicioTimeTextField;

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
    void deletar(ActionEvent event) {
        if (confirmar == false) {
            confirmar = true;
            warningText.setText("Clique novamente para confirmar");
            return;
        }
        warningText.setText("");
        confirmar = false;
        try {
            Resultado result = manutencaoRepository.deletar(idManutencao);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            App.setRoot("visualizacaoManutencoes");
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
                Resultado result = manutencaoRepository.atualizar(idManutencao, getManutencao());
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
            Resultado result = manutencaoRepository.get(idManutencao);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            manutencao = (Manutencao) result.comoSucesso().getObj();

            aeronaves = aeronaveRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        statuses = new ArrayList<>(Arrays.asList("Agendada", "Atrasada", "Em andamento", "Cancelada", "Finalizada"));

        for (int i=0; i<statuses.size(); i++) {
            statusChoiceBox.getItems().add(statuses.get(i));
            if (statuses.get(i).equals(manutencao.getStatus())) {
                statusChoiceBox.getSelectionModel().select(i);
            }
        }
        for (int i=0; i<aeronaves.size(); i++) {
            Aeronave aeronave = (Aeronave) aeronaves.get(i);
            aeronaveChoiceBox.getItems().add(aeronave.getId() + " " + aeronave.getModelo());
            if (aeronave.getId() == manutencao.getIdAeronave()) aeronaveChoiceBox.getSelectionModel().select(i);
        }

        carregarDados();
    }

    private Boolean verificarCamposVazios() {
        if (inicioDateTextField.getText().isEmpty() ||
            inicioTimeTextField.getText().isEmpty() ||
            fimDateTextField.getText().isEmpty() ||
            fimTimeTextField.getText().isEmpty() ||
            descricaoTextField.getText().isEmpty() ||
            aeronaveChoiceBox.getSelectionModel().getSelectedIndex() < 0 ||
            statusChoiceBox.getSelectionModel().getSelectedIndex() < 0) {
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
            
            LocalDateTime.parse(StringFormatter.formatNumericData(inicioDateTextField.getText()) + StringFormatter.formatNumericData(inicioTimeTextField.getText()), DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));
            LocalDateTime.parse(StringFormatter.formatNumericData(fimDateTextField.getText()) + StringFormatter.formatNumericData(fimTimeTextField.getText()), DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Alguma data digitada é inválida");
            return false;
        }
        return true;
    }

    private void carregarDados() {
        DateTimeFormatter dataFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        inicioDateTextField.setText(manutencao.getDataInicio().format(dataFormatter));
        inicioTimeTextField.setText(manutencao.getDataInicio().format(timeFormatter));
        fimDateTextField.setText(manutencao.getDataFim().format(dataFormatter));
        fimTimeTextField.setText(manutencao.getDataFim().format(timeFormatter));
        descricaoTextField.setText(manutencao.getDescricao());
    }

    public static void setIdManutencao(int id) {
        idManutencao = id;
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
