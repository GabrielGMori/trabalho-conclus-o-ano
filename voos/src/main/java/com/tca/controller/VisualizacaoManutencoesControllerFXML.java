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
import com.tca.repository.ManutencaoRepository;
import com.tca.util.JavaFXElementBuilder;
import com.tca.util.StringFormatter;
import com.tca.repository.AeronaveRepository;

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

public class VisualizacaoManutencoesControllerFXML implements Initializable {
    private ManutencaoRepository manutencaoRepository = ManutencaoRepository.getInstance();
    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();
    private ArrayList<String> statuses;

    @FXML
    private TextField horarioFimInicioTextField;

    @FXML
    private TextField dataFimInicioTextField;

    @FXML
    private Pane filtrosPane;

    @FXML
    private ChoiceBox<String> statusChoiceBox;

    @FXML
    private ListView<HBox> manutencoesListView;

    @FXML
    private TextField horarioFimFimTextField;

    @FXML
    private TextField descricaoTextField;

    @FXML
    private TextField dataFimFimTextField;

    @FXML
    private TextField dataInicioFimTextField;

    @FXML
    private TextField horarioInicioInicioTextField;

    @FXML
    private TextField aeronaveTextField;

    @FXML
    private TextField horarioInicioFimTextField;

    @FXML
    private TextField dataInicioInicioTextField;

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
        App.setRoot("criarManutencao");
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
        statusChoiceBox.getSelectionModel().clearSelection();
    }

    @FXML
    void confirmarFiltros(ActionEvent event) {
        warningText.setText("");
        if (validarFiltros() == false) {
            warningText.setText("Algum filtro é inválido");
            return;
        }

        manutencoesListView.getItems().clear();

        ArrayList<?> manutencoes;
        ArrayList<?> params = getFiltros();

        try {
            manutencoes = manutencaoRepository.getManutencaosFiltro(
                    (String) params.get(0),
                    (LocalDateTime) params.get(1),
                    (LocalDateTime) params.get(2),
                    (LocalDateTime) params.get(3),
                    (LocalDateTime) params.get(4),
                    (String) params.get(5),
                    (String) params.get(6));
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        if (manutencoes.isEmpty() || manutencoes == null) {
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        manutencoesListView.getItems().clear();
        buildListView(manutencoes);
        filtrosPane.setVisible(false);
    }

    private Boolean validarFiltros() {
        for (int i = 0; i < filtros.size(); i++) {
            if (!filtros.get(i).getText().trim().isEmpty()) {
                if (filtrosTipos.get(i).equals("Date")) {
                    String text = StringFormatter.formatNumericData(filtros.get(i).getText());
                    if (!text.matches("\\d{8}")) {
                        return false;
                    }
                }
                if (filtrosTipos.get(i).equals("Time")) {
                    String text = StringFormatter.formatNumericData(filtros.get(i).getText());
                    if (!text.matches("\\d{4}")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        statuses = new ArrayList<>(Arrays.asList("Agendada", "Atrasada", "Em andamento", "Cancelada", "Finalizada"));
        for (int i=0; i<statuses.size(); i++) statusChoiceBox.getItems().add(statuses.get(i));

        filtros = new ArrayList<>(Arrays.asList(descricaoTextField, dataInicioInicioTextField, dataInicioFimTextField, horarioInicioInicioTextField, horarioInicioFimTextField, dataFimInicioTextField, dataFimFimTextField, horarioFimInicioTextField, horarioFimFimTextField, aeronaveTextField));
        filtrosTipos = new ArrayList<>(Arrays.asList("String", "Date", "Date", "Time", "Time", "Date", "Date", "Time", "Time", "String"));

        ArrayList<?> manutencoes;
        try {
            manutencoes = manutencaoRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            return;
        }

        if (manutencoes.isEmpty() || manutencoes == null) {
            naoEncontrado();
            return;
        }

        manutencoesListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> arg0, HBox arg1, HBox arg2) {
                try {
                    editar(Integer.valueOf(manutencoesListView.getSelectionModel().getSelectedItem().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buildListView(manutencoes);
    }

    private void naoEncontrado() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Text text = JavaFXElementBuilder.text("Nenhuma manutenção encontrada", 20, FontWeight.BOLD, "#333333");
        hbox.getChildren().add(text);

        manutencoesListView.getItems().add(hbox);
        return;
    }

    private void editar(Integer id) throws IOException {
        EditarManutencaoControllerFXML.setIdManutencao(id);
        App.setRoot("editarManutencao");
    }

    private void buildListView(ArrayList<?> manutencoes) {
        for (int i = 0; i < manutencoes.size(); i++) {
            Manutencao manutencao = (Manutencao) manutencoes.get(i);
            String modeloAeronave;
            String idAeronave;
            try {
                Resultado result = aeronaveRepository.get(manutencao.getIdAeronave());
                if (result.foiErro()) {
                    System.out.println(result.comoErro().getMsg());
                    modeloAeronave = null;
                    idAeronave = null;
                }
                Aeronave aeronave = (Aeronave) result.comoSucesso().getObj();
                modeloAeronave = aeronave.getModelo();
                idAeronave = String.valueOf(aeronave.getId());

            } catch (Exception e) {
                e.printStackTrace();
                modeloAeronave = null;
                idAeronave = null;
            }

            HBox mainBox = new HBox();
            mainBox.setId(manutencao.getId().toString());
            mainBox.setSpacing(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setAlignment(Pos.CENTER);

            VBox inicioFimVBox = new VBox();
            inicioFimVBox.setSpacing(10);
            inicioFimVBox.setAlignment(Pos.CENTER);

            Text aeronaveText = null;
            if (modeloAeronave != null)
                aeronaveText = JavaFXElementBuilder.text(idAeronave + " " + modeloAeronave, 20, FontWeight.BOLD, "#333333");
            
            Text descricaoText = JavaFXElementBuilder.text("Desc.: " + manutencao.getDescricao(), 20, FontWeight.NORMAL, "#333333");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY HH:mm");
            Text inicioText = JavaFXElementBuilder.text("Ini.: " + manutencao.getDataInicio().format(formatter), 20,
                    FontWeight.NORMAL, "#333333");
            Text fimText = JavaFXElementBuilder.text("Fim: " + manutencao.getDataFim().format(formatter),
                    20, FontWeight.NORMAL, "#333333");

            Text statusText = JavaFXElementBuilder.text(manutencao.getStatus(), 20, FontWeight.BOLD, "#333333");

            inicioFimVBox.getChildren().addAll(inicioText, fimText);
            mainBox.getChildren().addAll(aeronaveText, descricaoText, inicioFimVBox, statusText);
            manutencoesListView.getItems().add(mainBox);
        }
    }

    private ArrayList<?> getFiltros() {
        String descricao = !descricaoTextField.getText().trim().isEmpty() ? descricaoTextField.getText().trim() : null;
        String aeronave = !aeronaveTextField.getText().trim().isEmpty() ? aeronaveTextField.getText().trim() : null;
        String dataInicioInicio = !dataInicioInicioTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataInicioInicioTextField.getText())
                : null;
        String dataInicioFim = !dataInicioFimTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataInicioFimTextField.getText())
                : null;
        String horarioInicioInicio = !horarioInicioInicioTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(horarioInicioInicioTextField.getText())
                : null;
        String horarioInicioFim = !horarioInicioFimTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(horarioInicioFimTextField.getText())
                : null;
        String dataFimInicio = !dataFimInicioTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataFimInicioTextField.getText())
                : null;
        String dataFimFim = !dataFimFimTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataFimFimTextField.getText())
                : null;
        String horarioFimInicio = !horarioFimInicioTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(horarioFimInicioTextField.getText())
                : null;
        String horarioFimFim = !horarioFimFimTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(horarioFimFimTextField.getText())
                : null;

        if (dataInicioInicio != null && horarioInicioInicio == null) {
            horarioInicioInicio = "0000";
        } else if (dataInicioInicio == null && horarioInicioInicio != null) {
            dataInicioInicio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        }

        if (dataInicioFim != null && horarioInicioFim == null) {
            horarioInicioFim = "0000";
        } else if (dataInicioFim == null && horarioInicioFim != null) {
            dataInicioFim = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        }

        if (dataFimInicio != null && horarioFimInicio == null) {
            horarioFimInicio = "0000";
        } else if (dataFimInicio == null && horarioFimInicio != null) {
            dataFimInicio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        }

        if (dataFimFim != null && horarioFimFim == null) {
            horarioFimFim = "0000";
        } else if (dataFimFim == null && horarioFimFim != null) {
            dataFimFim = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        }

        LocalDateTime inicioInicio;
        LocalDateTime inicioFim;
        LocalDateTime fimInicio;
        LocalDateTime fimFim;
        try {
            inicioInicio = dataInicioInicio != null
            ? LocalDateTime.parse(dataInicioInicio + horarioInicioInicio, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
            : null;
        } catch(Exception e) {
            e.printStackTrace();
            inicioInicio = null;
        }
        try {
            inicioFim = dataInicioFim != null
            ? LocalDateTime.parse(dataInicioFim + horarioInicioFim, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
            : null;
        } catch(Exception e) {
            e.printStackTrace();
            inicioFim = null;
        }
        try {
            fimInicio = dataFimInicio != null
            ? LocalDateTime.parse(dataFimInicio + horarioFimInicio, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
            : null;
        } catch(Exception e) {
            e.printStackTrace();
            fimInicio = null;
        }
        try {
            fimFim = dataFimFim != null
            ? LocalDateTime.parse(dataFimFim + horarioFimFim, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
            : null;
        } catch(Exception e) {
            e.printStackTrace();
            fimFim = null;
        }

        String status  = null;
        if (statusChoiceBox.getSelectionModel().getSelectedIndex() >= 0) status = statusChoiceBox.getSelectionModel().getSelectedItem();

        return new ArrayList<>(Arrays.asList(descricao, inicioInicio, inicioFim, fimInicio, fimFim, aeronave, status));
    }

}
