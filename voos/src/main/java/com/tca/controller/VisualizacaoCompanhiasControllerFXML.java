package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.tca.App;
import com.tca.model.CompanhiaAerea;
import com.tca.repository.CompanhiaAereaRepository;
import com.tca.util.JavaFXElementBuilder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class VisualizacaoCompanhiasControllerFXML implements Initializable {
    private CompanhiaAereaRepository companhiaAereaRepository = CompanhiaAereaRepository.getInstance();

    @FXML
    private Pane filtrosPane;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField codigoIcaoTextField;

    @FXML
    private Text warningText;

    @FXML
    private ListView<HBox> companhiasListView;

    private ArrayList<TextField> filtros;
    private ArrayList<String> filtrosTipos;

    @FXML
    void verFiltros(ActionEvent event) {
        filtrosPane.setVisible(true);
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        App.setRoot("criarCompanhia");
    }

    @FXML
    void verVoos(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoVoos");
    }

    @FXML
    void verAeroportos(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoAeroportos");
    }

    @FXML
    void verAeronaves(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoAeronaves");
    }

    @FXML
    void verManutencoes(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoManutencoes");
    }

    @FXML
    void verPassageiros(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoPassageiros");
    }

    @FXML
    void verAdministradores(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoAdministradores");
    }

    @FXML
    void sair(ActionEvent event) throws IOException {
        App.deslogar();
        App.setRoot("escolherLogin");
    }

    @FXML
    void limparFiltros(ActionEvent event) {
        warningText.setText("");
        for (int i = 0; i < filtros.size(); i++) {
            filtros.get(i).setText("");
        }
    }

    @FXML
    void confirmarFiltros(ActionEvent event) {
        warningText.setText("");
        if (validarFiltros() == false) {
            warningText.setText("Algum filtro é inválido");
            return;
        }

        companhiasListView.getItems().clear();

        ArrayList<?> companhias;
        ArrayList<?> params = getFiltros();

        try {
            companhias = companhiaAereaRepository.getCompanhiasFiltro(
                    (String) params.get(0),
                    (String) params.get(1));
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        if (companhias.isEmpty() || companhias == null) {
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        companhiasListView.getItems().clear();
        buildListView(companhias);
        filtrosPane.setVisible(false);
    }

    private Boolean validarFiltros() {
        for (int i = 0; i < filtros.size(); i++) {
            if (!filtros.get(i).getText().trim().isEmpty()) {
                if (filtrosTipos.get(i).equals("Icao")) {
                    String text = filtros.get(i).getText().trim();
                    if (!text.matches("[A-Z]{1,4}")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        filtros = new ArrayList<>(Arrays.asList(codigoIcaoTextField, nomeTextField));
        filtrosTipos = new ArrayList<>(Arrays.asList("Icao", "String"));

        ArrayList<?> companhias;
        try {
            companhias = companhiaAereaRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            return;
        }

        if (companhias.isEmpty() || companhias == null) {
            naoEncontrado();
            return;
        }

        companhiasListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> arg0, HBox arg1, HBox arg2) {
                try {
                    editar(Integer.valueOf(companhiasListView.getSelectionModel().getSelectedItem().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buildListView(companhias);
    }

    private void naoEncontrado() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Text text = JavaFXElementBuilder.text("Nenhuma companhia aérea encontrada", 20, FontWeight.BOLD, "#333333");
        hbox.getChildren().add(text);

        companhiasListView.getItems().add(hbox);
        return;
    }

    private void editar(Integer id) throws IOException {
        EditarCompanhiaControllerFXML.setIdCompanhia(id);
        App.setRoot("editarCompanhia");
    }

    private void buildListView(ArrayList<?> manutencoes) {
        for (int i = 0; i < manutencoes.size(); i++) {
            CompanhiaAerea companhia = (CompanhiaAerea) manutencoes.get(i);

            HBox mainBox = new HBox();
            mainBox.setId(companhia.getId().toString());
            mainBox.setSpacing(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setAlignment(Pos.CENTER);

            Text icaoText = JavaFXElementBuilder.text("Código ICAO: " + companhia.getCodigoIcao(), 20, FontWeight.BOLD, "#333333");
            Text nomeText = JavaFXElementBuilder.text("Nome: " + companhia.getNome(), 20, FontWeight.NORMAL, "#333333");
           
            mainBox.getChildren().addAll(icaoText, nomeText);
            companhiasListView.getItems().add(mainBox);
        }
    }

    private ArrayList<?> getFiltros() {
        String icao = !codigoIcaoTextField.getText().trim().isEmpty() ? codigoIcaoTextField.getText().trim() : null;
        String nome = !nomeTextField.getText().trim().isEmpty() ? nomeTextField.getText().trim() : null;

        return new ArrayList<>(Arrays.asList(icao, nome));
    }

}
