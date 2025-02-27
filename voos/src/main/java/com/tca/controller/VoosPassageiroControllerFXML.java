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
import com.tca.model.CompanhiaAerea;
import com.tca.model.Voo;
import com.tca.repository.AeronaveRepository;
import com.tca.repository.CompanhiaAereaRepository;
import com.tca.repository.VooRepository;
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
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class VoosPassageiroControllerFXML implements Initializable {
    private VooRepository vooRepository = VooRepository.getInstance();
    private CompanhiaAereaRepository companhiaAereaRepository = CompanhiaAereaRepository.getInstance();
    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();

    @FXML
    private TextField dataEmbarqueInicioTextField;

    @FXML
    private Pane filtrosPane;

    @FXML
    private TextField horarioEmbarqueFimTextField;

    @FXML
    private TextField dataDesembarqueInicioTextField;

    @FXML
    private TextField aeroportoDesembarqueTextField;

    @FXML
    private TextField dataEmbarqueFimTextField;

    @FXML
    private ListView<HBox> voosListView;

    @FXML
    private TextField aeroportoEmbarqueTextField;

    @FXML
    private TextField horarioEmbarqueInicioTextField;

    @FXML
    private TextField dataDesembarqueFimTextField;

    @FXML
    private TextField horarioDesembarqueInicioTextField;

    @FXML
    private TextField origemTextField;

    @FXML
    private TextField numeroTextField;

    @FXML
    private TextField horarioDesembarqueFimTextField;

    @FXML
    private Text warningText;

    @FXML
    private TextField destinoTextField;

    private ArrayList<TextField> filtros;
    private ArrayList<String> filtrosTipos;

    @FXML
    void verFiltros(ActionEvent event) {
        filtrosPane.setVisible(true);
    }

    @FXML
    void verPassagens(ActionEvent event) throws IOException {
        App.setRoot("passagensCompradas");
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

        voosListView.getItems().clear();

        ArrayList<?> voos;
        ArrayList<?> params = getFiltros();

        try {
            voos = vooRepository.getVoosFiltro(
                    (String) params.get(0),
                    (String) params.get(1),
                    (String) params.get(2),
                    (LocalDateTime) params.get(3),
                    (LocalDateTime) params.get(4),
                    (String) params.get(5),
                    (LocalDateTime) params.get(6),
                    (LocalDateTime) params.get(7),
                    (String) params.get(8),
                    (String) params.get(9));
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        if (voos.isEmpty() || voos == null) {
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        voosListView.getItems().clear();
        buildListView(voos);
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
                    if (!text.matches("\\d{3,4}")) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        filtros = new ArrayList<>(Arrays.asList(numeroTextField, origemTextField, destinoTextField,
                dataEmbarqueInicioTextField,
                dataEmbarqueFimTextField, horarioEmbarqueInicioTextField, horarioEmbarqueFimTextField,
                aeroportoEmbarqueTextField, dataDesembarqueInicioTextField, dataDesembarqueFimTextField,
                horarioDesembarqueInicioTextField, horarioDesembarqueFimTextField, aeroportoDesembarqueTextField));

        filtrosTipos = new ArrayList<>(Arrays.asList(
                "String", "String", "String",
                "Date", "Date", "Time", "Time",
                "String", "Date", "Date",
                "Time", "Time", "String"));

        ArrayList<?> voos;
        try {
            voos = vooRepository.getVoosFiltro(null, null, null,
                    LocalDateTime.of(LocalDateTime.now().getYear(), LocalDateTime.now().getMonth(), LocalDateTime.now().getDayOfMonth(), 0, 0, 0),
                    null, null, null, null, null, null);
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            return;
        }

        if (voos.isEmpty() || voos == null) {
            naoEncontrado();
            return;
        }

        for (int i=0; i<voos.size(); i++) {
            Voo voo = (Voo) voos.get(i);
            if (voo.getStatus() == "Finalizado") {
                voos.remove(i);
                i--;
            }
        }

        voosListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> arg0, HBox arg1, HBox arg2) {
                try {
                    verMais(Integer.valueOf(voosListView.getSelectionModel().getSelectedItem().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buildListView(voos);
    }

    private void naoEncontrado() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Text text = JavaFXElementBuilder.text("Nenhum voo encontrado", 20, FontWeight.BOLD, "#333333");
        hbox.getChildren().add(text);

        voosListView.getItems().add(hbox);
        return;
    }

    private void verMais(Integer id) throws IOException {
        VerVooControllerFXML.setIdVoo(id);
        App.setRoot("verVooPassageiro");
    }

    private void buildListView(ArrayList<?> voos) {
        for (int i = 0; i < voos.size(); i++) {
            Voo voo = (Voo) voos.get(i);
            String nomeCompanhia;
            try {
                Resultado result = aeronaveRepository.get(voo.getIdAeronave());
                if (result.foiErro()) {
                    System.out.println(result.comoErro().getMsg());
                    nomeCompanhia = null;
                }
                Aeronave aeronave = (Aeronave) result.comoSucesso().getObj();

                result = companhiaAereaRepository.get(aeronave.getIdCompanhiaAerea());
                if (result.foiErro()) {
                    System.out.println(result.comoErro().getMsg());
                    nomeCompanhia = null;
                }
                CompanhiaAerea companhiaAerea = (CompanhiaAerea) result.comoSucesso().getObj();
                nomeCompanhia = companhiaAerea.getNome();

            } catch (Exception e) {
                e.printStackTrace();
                nomeCompanhia = null;
            }

            HBox mainBox = new HBox();
            mainBox.setId(voo.getId().toString());
            mainBox.setSpacing(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setAlignment(Pos.CENTER);

            VBox numeroCompanhiaVBox = new VBox();
            numeroCompanhiaVBox.setSpacing(10);
            numeroCompanhiaVBox.setAlignment(Pos.CENTER);

            VBox origemDestinoVBox = new VBox();
            origemDestinoVBox.setSpacing(10);
            origemDestinoVBox.setAlignment(Pos.CENTER);

            VBox embarqueDesembarqueVBox = new VBox();
            embarqueDesembarqueVBox.setSpacing(10);
            embarqueDesembarqueVBox.setAlignment(Pos.CENTER);

            Text numeroText = JavaFXElementBuilder.text("Num.: " + voo.getNumero(), 20, FontWeight.BOLD, "#333333");
            Text companhiaText = null;
            if (nomeCompanhia != null)
                companhiaText = JavaFXElementBuilder.text(nomeCompanhia, 20, FontWeight.BOLD, "#333333");

            Text origemText = JavaFXElementBuilder.text("Origem: " + voo.getOrigem(), 20, FontWeight.NORMAL, "#333333");
            Text destinoText = JavaFXElementBuilder.text("Destino: " + voo.getDestino(), 20, FontWeight.NORMAL,
                    "#333333");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY HH:mm");
            Text embarqueText = JavaFXElementBuilder.text("Emb.: " + voo.getHorarioEmbarque().format(formatter), 20,
                    FontWeight.NORMAL, "#333333");
            Text desembarqueText = JavaFXElementBuilder.text("Des.: " + voo.getHorarioDesembarque().format(formatter),
                    20, FontWeight.NORMAL, "#333333");

            Text statusText = JavaFXElementBuilder.text(voo.getStatus(), 20, FontWeight.BOLD, "#333333");

            numeroCompanhiaVBox.getChildren().add(numeroText);
            if (companhiaText != null)
                numeroCompanhiaVBox.getChildren().add(companhiaText);
            origemDestinoVBox.getChildren().addAll(origemText, destinoText);
            embarqueDesembarqueVBox.getChildren().addAll(embarqueText, desembarqueText);

            mainBox.getChildren().addAll(numeroCompanhiaVBox, origemDestinoVBox, embarqueDesembarqueVBox, statusText);
            voosListView.getItems().add(mainBox);
        }
    }

    private ArrayList<?> getFiltros() {
        String numero = !numeroTextField.getText().trim().isEmpty() ? numeroTextField.getText().trim() : null;
        String origem = !origemTextField.getText().trim().isEmpty() ? origemTextField.getText().trim() : null;
        String destino = !destinoTextField.getText().trim().isEmpty() ? destinoTextField.getText().trim() : null;
        String dataEmbarqueInicio = !dataEmbarqueInicioTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataEmbarqueInicioTextField.getText())
                : null;
        String dataEmbarqueFim = !dataEmbarqueFimTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataEmbarqueFimTextField.getText())
                : null;
        String horarioEmbarqueInicio = !horarioEmbarqueInicioTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(horarioEmbarqueInicioTextField.getText())
                : null;
        String horarioEmbarqueFim = !horarioEmbarqueFimTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(horarioEmbarqueFimTextField.getText())
                : null;
        String aeroportoEmbarque = !aeroportoEmbarqueTextField.getText().trim().isEmpty()
                ? aeroportoEmbarqueTextField.getText().trim()
                : null;
        String dataDesembarqueInicio = !dataDesembarqueInicioTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataDesembarqueInicioTextField.getText())
                : null;
        String dataDesembarqueFim = !dataDesembarqueFimTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataDesembarqueFimTextField.getText())
                : null;
        String horarioDesembarqueInicio = !horarioDesembarqueInicioTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(horarioDesembarqueInicioTextField.getText())
                : null;
        String horarioDesembarqueFim = !horarioDesembarqueFimTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(horarioDesembarqueFimTextField.getText())
                : null;
        String aeroportoDesembarque = !aeroportoDesembarqueTextField.getText().trim().isEmpty()
                ? aeroportoDesembarqueTextField.getText().trim()
                : null;

        if (dataEmbarqueInicio != null && horarioEmbarqueInicio == null) {
            horarioEmbarqueInicio = "0000";
        } else if (dataEmbarqueInicio == null && horarioEmbarqueInicio != null) {
            dataEmbarqueInicio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        }

        if (dataEmbarqueFim != null && horarioEmbarqueFim == null) {
            horarioEmbarqueFim = "0000";
        } else if (dataEmbarqueFim == null && horarioEmbarqueFim != null) {
            dataEmbarqueFim = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        }

        if (dataDesembarqueInicio != null && horarioDesembarqueInicio == null) {
            horarioDesembarqueInicio = "0000";
        } else if (dataDesembarqueInicio == null && horarioDesembarqueInicio != null) {
            dataDesembarqueInicio = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        }

        if (dataDesembarqueFim != null && horarioDesembarqueFim == null) {
            horarioDesembarqueFim = "0000";
        } else if (dataDesembarqueFim == null && horarioDesembarqueFim != null) {
            dataDesembarqueFim = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyy"));
        }

        LocalDateTime embarqueInicio;
        LocalDateTime embarqueFim;
        LocalDateTime desembarqueInicio;
        LocalDateTime desembarqueFim;
        if (horarioEmbarqueInicio != null && horarioEmbarqueInicio.length() == 3) horarioEmbarqueInicio = "0" + horarioEmbarqueInicio;
        try {
            embarqueInicio = dataEmbarqueInicio != null
            ? LocalDateTime.parse(dataEmbarqueInicio + horarioEmbarqueInicio, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
            : null;
        } catch(Exception e) {
            e.printStackTrace();
            embarqueInicio = null;
        }

        if (horarioEmbarqueFim != null && horarioEmbarqueFim.length() == 3) horarioEmbarqueFim = "0" + horarioEmbarqueFim;
        try {
            embarqueFim = dataEmbarqueFim != null
            ? LocalDateTime.parse(dataEmbarqueFim + horarioEmbarqueFim, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
            : null;
        } catch(Exception e) {
            e.printStackTrace();
            embarqueFim = null;
        }

        if (horarioDesembarqueInicio != null && horarioDesembarqueInicio.length() == 3) horarioDesembarqueInicio = "0" + horarioDesembarqueInicio;
        try {
            desembarqueInicio = dataDesembarqueInicio != null
            ? LocalDateTime.parse(dataDesembarqueInicio + horarioDesembarqueInicio, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
            : null;
        } catch(Exception e) {
            e.printStackTrace();
            desembarqueInicio = null;
        }

        if (horarioDesembarqueFim != null && horarioDesembarqueFim.length() == 3) horarioDesembarqueFim = "0" + horarioDesembarqueFim;
        try {
            desembarqueFim = dataDesembarqueFim != null
            ? LocalDateTime.parse(dataDesembarqueFim + horarioDesembarqueFim, DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
            : null;
        } catch(Exception e) {
            e.printStackTrace();
            desembarqueFim = null;
        }

        return new ArrayList<>(Arrays.asList(numero, origem, destino, embarqueInicio, embarqueFim, aeroportoEmbarque, desembarqueInicio, desembarqueFim, aeroportoDesembarque, null));
    }
}
