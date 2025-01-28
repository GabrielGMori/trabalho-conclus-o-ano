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

    private VooRepository vooRepository = new VooRepository();
    private AeronaveRepository aeronaveRepository = new AeronaveRepository();
    private CompanhiaAereaRepository companhiaAereaRepository = new CompanhiaAereaRepository();
    private PortaoEmbarqueRepository portaoEmbarqueRepository = new PortaoEmbarqueRepository();
    private AeroportoRepository aeroportoRepository = new AeroportoRepository();

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
            Resultado resultPortao = portaoEmbarqueRepository.get(voo.getIdPortaoEmbarque());
            Resultado resultAeroportoChegada = aeroportoRepository.get(voo.getIdAeroportoChegada());

            if (resultAeronave.foiErro()) {
                System.out.println(resultAeronave.comoErro().getMsg());
                return;
            }

            if (resultPortao.foiErro()) {
                System.out.println(resultPortao.comoErro().getMsg());
                return;
            }

            if (resultAeroportoChegada.foiErro()) {
                System.out.println(resultAeroportoChegada.comoErro().getMsg());
                return;
            }
            Aeronave aeronave = (Aeronave) resultAeronave.comoSucesso().getObj();
            PortaoEmbarque portao = (PortaoEmbarque) resultPortao.comoSucesso().getObj();
            Aeroporto aeroportoChegada = (Aeroporto) resultAeroportoChegada.comoSucesso().getObj();

            Resultado resultCompanhia = companhiaAereaRepository.get(aeronave.getIdCompanhiaAerea());
            Resultado resultAeroportoEmbarque = aeroportoRepository.get(portao.getIdAeroporto());

            if (resultCompanhia.foiErro()) {
                System.out.println(resultCompanhia.comoErro().getMsg());
                return;
            }

            if (resultAeroportoEmbarque.foiErro()) {
                System.out.println(resultAeroportoEmbarque.comoErro().getMsg());
                return;
            }
            CompanhiaAerea companhia = (CompanhiaAerea) resultCompanhia.comoSucesso().getObj();
            Aeroporto aeroportoEmbarque = (Aeroporto) resultAeroportoEmbarque.comoSucesso().getObj();

            numeroText.setText("Número do Voo: " + voo.getNumero());
            companhiaText.setText("Companhia Aérea: " + companhia.getNome());
            horarioEmbarqueText.setText("Horário de Embarque: " + voo.getHorarioEmbarque().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            horarioDesembarqueText.setText("Horário de Desembarque: " + voo.getHorarioDesembarque().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            aeroportoEmbarqueText.setText("Aeroporto de Embarque: " + aeroportoEmbarque.getNome());
            aeroportoDesembarqueText.setText("Aeroporto de Desembarque: " + aeroportoChegada.getNome());
            statusText.setText("Status: " + voo.getStatus());
            origemText.setText("Origem: " + voo.getOrigem());
            destinoText.setText("Destino: " + voo.getDestino());
            portaoText.setText("Portão de Embarque: " + portao.getCodigo());
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
            Resultado result = vooRepository.get(idVoo);
            if (result.foiErro()) {
                System.out.println(result.comoErro().getMsg());
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
