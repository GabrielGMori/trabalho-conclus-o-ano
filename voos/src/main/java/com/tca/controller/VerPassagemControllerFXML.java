package com.tca.controller;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.github.hugoperlin.results.Resultado;
import com.tca.App;
import com.tca.model.Aeronave;
import com.tca.model.Aeroporto;
import com.tca.model.CompanhiaAerea;
import com.tca.model.MetodoPagamento;
import com.tca.model.Passagem;
import com.tca.model.PortaoEmbarque;
import com.tca.model.Voo;
import com.tca.repository.AeronaveRepository;
import com.tca.repository.AeroportoRepository;
import com.tca.repository.CompanhiaAereaRepository;
import com.tca.repository.MetodoPagamentoRepository;
import com.tca.repository.PassagemRepository;
import com.tca.repository.PortaoEmbarqueRepository;
import com.tca.repository.VooRepository;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.text.Text;

public class VerPassagemControllerFXML implements Initializable {
    private static int idPassagem;

    private PassagemRepository passagemRepository = PassagemRepository.getInstance();
    private VooRepository vooRepository = VooRepository.getInstance();
    private AeronaveRepository aeronaveRepository = AeronaveRepository.getInstance();
    private CompanhiaAereaRepository companhiaAereaRepository = CompanhiaAereaRepository.getInstance();
    private PortaoEmbarqueRepository portaoEmbarqueRepository = PortaoEmbarqueRepository.getInstance();
    private AeroportoRepository aeroportoRepository = AeroportoRepository.getInstance();
    private MetodoPagamentoRepository metodoPagamentoRepository = MetodoPagamentoRepository.getInstance();

    private Boolean confirmacao = false;

    @FXML
    private Text horarioDesembarqueText;

    @FXML
    private Text dataCompraText;

    @FXML
    private Text aeroportoEmbarqueText;

    @FXML
    private Text companhiaText;

    @FXML
    private Text aeroportoDesembarqueText;

    @FXML
    private Text destinoText;

    @FXML
    private Text portaoText;

    @FXML
    private Text horarioEmbarqueText;

    @FXML
    private Text statusText;

    @FXML
    private Text origemText;

    @FXML
    private Text warningText;

    @FXML
    private Text numeroText;

    @FXML
    private Text metodoPagamentoText;

    @Override
    public void initialize(URL arg0, ResourceBundle arg1) {
        try {
            Resultado resultPassagem = passagemRepository.get(idPassagem);
            if (resultPassagem.foiErro()) {
                System.out.println(resultPassagem.comoErro().getMsg());
                return;
            }
            Passagem passagem = (Passagem) resultPassagem.comoSucesso().getObj();

            Resultado resultVoo = vooRepository.get(passagem.getIdVoo());
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

            Resultado resultMetodoPagamento = metodoPagamentoRepository.get(passagem.getIdMetodoPagamento());
            if (resultMetodoPagamento.foiErro()) {
                System.out.println(resultMetodoPagamento.comoErro().getMsg());
                return;
            }
            MetodoPagamento metodoPagamento = (MetodoPagamento) resultMetodoPagamento.comoSucesso().getObj();

            numeroText.setText("Passagem para o voo número " + voo.getNumero());
            companhiaText.setText("Companhia aérea: " + companhia.getNome());
            horarioEmbarqueText.setText("Horário de embarque: "
                    + voo.getHorarioEmbarque().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            horarioDesembarqueText.setText("Horário de desembarque: "
                    + voo.getHorarioDesembarque().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            aeroportoEmbarqueText.setText("Aeroporto de embarque: " + aeroportoEmbarque.getNome());
            aeroportoDesembarqueText.setText("Aeroporto de desembarque: " + aeroportoChegada.getNome());
            statusText.setText("Status: " + voo.getStatus());
            origemText.setText("Origem: " + voo.getOrigem());
            destinoText.setText("Destino: " + voo.getDestino());
            portaoText.setText("Portão de Embarque: " + portao.getCodigo());
            dataCompraText.setText("Data de compra: "
                    + passagem.getDataCompra().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")));
            metodoPagamentoText.setText("Método de pagamento: " + metodoPagamento.getMetodo());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    void voltar(ActionEvent event) throws IOException {
        if (App.getPassageiroLogado() != null) {
            App.setRoot("passagensCompradas");
        } else {
            App.setRoot("passagens");
        }
    }

    @FXML
    void realizarCheckIn(ActionEvent event) {
        Passagem passagem;
        Voo voo;
        try {
            Resultado result = passagemRepository.get(idPassagem);
            if (result.foiErro()) {
                warningText.setText("Erro: " + result.comoErro().getMsg());
                return;
            }
            passagem = (Passagem) result.comoSucesso().getObj();

            result = vooRepository.get(passagem.getIdVoo());
            if (result.foiErro()) {
                System.out.println(result.comoErro().getMsg());
                return;
            }
            voo = (Voo) result.comoSucesso().getObj();
        } catch (Exception e) {
            e.printStackTrace();
            warningText.setText("Algo deu errado, tente novamente");
            return;
        }

        if (passagem.getIdCheckIn() > 0) {
            warningText.setText("O check-in da passagem já foi realizado");
            return;
        } else if (LocalDateTime.now().isBefore(voo.getHorarioEmbarque().minusHours(24))) {
            warningText.setText("O check-in só pode ser realizado 24 horas antes do embarque do voo");
            return;
        } else if (confirmacao == false) {
            confirmacao = true;
            warningText.setText("Tem certeza?");
            return;
        }
        passagem.realizarCheckIn();
        warningText.setText("Check-in realizado");
    }

    public static void setIdPassagem(int id) {
        idPassagem = id;
    }

}
