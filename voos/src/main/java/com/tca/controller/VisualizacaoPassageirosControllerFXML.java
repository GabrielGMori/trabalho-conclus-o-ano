package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Pais;
import com.tca.model.Passageiro;
import com.tca.repository.PaisRepository;
import com.tca.repository.PassageiroRepository;
import com.tca.util.JavaFXElementBuilder;
import com.tca.util.StringFormatter;

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
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class VisualizacaoPassageirosControllerFXML implements Initializable {
    private PassageiroRepository passageiroRepository = PassageiroRepository.getInstance();
    private PaisRepository paisRepository = PaisRepository.getInstance();
    private ArrayList<?> paises;

    @FXML
    private Pane filtrosPane;

    @FXML
    private TextField passaporteTextField;

    @FXML
    private ChoiceBox<String> nacionalidadeChoiceBox;

    @FXML
    private TextField nomeTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField telefoneTextField;

    @FXML
    private ListView<HBox> passageirosListView;

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
        App.setRoot("criarPassageiro");
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
        nacionalidadeChoiceBox.getSelectionModel().clearSelection();
    }

    @FXML
    void confirmarFiltros(ActionEvent event) {
        warningText.setText("");
        if (validarFiltros() == false) {
            warningText.setText("Algum filtro é inválido");
            return;
        }

        passageirosListView.getItems().clear();

        ArrayList<?> passageiros;
        ArrayList<?> params = getFiltros();

        try {
            passageiros = passageiroRepository.getPassageirosFiltro(
                    (String) params.get(0),
                    (String) params.get(1),
                    (String) params.get(2),
                    (String) params.get(3),
                    (String) params.get(4),
                    (Integer) params.get(5));
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        if (passageiros.isEmpty() || passageiros == null) {
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        passageirosListView.getItems().clear();
        buildListView(passageiros);
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
                if (filtrosTipos.get(i).equals("Telephone")) {
                    String text = StringFormatter.formatNumericData(filtros.get(i).getText());
                    if (!text.matches("\\d{11}")) {
                        return false;
                    }
                }
                if (filtrosTipos.get(i).equals("Email")) {
                    String text = filtros.get(i).getText().trim();
                    if (!text.contains("@")) {
                        return false;
                    }
                }
                if (filtrosTipos.get(i).equals("Passport")) {
                    String text = StringFormatter.formatNumericData(filtros.get(i).getText());
                    if (!text.matches("[A-Za-z]{2}\\d{6}")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            paises = paisRepository.listar();
            if (paises == null || paises.isEmpty() || !(paises.stream().allMatch(element -> element instanceof Pais))) {
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        for (int i=0; i<paises.size(); i++) {
            Pais pais = (Pais) paises.get(i);
            nacionalidadeChoiceBox.getItems().add(pais.getNome());
        }

        filtros = new ArrayList<>(Arrays.asList(cpfTextField, emailTextField, nomeTextField, passaporteTextField, telefoneTextField));
        filtrosTipos = new ArrayList<>(Arrays.asList("Cpf", "Email", "String", "Passport", "Telephone"));

        ArrayList<?> passageiros;
        try {
            passageiros = passageiroRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            return;
        }

        if (passageiros.isEmpty() || passageiros == null) {
            naoEncontrado();
            return;
        }

        passageirosListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> arg0, HBox arg1, HBox arg2) {
                try {
                    editar(passageirosListView.getSelectionModel().getSelectedItem().getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buildListView(passageiros);
    }

     private void naoEncontrado() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Text text = JavaFXElementBuilder.text("Nenhum passageiro encontrado", 20, FontWeight.BOLD, "#333333");
        hbox.getChildren().add(text);

        passageirosListView.getItems().add(hbox);
        return;
    }

    private void editar(String cpf) throws IOException {
        EditarPassageiroControllerFXML.setCpfPassageiro(cpf);
        App.setRoot("editarPassageiro");
    }

    private void buildListView(ArrayList<?> passageiros) {
        for (int i = 0; i < passageiros.size(); i++) {
            Passageiro passageiro = (Passageiro) passageiros.get(i);
            String nomePais;
            try {
                Resultado result = paisRepository.get(passageiro.getIdPais());
                if (result.foiErro()) {
                    System.out.println(result.comoErro().getMsg());
                    nomePais = null;
                }
                Pais pais = (Pais) result.comoSucesso().getObj();
                nomePais = pais.getNome();

            } catch (Exception e) {
                e.printStackTrace();
                nomePais = null;
            }

            HBox mainBox = new HBox();
            mainBox.setId(passageiro.getCpf());
            mainBox.setSpacing(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setAlignment(Pos.CENTER);

            VBox cpfEmailVBox = new VBox();
            cpfEmailVBox.setSpacing(10);
            cpfEmailVBox.setAlignment(Pos.CENTER);

            VBox nomeNacionalidadeVBox = new VBox();
            nomeNacionalidadeVBox.setSpacing(10);
            nomeNacionalidadeVBox.setAlignment(Pos.CENTER);

            VBox passaporteTelefoneVBox = new VBox();
            passaporteTelefoneVBox.setSpacing(10);
            passaporteTelefoneVBox.setAlignment(Pos.CENTER);

            Text cpfText = JavaFXElementBuilder.text("CPF: " + passageiro.getCpf().replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"), 20, FontWeight.BOLD, "#333333");
            Text emailText = JavaFXElementBuilder.text("E-mail: " + passageiro.getEmail(), 20, FontWeight.BOLD, "#333333");

            Text nomeText = JavaFXElementBuilder.text("Nome: " + passageiro.getNome(), 20, FontWeight.NORMAL, "#333333");
            Text paisText = null;
            if (nomePais != null)
                paisText = JavaFXElementBuilder.text("País: " + nomePais, 20, FontWeight.NORMAL, "#333333");

            Text passaporteText = JavaFXElementBuilder.text("Passaporte: " + passageiro.getPassaporte(), 20, FontWeight.NORMAL, "#333333");
            Text telefoneText = JavaFXElementBuilder.text("Telefone: " + passageiro.getTelefone().replaceAll("(\\d{2})(\\d{5})(\\d{4})", "($1) $2-$3"), 20, FontWeight.NORMAL, "#333333");

            cpfEmailVBox.getChildren().addAll(cpfText, emailText);
            nomeNacionalidadeVBox.getChildren().add(nomeText);
            if (paisText != null)
                nomeNacionalidadeVBox.getChildren().add(paisText);
            passaporteTelefoneVBox.getChildren().addAll(passaporteText, telefoneText);

            mainBox.getChildren().addAll(cpfEmailVBox, nomeNacionalidadeVBox, passaporteTelefoneVBox);
            passageirosListView.getItems().add(mainBox);
        }
    }

    private ArrayList<?> getFiltros() {
        String cpf = !StringFormatter.formatNumericData(cpfTextField.getText()).isEmpty() ? StringFormatter.formatNumericData(cpfTextField.getText()).trim() : null;
        String email = !emailTextField.getText().trim().isEmpty() ? emailTextField.getText().trim() : null;
        String nome = !nomeTextField.getText().trim().isEmpty() ? nomeTextField.getText().trim() : null;
        String passaporte = !StringFormatter.formatNumericData(passaporteTextField.getText()).isEmpty() ? StringFormatter.formatNumericData(passaporteTextField.getText()).trim() : null;
        String telefone = !StringFormatter.formatNumericData(telefoneTextField.getText()).isEmpty() ? StringFormatter.formatNumericData(telefoneTextField.getText()).trim() : null;

        Integer idPais = null;
        if (nacionalidadeChoiceBox.getSelectionModel().getSelectedIndex() >= 0) {
            Integer indexPais = nacionalidadeChoiceBox.getSelectionModel().getSelectedIndex();
            Pais metodoPagamento = (Pais) paises.get(indexPais);
            idPais = metodoPagamento.getId();
        }

        return new ArrayList<>(Arrays.asList(cpf, email, nome, passaporte, telefone, idPais));
    }

}
