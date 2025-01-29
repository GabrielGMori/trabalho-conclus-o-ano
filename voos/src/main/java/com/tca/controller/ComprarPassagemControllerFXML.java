package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.MetodoPagamento;
import com.tca.model.Passagem;
import com.tca.model.Voo;
import com.tca.repository.MetodoPagamentoRepository;
import com.tca.repository.PassagemRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

public class ComprarPassagemControllerFXML implements Initializable {
    private static Voo voo;

    private PassagemRepository passagemRepository = PassagemRepository.getInstance();
    private MetodoPagamentoRepository metodoPagamentoRepository = MetodoPagamentoRepository.getInstance();
    private ArrayList<?> metodos;

    @FXML
    private ChoiceBox<String> metodoPagamentoChoiceBox;

    @FXML
    private Text warningText;

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
    }

    @FXML
    void cancelarPagamento(ActionEvent event) throws IOException {
        App.setRoot("verVooPassageiro");
    }

    @FXML
    void confirmarPagamento(ActionEvent event) throws IOException {
        try {
            if (metodoPagamentoChoiceBox.getSelectionModel().getSelectedIndex() < 0) {
                warningText.setText("Por favor, selecione um método de pagamento");
                return;
            }
            MetodoPagamento metodo = (MetodoPagamento) metodos.get(metodoPagamentoChoiceBox.getSelectionModel().getSelectedIndex());
            Resultado result = passagemRepository.criar(new Passagem(LocalDateTime.now(), App.getPassageiroLogado().getCpf(), voo.getId(), metodo.getId()));
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        App.setRoot("voosPassageiro");
    }

    public static void setVoo(Voo novoVoo) {
        voo = novoVoo;
    }

}
