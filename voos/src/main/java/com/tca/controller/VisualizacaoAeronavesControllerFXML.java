package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Aeronave;
import com.tca.model.CompanhiaAerea;
import com.tca.repository.AeronaveRepository;
import com.tca.repository.CompanhiaAereaRepository;
import com.tca.util.JavaFXElementBuilder;
import com.tca.util.StringFormatter;

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

public class VisualizacaoAeronavesControllerFXML implements Initializable {
    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();
    private CompanhiaAereaRepository companhiaAereaRepository = CompanhiaAereaRepository.getInstance();

    @FXML
    private TextField companhiaTextField;

    @FXML
    private Pane filtrosPane;

    @FXML
    private TextField modeloTextField;

    @FXML
    private ListView<HBox> aeronavesListView;

    @FXML
    private Text warningText;

    @FXML
    private TextField capacidadeTextField;

    private ArrayList<TextField> filtros;

    @FXML
    void verFiltros(ActionEvent event) {
        filtrosPane.setVisible(true);
    }

    @FXML
    void criar(ActionEvent event) {

    }

    @FXML
    void verVoos(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoVoos");
    }

    @FXML
    void verCompanhias(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoCompanhias");
    }

    @FXML
    void verAeroportos(ActionEvent event) throws IOException {
        App.setRoot("visualizacaoAeroportos");
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

        aeronavesListView.getItems().clear();

        ArrayList<?> aeronaves;
        ArrayList<?> params = getFiltros();

        try {
            aeronaves = aeronaveRepository.getAeronavesFiltro(
                    (String) params.get(0),
                    (Integer) params.get(1),
                    (String) params.get(2));
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        if (aeronaves.isEmpty() || aeronaves == null) {
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        aeronavesListView.getItems().clear();
        buildListView(aeronaves);
        filtrosPane.setVisible(false);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        filtros = new ArrayList<>(Arrays.asList(modeloTextField, capacidadeTextField, companhiaTextField));

        ArrayList<?> aeronaves;
        try {
            aeronaves = aeronaveRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            return;
        }

        if (aeronaves.isEmpty() || aeronaves == null) {
            naoEncontrado();
            return;
        }

        aeronavesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> arg0, HBox arg1, HBox arg2) {
                try {
                    editar(Integer.valueOf(aeronavesListView.getSelectionModel().getSelectedItem().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buildListView(aeronaves);
    }

    private void naoEncontrado() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Text text = JavaFXElementBuilder.text("Nenhuma aeronave encontrada", 20, FontWeight.BOLD, "#333333");
        hbox.getChildren().add(text);

        aeronavesListView.getItems().add(hbox);
        return;
    }

    private void editar(Integer id) throws IOException {
        // TODO
        App.setRoot("editarVoo");
    }

    private void buildListView(ArrayList<?> aeronaves) {
        for (int i = 0; i < aeronaves.size(); i++) {
            Aeronave aeronave = (Aeronave) aeronaves.get(i);
            String nomeCompanhia;
            try {
                Resultado result = companhiaAereaRepository.get(aeronave.getIdCompanhiaAerea());
                if (result.foiErro()) {
                    System.out.println(result.comoErro().getMsg());
                    nomeCompanhia = null;
                }
                CompanhiaAerea companhia = (CompanhiaAerea) result.comoSucesso().getObj();
                nomeCompanhia = companhia.getNome();

            } catch (Exception e) {
                e.printStackTrace();
                nomeCompanhia = null;
            }

            HBox mainBox = new HBox();
            mainBox.setId(aeronave.getId().toString());
            mainBox.setSpacing(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setAlignment(Pos.CENTER);

            Text idText = JavaFXElementBuilder.text("ID: " + aeronave.getId(), 20, FontWeight.BOLD, "#333333");
            Text modeloText = JavaFXElementBuilder.text("Modelo: " + aeronave.getModelo(), 20, FontWeight.BOLD, "#333333");
            Text capacidadeText = JavaFXElementBuilder.text("Capacidade: " + aeronave.getCapacidade(), 20, FontWeight.NORMAL, "#333333");
            Text companhiaText = null;
            if (nomeCompanhia != null)
                companhiaText = JavaFXElementBuilder.text(nomeCompanhia, 20, FontWeight.BOLD, "#333333");
           
            mainBox.getChildren().addAll(idText, modeloText, capacidadeText, companhiaText);
            aeronavesListView.getItems().add(mainBox);
        }
    }

    private ArrayList<?> getFiltros() {
        String codigo = !modeloTextField.getText().trim().isEmpty() ? modeloTextField.getText().trim() : null;
        Integer capacidade = !StringFormatter.formatNumericData(capacidadeTextField.getText()).isEmpty() ? Integer.valueOf(StringFormatter.formatNumericData(capacidadeTextField.getText())) : null;
        String companhia = !companhiaTextField.getText().trim().isEmpty() ? companhiaTextField.getText().trim() : null;

        return new ArrayList<>(Arrays.asList(codigo, capacidade, companhia));
    }

}
