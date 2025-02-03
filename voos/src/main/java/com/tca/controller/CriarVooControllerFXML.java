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
import com.tca.model.Aeroporto;
import com.tca.model.PortaoEmbarque;
import com.tca.model.Voo;
import com.tca.repository.AeronaveRepository;
import com.tca.repository.AeroportoRepository;
import com.tca.repository.PortaoEmbarqueRepository;
import com.tca.repository.VooRepository;
import com.tca.util.StringFormatter;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CriarVooControllerFXML implements Initializable {
    private VooRepository vooRepository = VooRepository.getInstance();
    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();
    private AeroportoRepository aeroportoRepository = AeroportoRepository.getInstance();
    private PortaoEmbarqueRepository portaoEmbarqueRepository = PortaoEmbarqueRepository.getInstance();

    private ArrayList<?> aeronaves; 
    private ArrayList<?> aeroportos; 
    private ArrayList<?> portoes; 
    private ArrayList<String> statuses;

    @FXML
    private TextField horarioDesembarqueTextField;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private TextField horarioEmbarqueTextField;

    @FXML
    private ChoiceBox<String> aeronaveChoiceBox;

    @FXML
    private ChoiceBox<String> aeroportoEmbarqueChoiceBox;

    @FXML
    private TextField origemTextField;

    @FXML
    private TextField numeroTextField;

    @FXML
    private TextField dataEmbarqueTextField;

    @FXML
    private ChoiceBox<String> portaoChoiceBox;

    @FXML
    private Text warningText;

    @FXML
    private TextField dataDesembarqueTextField;

    @FXML
    private TextField destinoTextField;

    @FXML
    private ChoiceBox<String> aeroportoDesembarqueChoiceBox;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoVoos");
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false && validarDados() == true) {
            try {
                Resultado result = vooRepository.criar(getVoo());
                if (result.foiErro()) {
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            App.setRoot("visualizacaoVoos");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            aeroportos = aeroportoRepository.listar();
            if (aeroportos.isEmpty()) {
                warningText.setText("Não será possível criar um voo pois nenhum aeroporto está registrado no sistema");
            }
            aeronaves = aeronaveRepository.listar();
            if (aeronaves.isEmpty()) {
                warningText.setText("Não será possível criar um voo pois nenhuma aeronave está registrada no sistema");
            }
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }  

        statuses = new ArrayList<>(Arrays.asList("Agendado", "Atrasado", "Em andamento", "Cancelado", "Finalizado"));
        for (int i=0; i<statuses.size(); i++) statusChoiceBox.getItems().add(statuses.get(i));

        for (int i=0; i<aeronaves.size(); i++) {
            Aeronave aeronave = (Aeronave) aeronaves.get(i);
            aeronaveChoiceBox.getItems().add(aeronave.getId() + " " + aeronave.getModelo());
        }
        for (int i=0; i<aeroportos.size(); i++) {
            Aeroporto aeroporto = (Aeroporto) aeroportos.get(i);
            aeroportoEmbarqueChoiceBox.getItems().add(aeroporto.getNome());
            aeroportoDesembarqueChoiceBox.getItems().add(aeroporto.getNome());
        }

        aeroportoEmbarqueChoiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            warningText.setText("");
            portaoChoiceBox.getItems().clear();
            try {
                Aeroporto aeroporto = (Aeroporto) aeroportos.get(aeroportoEmbarqueChoiceBox.getSelectionModel().getSelectedIndex());
                portoes = portaoEmbarqueRepository.getPortoesEmbarqueFiltro(null, null, aeroporto.getId());
                if (portoes.isEmpty()) {
                    warningText.setText("O aeroporto selecionado não possui nenhum portão de embarque cadastrado no sistema");
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Um erro ocorreu, não foi possível carregar os potões de embarque");
                return;
            }

            for (int i=0; i<portoes.size(); i++) {
                PortaoEmbarque portao = (PortaoEmbarque) portoes.get(i);
                portaoChoiceBox.getItems().add(portao.getCodigo());
            }
        });
    }

    private Boolean verificarCamposVazios() {
        if (dataEmbarqueTextField.getText().isEmpty() ||
            horarioEmbarqueTextField.getText().isEmpty() ||
            dataDesembarqueTextField.getText().isEmpty() ||
            horarioDesembarqueTextField.getText().isEmpty() ||
            origemTextField.getText().isEmpty() ||
            numeroTextField.getText().isEmpty() ||
            destinoTextField.getText().isEmpty() ||
            aeroportoEmbarqueChoiceBox.getSelectionModel().getSelectedIndex() < 0 ||
            aeroportoDesembarqueChoiceBox.getSelectionModel().getSelectedIndex() < 0 ||
            aeroportoDesembarqueChoiceBox.getSelectionModel().getSelectedIndex() < 0 ||
            aeronaveChoiceBox.getSelectionModel().getSelectedIndex() < 0 ||
            portaoChoiceBox.getSelectionModel().getSelectedIndex() < 0) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private Boolean validarDados() {
        try {
            if (!StringFormatter.formatNumericData(dataEmbarqueTextField.getText()).matches("\\d{8}") ||
            !StringFormatter.formatNumericData(dataDesembarqueTextField.getText()).matches("\\d{8}") ||
            !StringFormatter.formatNumericData(horarioEmbarqueTextField.getText()).matches("\\d{3,4}") ||
            !StringFormatter.formatNumericData(horarioDesembarqueTextField.getText()).matches("\\d{3,4}")) {
            warningText.setText("Alguma data digitada é inválida");
            return false;
            }

            LocalDateTime.parse(StringFormatter.formatNumericData(dataEmbarqueTextField.getText()) + StringFormatter.formatNumericData(horarioEmbarqueTextField.getText()), DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));
            LocalDateTime.parse(StringFormatter.formatNumericData(dataDesembarqueTextField.getText()) + StringFormatter.formatNumericData(horarioDesembarqueTextField.getText()), DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Alguma data digitada é inválida");
            return false;
        }
        return true;
    }

    private Voo getVoo() {
        String numero = numeroTextField.getText().trim().toUpperCase();
        String origem = StringFormatter.capitalize(origemTextField.getText());
        String destino = StringFormatter.capitalize(destinoTextField.getText());

        String horarioFormatado = StringFormatter.formatNumericData(horarioEmbarqueTextField.getText());
        if (horarioFormatado.length() == 3) horarioFormatado = "0" + horarioFormatado;
        LocalDateTime horarioEmbarque = LocalDateTime.parse(
                StringFormatter.formatNumericData(dataEmbarqueTextField.getText()) + horarioFormatado, 
                DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));

        horarioFormatado = StringFormatter.formatNumericData(horarioDesembarqueTextField.getText());
        if (horarioFormatado.length() == 3) horarioFormatado = "0" + horarioFormatado;
        LocalDateTime horarioDesembarque = LocalDateTime.parse(
            StringFormatter.formatNumericData(dataDesembarqueTextField.getText()) + horarioFormatado, 
            DateTimeFormatter.ofPattern("ddMMyyyyHHmm"));

        Integer idPortaoEmbarque = ((PortaoEmbarque) portoes.get(portaoChoiceBox.getSelectionModel().getSelectedIndex())).getId();
        Integer idAeroportoChegada = ((Aeroporto) aeroportos.get(aeroportoDesembarqueChoiceBox.getSelectionModel().getSelectedIndex())).getId();
        Integer idAeronave = ((Aeronave) aeronaves.get(aeronaveChoiceBox.getSelectionModel().getSelectedIndex())).getId();
        String status = statuses.get(statusChoiceBox.getSelectionModel().getSelectedIndex());

        return new Voo(numero, status, origem, destino, horarioEmbarque, horarioDesembarque, idAeronave, idPortaoEmbarque, idAeroportoChegada);
    }

}
