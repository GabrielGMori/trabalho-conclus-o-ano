package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.tca.App;
import com.tca.model.Administrador;
import com.tca.repository.AdministradorRepository;
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

public class VisualizacaoAdministradoresControllerFXML implements Initializable {
    private AdministradorRepository administradorRepository = AdministradorRepository.getInstance();

    @FXML
    private ListView<HBox> administradoresListView;

    @FXML
    private Pane filtrosPane;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField cpfTextField;

    @FXML
    private Text warningText;

    private ArrayList<TextField> filtros;
    private ArrayList<String> filtrosTipos;

    @FXML
    void verFiltros(ActionEvent event) {
        filtrosPane.setVisible(true);
    }

    @FXML
    void criar(ActionEvent event) throws IOException {
        // Add your logic here
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
    void sair(ActionEvent event) throws IOException {
        App.deslogar();
        App.setRoot("escolherLogin");
    }

    @FXML
    void limparFiltros(ActionEvent event) throws IOException {
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

        administradoresListView.getItems().clear();

        ArrayList<?> administradores;
        ArrayList<?> params = getFiltros();

        try {
            administradores = administradorRepository.getAdministradoresFiltro(
                    (String) params.get(0),
                    (String) params.get(1));
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        if (administradores.isEmpty() || administradores == null) {
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        administradoresListView.getItems().clear();
        buildListView(administradores);
        filtrosPane.setVisible(false);
    }

     private Boolean validarFiltros() {
        for (int i = 0; i < filtros.size(); i++) {
            if (!filtros.get(i).getText().trim().isEmpty()) {
                if (filtrosTipos.get(i).equals("Cpf")) {
                    String text = StringFormatter.formatNumericData(filtros.get(i).getText());
                    if (!text.matches("\\d{11}")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        filtros = new ArrayList<>(Arrays.asList(cpfTextField, nomeTextField));
        filtrosTipos = new ArrayList<>(Arrays.asList("Cpf", "String"));

        ArrayList<?> administradores;
        try {
            administradores = administradorRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            return;
        }

        if (administradores.isEmpty() || administradores == null) {
            naoEncontrado();
            return;
        }

        administradoresListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> arg0, HBox arg1, HBox arg2) {
                try {
                    editar(Integer.valueOf(administradoresListView.getSelectionModel().getSelectedItem().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buildListView(administradores);
    }

    private void naoEncontrado() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Text text = JavaFXElementBuilder.text("Nenhum administrador encontrado", 20, FontWeight.BOLD, "#333333");
        hbox.getChildren().add(text);

        administradoresListView.getItems().add(hbox);
        return;
    }

    private void editar(Integer id) throws IOException {
        // TODO
        App.setRoot("editarVoo");
    }

    private void buildListView(ArrayList<?> administradores) {
        for (int i = 0; i < administradores.size(); i++) {
            Administrador administrador = (Administrador) administradores.get(i);

            HBox mainBox = new HBox();
            mainBox.setId(administrador.getCpf());
            mainBox.setSpacing(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setAlignment(Pos.CENTER);

            Text cpfText = JavaFXElementBuilder.text("CPF: " + administrador.getCpf().replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"), 20, FontWeight.BOLD, "#333333");
            Text nomeText = JavaFXElementBuilder.text("Nome: " + administrador.getNome(), 20, FontWeight.NORMAL, "#333333");
           
            mainBox.getChildren().addAll(cpfText, nomeText);
            administradoresListView.getItems().add(mainBox);
        }
    }

    private ArrayList<?> getFiltros() {
        String cpf = !cpfTextField.getText().trim().isEmpty() ? cpfTextField.getText().trim() : null;
        String nome = !nomeTextField.getText().trim().isEmpty() ? nomeTextField.getText().trim() : null;

        return new ArrayList<>(Arrays.asList(cpf, nome));
    }

}
