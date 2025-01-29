package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Aeronave;
import com.tca.model.Aeroporto;
import com.tca.model.CompanhiaAerea;
import com.tca.model.PortaoEmbarque;
import com.tca.model.Voo;
import com.tca.repository.AeronaveRepository;
import com.tca.repository.AeroportoRepository;
import com.tca.repository.CompanhiaAereaRepository;
import com.tca.repository.PortaoEmbarqueRepository;
import com.tca.repository.VooRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class VerVooControllerFXML implements Initializable {
    private static int idVoo;

    private VooRepository vooRepository = VooRepository.getInstance();
    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();
    private CompanhiaAereaRepository companhiaAereaRepository = CompanhiaAereaRepository.getInstance();
    private PortaoEmbarqueRepository portaoEmbarqueRepository = PortaoEmbarqueRepository.getInstance();
    private AeroportoRepository aeroportoRepository = AeroportoRepository.getInstance();

    @FXML
    private Text horarioDesembarqueText;

    @FXML
    private Text aeroportoEmbarqueText;

    @FXML
    private Text horarioEmbarqueText;

    @FXML
    private Text companhiaText;

    @FXML
    private Text aeroportoDesembarqueText;

    @FXML
    private Text statusText;

    @FXML
    private Text origemText;

    @FXML
    private Text destinoText;

    @FXML
    private Text portaoText;

    @FXML
    private Text numeroText;

    @FXML
    private Text warningText;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Resultado resultVoo = vooRepository.get(idVoo);
            if (resultVoo.foiErro()) {
                System.out.println(resultVoo.comoErro().getMsg());
                return;
            }
            Voo voo = (Voo) resultVoo.comoSucesso().getObj();

            Resultado resultAeronave = aeronaveRepository.get(voo.getIdAeronave());
            if (resultAeronave.foiErro()) {
                System.out.println(resultAeronave.comoErro().getMsg());
                return;
            }
            Aeronave aeronave = (Aeronave) resultAeronave.comoSucesso().getObj();

            Resultado resultPortao = portaoEmbarqueRepository.get(voo.getIdPortaoEmbarque());
            if (resultPortao.foiErro()) {
                System.out.println(resultPortao.comoErro().getMsg());
                return;
            }
            PortaoEmbarque portao = (PortaoEmbarque) resultPortao.comoSucesso().getObj();

            Resultado resultAeroportoChegada = aeroportoRepository.get(voo.getIdAeroportoChegada());
            if (resultAeroportoChegada.foiErro()) {
                System.out.println(resultAeroportoChegada.comoErro().getMsg());
                return;
            }
            Aeroporto aeroportoChegada = (Aeroporto) resultAeroportoChegada.comoSucesso().getObj();

            Resultado resultCompanhia = companhiaAereaRepository.get(aeronave.getIdCompanhiaAerea());
            if (resultCompanhia.foiErro()) {
                System.out.println(resultCompanhia.comoErro().getMsg());
                return;
            }
            CompanhiaAerea companhia = (CompanhiaAerea) resultCompanhia.comoSucesso().getObj();

            Resultado resultAeroportoEmbarque = aeroportoRepository.get(portao.getIdAeroporto());
            if (resultAeroportoEmbarque.foiErro()) {
                System.out.println(resultAeroportoEmbarque.comoErro().getMsg());
                return;
            }
            Aeroporto aeroportoEmbarque = (Aeroporto) resultAeroportoEmbarque.comoSucesso().getObj();

            numeroText.setText("Número do Voo: " + voo.getNumero());
            companhiaText.setText("Companhia aérea: " + companhia.getNome());
            horarioEmbarqueText.setText("Horário de embarque: " + voo.getHorarioEmbarque().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            horarioDesembarqueText.setText("Horário de desembarque: " + voo.getHorarioDesembarque().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            aeroportoEmbarqueText.setText("Aeroporto de embarque: " + aeroportoEmbarque.getNome());
            aeroportoDesembarqueText.setText("Aeroporto de desembarque: " + aeroportoChegada.getNome());
            statusText.setText("Status: " + voo.getStatus());
            origemText.setText("Origem: " + voo.getOrigem());
            destinoText.setText("Destino: " + voo.getDestino());
            portaoText.setText("Portão de embarque: " + portao.getCodigo());
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void voltar(ActionEvent event) throws IOException {
        App.setRoot("voosPassageiro");
    }

    @FXML
    void comprarPassagem(ActionEvent event) throws IOException {
        Voo voo;
        try {
            Resultado result = vooRepository.verificarVooLotado(idVoo);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            } else if ((Boolean) result.comoSucesso().getObj() == true) {
                warningText.setText("O voo está lotado, não é possível comprar a passagem");
                return;
            }
            result = vooRepository.get(idVoo);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            voo = (Voo) result.comoSucesso().getObj();
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        ComprarPassagemControllerFXML.setVoo(voo);
        App.setRoot("comprarPassagem");
    }

    public static void setIdVoo(int id) {
        idVoo = id;
    }

}
