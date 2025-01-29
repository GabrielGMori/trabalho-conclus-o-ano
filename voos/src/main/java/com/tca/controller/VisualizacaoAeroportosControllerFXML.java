package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.tca.App;
import com.tca.model.Aeroporto;
import com.tca.repository.AeroportoRepository;
import com.tca.util.JavaFXElementBuilder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class VisualizacaoAeroportosControllerFXML implements Initializable {
    private AeroportoRepository aeroportoRepository = AeroportoRepository.getInstance();

    @FXML
    private Pane filtrosPane;

    @FXML
    private ListView<HBox> aeroportosListView;

    @FXML
    private TextField localizacaoTextField;

    @FXML
    private TextField nomeTextField;

    @FXML
    private Text warningText;

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

        aeroportosListView.getItems().clear();

        ArrayList<?> aeroportos;
        ArrayList<?> params = getFiltros();

        try {
            aeroportos = aeroportoRepository.getAeroportosFiltro(
                    (String) params.get(0),
                    (String) params.get(1));
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        if (aeroportos.isEmpty() || aeroportos == null) {
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        aeroportosListView.getItems().clear();
        buildListView(aeroportos);
        filtrosPane.setVisible(false);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        filtros = new ArrayList<>(Arrays.asList(nomeTextField, localizacaoTextField));

        ArrayList<?> aeroportos;
        try {
            aeroportos = aeroportoRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            return;
        }

        if (aeroportos.isEmpty() || aeroportos == null) {
            naoEncontrado();
            return;
        }

        aeroportosListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> arg0, HBox arg1, HBox arg2) {
                try {
                    editar(Integer.valueOf(aeroportosListView.getSelectionModel().getSelectedItem().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buildListView(aeroportos);
    }

    private void naoEncontrado() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Text text = JavaFXElementBuilder.text("Nenhum aeroporto encontrado", 20, FontWeight.BOLD, "#333333");
        hbox.getChildren().add(text);

        aeroportosListView.getItems().add(hbox);
        return;
    }

    private void editar(Integer id) throws IOException {
        // TODO
        App.setRoot("editarVoo");
    }

    private void buildListView(ArrayList<?> aeroportos) {
        for (int i = 0; i < aeroportos.size(); i++) {
            Aeroporto aeroporto = (Aeroporto) aeroportos.get(i);

            HBox mainBox = new HBox();
            mainBox.setId(aeroporto.getId().toString());
            mainBox.setSpacing(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setAlignment(Pos.CENTER);

            Text nomeText = JavaFXElementBuilder.text("Nome: " + aeroporto.getNome(), 20, FontWeight.BOLD, "#333333");
            Text localizacaoText = JavaFXElementBuilder.text("Localização: " + aeroporto.getLocalizacao(), 20, FontWeight.NORMAL, "#333333");

            Button verPortoesButton = new Button("Portões de embarque");
            verPortoesButton.setFont(Font.font("Yu Gothic", FontWeight.BOLD, 20));
            verPortoesButton.setStyle("-fx-text-fill: #f1ffe7; -fx-background-color: #3FC2FF;");
            verPortoesButton.setOnAction(event -> {
                try {
                    verPortoes(aeroporto.getId());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
           
            mainBox.getChildren().addAll(nomeText, localizacaoText, verPortoesButton);
            aeroportosListView.getItems().add(mainBox);
        }
    }

    private ArrayList<?> getFiltros() {
        String nome = !nomeTextField.getText().trim().isEmpty() ? nomeTextField.getText().trim() : null;
        String localizacao = !localizacaoTextField.getText().trim().isEmpty() ? localizacaoTextField.getText().trim() : null;

        return new ArrayList<>(Arrays.asList(nome, localizacao));
    }

    private void verPortoes(Integer idAeroporto) throws IOException {
        VisualizacaoPortoesControllerFXML.setIdAeroporto(idAeroporto);
        App.setRoot("visualizacaoPortoes");
    }

}
