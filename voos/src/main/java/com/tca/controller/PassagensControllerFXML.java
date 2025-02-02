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
import com.tca.model.MetodoPagamento;
import com.tca.model.Passagem;
import com.tca.model.Voo;
import com.tca.repository.MetodoPagamentoRepository;
import com.tca.repository.PassagemRepository;
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
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class PassagensControllerFXML implements Initializable {
    private static Integer idVoo;

    private PassagemRepository passagemRepository = PassagemRepository.getInstance();
    private VooRepository vooRepository = VooRepository.getInstance();
    private MetodoPagamentoRepository metodoPagamentoRepository = MetodoPagamentoRepository.getInstance();
    private ArrayList<?> metodos;

    @FXML
    private TextField dataFimTextField;

    @FXML
    private Pane filtrosPane;

    @FXML
    private TextField dataInicioTextField;

    @FXML
    private ListView<HBox> passagensListView;

    @FXML
    private ChoiceBox<String> metodoPagamentoChoiceBox;

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
    void sair(ActionEvent event) throws IOException {
        App.deslogar();
        App.setRoot("escolherLogin");
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
    void limparFiltros(ActionEvent event) {
        warningText.setText("");
        for (int i = 0; i < filtros.size(); i++) {
            filtros.get(i).setText("");
        }
        metodoPagamentoChoiceBox.getSelectionModel().clearSelection();
    }

    @FXML
    void confirmarFiltros(ActionEvent event) {
        warningText.setText("");
        if (validarFiltros() == false) {
            warningText.setText("Algum filtro é inválido");
            return;
        }

        passagensListView.getItems().clear();

        ArrayList<?> passagens;
        ArrayList<?> params = getFiltros();

        try {
            passagens = passagemRepository.getPassagensFiltro(
                    (Integer) params.get(0),
                    (String) params.get(1),
                    (String) params.get(2),
                    (String) params.get(3),
                    (String) params.get(4),
                    (Integer) params.get(5),
                    (LocalDateTime) params.get(6),
                    (LocalDateTime) params.get(7));
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        if (passagens.isEmpty() || passagens == null) {
            naoEncontrado();
            filtrosPane.setVisible(false);
            return;
        }

        passagensListView.getItems().clear();
        buildListView(passagens);
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
        try {
            metodos = metodoPagamentoRepository.listar();
            if (metodos == null || metodos.isEmpty() || !(metodos.stream().allMatch(element -> element instanceof MetodoPagamento))) {
                warningText.setText("Algo deu errado, tente novamente");
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        for (int i=0; i<metodos.size(); i++) {
            MetodoPagamento metodo = (MetodoPagamento) metodos.get(i);
            metodoPagamentoChoiceBox.getItems().add(metodo.getMetodo());
        }

        filtros = new ArrayList<>(Arrays.asList(cpfTextField, dataInicioTextField, dataFimTextField));

        filtrosTipos = new ArrayList<>(Arrays.asList("String", "String", "String", "Cpf", "Date", "Date"));

        ArrayList<?> passagens;
        try {
            passagens = passagemRepository.listar();
        } catch (Exception e) {
            e.printStackTrace();
            naoEncontrado();
            return;
        }

        for (int i=0; i<passagens.size(); i++) {
            Passagem passagem = (Passagem) passagens.get(i);
            if (passagem.getIdVoo() != idVoo) {
                passagens.remove(i);
                i--;
            }
        }

        if (passagens.isEmpty() || passagens == null) {
            naoEncontrado();
            return;
        }

        passagensListView.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<HBox>() {
            @Override
            public void changed(ObservableValue<? extends HBox> arg0, HBox arg1, HBox arg2) {
                try {
                    verMais(Integer.valueOf(passagensListView.getSelectionModel().getSelectedItem().getId()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        buildListView(passagens);
    }

    private void naoEncontrado() {
        HBox hbox = new HBox();
        hbox.setSpacing(10);
        hbox.setPadding(new Insets(10));
        hbox.setAlignment(Pos.CENTER);

        Text text = JavaFXElementBuilder.text("Nenhuma passagem encontrada", 20, FontWeight.BOLD, "#333333");
        hbox.getChildren().add(text);

        passagensListView.getItems().add(hbox);
        return;
    }

    private void verMais(Integer id) throws IOException {
        VerPassagemControllerFXML.setIdPassagem(id);
        App.setRoot("verPassagem");
    }

    private void buildListView(ArrayList<?> passagens) {
        for (int i = 0; i < passagens.size(); i++) {
            Passagem passagem = (Passagem) passagens.get(i);
            Voo voo;
            try {
                Resultado result = vooRepository.get(passagem.getIdVoo());
                if (result.foiErro()) {
                    System.out.println(result.comoErro().getMsg());
                    voo = null;
                }
                voo = (Voo) result.comoSucesso().getObj();

            } catch (Exception e) {
                e.printStackTrace();
                voo = null;
            }

            HBox mainBox = new HBox();
            mainBox.setId(passagem.getId().toString());
            mainBox.setSpacing(15);
            mainBox.setPadding(new Insets(20));
            mainBox.setAlignment(Pos.CENTER);

            VBox cpfDataVBox = new VBox();
            cpfDataVBox.setSpacing(10);
            cpfDataVBox.setAlignment(Pos.CENTER);

            VBox origemDestinoVBox = new VBox();
            origemDestinoVBox.setSpacing(10);
            origemDestinoVBox.setAlignment(Pos.CENTER);

            Text numeroText = null;
            if (voo != null) numeroText = JavaFXElementBuilder.text(voo.getNumero(), 20, FontWeight.BOLD, "#333333");

            Text cpfText = JavaFXElementBuilder.text("CPF: " + passagem.getCpfPassageiro().replaceAll("(\\d{3})(\\d{3})(\\d{3})(\\d{2})", "$1.$2.$3-$4"), 20, FontWeight.NORMAL, "#333333");

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/YY HH:mm");
            Text dataText = JavaFXElementBuilder.text("Compra: " + passagem.getDataCompra().format(formatter), 20, FontWeight.NORMAL, "#333333");

            Text origemText = null;
            Text destinoText = null;
            if (voo != null) origemText = JavaFXElementBuilder.text("Origem: " + voo.getOrigem(), 20, FontWeight.NORMAL, "#333333");
            if (voo != null) destinoText = JavaFXElementBuilder.text("Destino: " + voo.getDestino(), 20, FontWeight.NORMAL, "#333333");

            Text checkInText = null;
            if (passagem.getIdCheckIn() > 0) {
                checkInText = JavaFXElementBuilder.text("Check-in realizado", 20, FontWeight.BOLD, "#333333");
            } else if (LocalDateTime.now().isAfter(voo.getHorarioEmbarque().minusHours(24))) {
                checkInText = JavaFXElementBuilder.text("Check-in disponível", 20, FontWeight.BOLD, "#333333");
            } else {
                checkInText = JavaFXElementBuilder.text("Check-in indisponível", 20, FontWeight.BOLD, "#333333");
            }

            cpfDataVBox.getChildren().addAll(cpfText, dataText);
            if (voo != null) origemDestinoVBox.getChildren().addAll(origemText, destinoText);

            mainBox.getChildren().addAll(numeroText, cpfDataVBox, origemDestinoVBox, checkInText);
            passagensListView.getItems().add(mainBox);
        }
    }

    private ArrayList<?> getFiltros() {
        String cpf = !StringFormatter.formatNumericData(cpfTextField.getText()).isEmpty() ? StringFormatter.formatNumericData(cpfTextField.getText()) : null;
        String dataInicioText = !dataInicioTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataInicioTextField.getText())
                : null;
        String dataFimText = !dataFimTextField.getText().trim().isEmpty()
                ? StringFormatter.formatNumericData(dataFimTextField.getText())
                : null;

        LocalDateTime dataInicio;
        LocalDateTime dataFim;
        try {
            dataInicio = dataInicioText != null
                ? LocalDateTime.parse(dataInicioText + "0000", DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
                : null;
        } catch(Exception e) {
            e.printStackTrace();
            dataInicio = null;
        }
        try {
            dataFim = dataFimText != null
                ? LocalDateTime.parse(dataFimText + "0000", DateTimeFormatter.ofPattern("ddMMyyyyHHmm"))
                : null;
        } catch(Exception e) {
            e.printStackTrace();
            dataFim = null;
        }

        Integer idMetodoPagamento = null;
        if (metodoPagamentoChoiceBox.getSelectionModel().getSelectedIndex() >= 0) {
            Integer indexMetodoPagamento = metodoPagamentoChoiceBox.getSelectionModel().getSelectedIndex();
            MetodoPagamento metodoPagamento = (MetodoPagamento) metodos.get(indexMetodoPagamento);
            idMetodoPagamento = metodoPagamento.getId();
        }

        return new ArrayList<>(Arrays.asList(null, null, null, cpf, idMetodoPagamento, dataInicio, dataFim));
    }

    public static void setIdVoo(int id) {
        idVoo = id;
    }

}
