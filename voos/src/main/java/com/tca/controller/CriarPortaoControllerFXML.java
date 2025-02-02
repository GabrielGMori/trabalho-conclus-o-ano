package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Aeroporto;
import com.tca.model.PortaoEmbarque;
import com.tca.repository.AeroportoRepository;
import com.tca.repository.PortaoEmbarqueRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CriarPortaoControllerFXML implements Initializable {
    private PortaoEmbarqueRepository portaoEmbarqueRepository = PortaoEmbarqueRepository.getInstance();
    private AeroportoRepository aeroportoRepository = AeroportoRepository.getInstance();

    private ArrayList<?> aeroportos; 

    @FXML
    private TextField codigoTextField;

    @FXML
    private ChoiceBox<String> aeroportoChoiceBox;

    @FXML
    private Text warningText;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoPortoes");
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false) {
            PortaoEmbarque portao;
            try {
                portao = getPortao();
                Resultado result = portaoEmbarqueRepository.criar(portao);
                if (result.foiErro()) { 
                    warningText.setText("Erro: " + result.comoErro().getMsg());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
            VisualizacaoPortoesControllerFXML.setIdAeroporto(portao.getIdAeroporto());
            App.setRoot("visualizacaoPortoes");
        }
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            aeroportos = aeroportoRepository.listar();
            if (aeroportos.isEmpty()) {
                warningText.setText("Não será possível criar um voo pois nenhum aeroporto está registrado no sistema");
            }
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        } 

        for (int i=0; i<aeroportos.size(); i++) {
            Aeroporto aeroporto = (Aeroporto) aeroportos.get(i);
            aeroportoChoiceBox.getItems().add(aeroporto.getNome());
            if (aeroporto.getId() == VisualizacaoPortoesControllerFXML.getIdAeroporto()) {
                aeroportoChoiceBox.getSelectionModel().select(i);
            }
        }
    }

    private Boolean verificarCamposVazios() {
        if (codigoTextField.getText().isEmpty() ||
            aeroportoChoiceBox.getSelectionModel().getSelectedIndex() < 0) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    private PortaoEmbarque getPortao() {
        String codigo = codigoTextField.getText().trim().toUpperCase();
        Integer idAeroporto = ((Aeroporto) aeroportos.get(aeroportoChoiceBox.getSelectionModel().getSelectedIndex())).getId();

        return new PortaoEmbarque(codigo, true, idAeroporto);
    }

}
