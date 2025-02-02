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

public class EditarPortaoControllerFXML implements Initializable {
    private Boolean confirmar = false;

    private static Integer idPortao;
    private PortaoEmbarque portaoEmbarque;

    private PortaoEmbarqueRepository portaoEmbarqueRepository = PortaoEmbarqueRepository.getInstance();
    private AeroportoRepository aeroportoRepository = AeroportoRepository.getInstance();

    private ArrayList<?> aeroportos; 

    @FXML
    private TextField codigoTextField;

    @FXML
    private ChoiceBox<String> aeroportoChoiceBox;

    @FXML
    private ChoiceBox<String> disponivelChoiceBox;

    @FXML
    private Text warningText;

    @FXML
    void cancelar(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoPortoes");
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
            Resultado result = portaoEmbarqueRepository.deletar(idPortao);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            App.setRoot("visualizacaoPortoes");
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
        }
    }

    @FXML
    void confirmar(ActionEvent event) throws IOException {
        warningText.setText("");
        if (verificarCamposVazios() == false) {
            PortaoEmbarque portao;
            try {
                portao = getPortao();
                Resultado result = portaoEmbarqueRepository.atualizar(idPortao, portao);
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
            Resultado result = portaoEmbarqueRepository.get(idPortao);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            portaoEmbarque = (PortaoEmbarque) result.comoSucesso().getObj();

            aeroportos = aeroportoRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        for (int i=0; i<aeroportos.size(); i++) {
            Aeroporto aeroporto = (Aeroporto) aeroportos.get(i);
            aeroportoChoiceBox.getItems().add(aeroporto.getNome());
            if (aeroporto.getId() == portaoEmbarque.getIdAeroporto()) aeroportoChoiceBox.getSelectionModel().select(i);
        }

        disponivelChoiceBox.getItems().addAll("Sim", "Não");
        if (portaoEmbarque.getDisponivel() == true) disponivelChoiceBox.getSelectionModel().select(0);
        else disponivelChoiceBox.getSelectionModel().select(1);

        codigoTextField.setText(portaoEmbarque.getCodigo());
    }

    private Boolean verificarCamposVazios() {
        if (codigoTextField.getText().isEmpty() ||
            aeroportoChoiceBox.getSelectionModel().getSelectedIndex() < 0 ||
            disponivelChoiceBox.getSelectionModel().getSelectedIndex() < 0) {
            warningText.setText("Por favor, preencha todos os campos");
            return true;
        }
        return false;
    }

    public static void setIdPortao(int id) {
        idPortao = id;
    }

    private PortaoEmbarque getPortao() {
        String codigo = codigoTextField.getText().trim().toUpperCase();
        Integer idAeroporto = ((Aeroporto) aeroportos.get(aeroportoChoiceBox.getSelectionModel().getSelectedIndex())).getId();

        Boolean disponivel = null;
        String selected = disponivelChoiceBox.getSelectionModel().getSelectedItem();
        if (selected != null) disponivel = selected.equals("Sim");

        return new PortaoEmbarque(idPortao, codigo, disponivel, idAeroporto);
    }

}
