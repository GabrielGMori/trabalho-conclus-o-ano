package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.tca.App;
import com.tca.model.PortaoEmbarque;
import com.tca.repository.PortaoEmbarqueRepository;
import com.tca.util.JavaFXElementBuilder;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class VisualizacaoPortoesControllerFXML implements Initializable {
    private static Integer idAeroporto;

    private PortaoEmbarqueRepository portaoEmbarqueRepository = PortaoEmbarqueRepository.getInstance();

    @FXML
    private Pane filtrosPane;

    @FXML
    private TextField codigoTextField;

    @FXML
    private ChoiceBox<String> disponivelChoiceBox;

    @FXML
    private ListView<HBox> portoesListView;

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
        disponivelChoiceBox.getItems().clear();
    }

    @FXML
    void confirmarFiltros(ActionEvent event) {
        warningText.setText("");

        portoesListView.getItems().clear();

        ArrayList<?> portoes;
        ArrayList<?> params = getFiltros();

        try {
            portoes = portaoEmbarqueRepository.getPortoesEmbarqueFiltro(
                    (String) params.get(0),
                    (Boolean) params.get(1),
                    idAeroporto);
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        if (portoes.isEmpty() || portoes == null) {
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        portoesListView.getItems().clear();
        buildListView(portoes);
        filtrosPane.setVisible(false);
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        disponivelChoiceBox.getItems().addAll("Sim", "Não");

        filtros = new ArrayList<>(Arrays.asList(codigoTextField));

        ArrayList<?> portoes;
        try {
            portoes = portaoEmbarqueRepository.getPortoesEmbarqueFiltro(null, null, idAeroporto);
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            return;
        }

        if (portoes.isEmpty() || portoes == null) {
            naoEncontrado();
            return;
        }

        portoesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> arg0, HBox arg1, HBox arg2) {
                try {
                    editar(Integer.valueOf(portoesListView.getSelectionModel().getSelectedItem().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buildListView(portoes);
    }

    private void naoEncontrado() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Text text = JavaFXElementBuilder.text("Nenhum portão de embarque encontrado", 20, FontWeight.BOLD, "#333333");
        hbox.getChildren().add(text);

        portoesListView.getItems().add(hbox);
        return;
    }

    private void editar(Integer id) throws IOException {
        // TODO
        App.setRoot("editarVoo");
    }

    private void buildListView(ArrayList<?> portoes) {
        for (int i = 0; i < portoes.size(); i++) {
            PortaoEmbarque portao = (PortaoEmbarque) portoes.get(i);

            HBox mainBox = new HBox();
            mainBox.setId(portao.getId().toString());
            mainBox.setSpacing(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setAlignment(Pos.CENTER);

            Text codigoText = JavaFXElementBuilder.text("Código: " + portao.getCodigo(), 20, FontWeight.BOLD, "#333333");
            Text disponivelText = JavaFXElementBuilder.text("Disponível: " + (portao.getDisponivel() == true ? "Sim" : "Não"), 20, FontWeight.NORMAL, "#333333");
           
            mainBox.getChildren().addAll(codigoText, disponivelText);
            portoesListView.getItems().add(mainBox);
        }
    }

    private ArrayList<?> getFiltros() {
        String codigo = !codigoTextField.getText().trim().isEmpty() ? codigoTextField.getText().trim() : null;

        Boolean disponivel = null;
        String selected = disponivelChoiceBox.getSelectionModel().getSelectedItem();
        if (selected != null) disponivel = selected.equals("Sim");

        return new ArrayList<>(Arrays.asList(codigo, disponivel));
    }

    public static void setIdAeroporto(int id) {
        idAeroporto = id;
    }

}
