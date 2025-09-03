package view;

import controller.ElevadorControl;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import model.Elevador;
import model.Pessoa;

import java.util.*;

public class ElevadorView extends Application {
    private static List<String> nomes;
    private static List<String> frases = new ArrayList<>();
    private static List<String> frasesErro = new ArrayList<>();
    private static List<String> frasesSurrender = new ArrayList<>();
    private static List<String> frasesSucesso = new ArrayList<>();
    private Elevador elevador = new Elevador();
    private final Label statusElevador = new Label("Status do elevador: " + elevador.getStatus());
    private final Label andarAtual = new Label("Andar atual: " + elevador.getAndar());
    private final Label porta = new Label("Porta: " + elevador.getStatusPortas());
    private final Label contagemPassageiros = new Label("Contagem de passageiros: " + elevador.getPassageiros().size());
    private final ElevadorControl control = new ElevadorControl();
    private final Random random = new Random();
    private final Button btnAdicionar = new Button("Adicionar passageiro");
    private final Button btnAdicionar2 = new Button("Adicionar passageiro");
    private final Button btnRemover = new Button("Remover passageiro");
    private final Button btnIniciar = new Button("Iniciar Rota");
    private final Button btnContinuar = new Button("Continuar a rota");
    private final Button btnSaida = new Button("Saida de Passageiros");
    private final VBox boxRota = new VBox(10, btnContinuar, btnSaida, btnAdicionar);
    @Override
    public void start(Stage stage) {

        // Layout superior (informações do elevador)
        VBox infoBox = new VBox(5, andarAtual, statusElevador, porta);
        infoBox.setAlignment(Pos.TOP_LEFT);

        // Grid de botões (teclado numérico do elevador)
        GridPane teclado = getGridPane();

        // Colocar contador de passageiros à direita do teclado
        VBox contadorBox = new VBox(10, contagemPassageiros);
        contadorBox.setAlignment(Pos.CENTER_LEFT);
        HBox tecladoComContador = new HBox(20, teclado, contadorBox);
        tecladoComContador.setAlignment(Pos.CENTER);

        // Botões inferiores

        btnAdicionar.setPrefWidth(160);
        btnAdicionar2.setPrefWidth(160);
        btnRemover.setPrefWidth(160);
        btnIniciar.setPrefWidth(160);
        btnSaida.setPrefWidth(160);
        btnContinuar.setPrefWidth(160);


        boxRota.setAlignment(Pos.CENTER);

        VBox boxPassageiros = new VBox(10, btnAdicionar2, btnRemover);
        boxPassageiros.setAlignment(Pos.CENTER);

        HBox botoesInferiores = new HBox(30, boxPassageiros, btnIniciar);
        botoesInferiores.setAlignment(Pos.CENTER);

        btnAdicionar.setOnAction(e -> adicionarPassageiros());
        btnAdicionar2.setOnAction(e ->
                    adicionarPassageiros());
        btnRemover.setOnAction(e ->{
            if (elevador.getCapacidade() == 0){
                alert("Não há ninguém para sair do elevador");
                return;
            }
            String n = elevador.getPassageiros().get(random.nextInt(elevador.getPassageiros().size())).getNome(); //Seleciona um nome de um passageiro
            elevador = control.removerPassageiro(elevador, n); //Lógica para remover o passageiro selecionado
            alert(n + ": " + frasesSurrender.get(random.nextInt(frasesSurrender.size())));

            //atualiza a label
            contagemPassageiros.setText("Contagem de passageiros: " + elevador.getPassageiros().size());
        });
        btnIniciar.setOnAction(e -> {
            try {
                if (elevador.getRota().isEmpty()) {
                    alert("Rota vazia, selecione, pelo menos, um andar antes.");
                    return;
                }
                elevador.setPortas(true);
                statusElevador.setText("Status do elevador: " + elevador.getStatus());
                porta.setText("Porta: " + elevador.getStatusPortas());

                Task<Void> tarefa = control.mover(
                        elevador,
                        novoAndar -> andarAtual.setText("Andar atual: " + novoAndar), boxRota
                );

                botoesInferiores.getChildren().clear();
                botoesInferiores.getChildren().addAll(boxRota);
                boxRota.setVisible(false);
                btnAdicionar.setVisible(false);
                new Thread(tarefa).start();
            }catch (Exception ex){
                alert("Erro: " + ex);
            }
            });


        //Rota iniciada
        btnContinuar.setOnAction(e ->{
            if (elevador.getRota().isEmpty()) {
               elevador.setStatus("Parado");
               elevador.setPortas(false);
               statusElevador.setText("Status do elevador: " + elevador.getStatus());
               porta.setText("Portas: " + elevador.getStatusPortas());
               botoesInferiores.getChildren().clear();
                botoesInferiores.getChildren().addAll(boxPassageiros,btnIniciar);
                return;
            }
            elevador.setPortas(true);
            elevador.setStatus(control.getStatusPassado());
            statusElevador.setText("Status do elevador: " + elevador.getStatus());
            porta.setText("Porta: " + elevador.getStatusPortas());

            Task<Void> tarefa = control.mover(
                    elevador,
                    novoAndar -> andarAtual.setText("Andar atual: " + novoAndar), boxRota
            );
            btnAdicionar.setVisible(false);
            boxRota.setVisible(false);
            new Thread(tarefa).start();
        });

        btnSaida.setOnAction(e -> {
            if (elevador.getPassageiros().isEmpty()){
                alert("O Elevador está vazio. Mas ele continua mantendo seu trabalho de abrir as portas quando chegasse no andar");

                elevador.setPortas(false);
                elevador.setStatus("Parado");
                statusElevador.setText("Status do elevador: " + elevador.getStatus());
                porta.setText("Portas: " + elevador.getStatusPortas());

                verificaBotao();
                return;
            }
            elevador.setStatus("Parado");
            statusElevador.setText("Status do elevador: " + elevador.getStatus());
            elevador.setPortas(false);
            porta.setText("Portas: " + elevador.getStatusPortas());
            List<Pessoa> sairam = new ArrayList<>(elevador.getPassageiros());

            elevador = control.saidaPassageiros(elevador);
            sairam.removeAll(elevador.getPassageiros());
            StringBuilder falas = new StringBuilder();
            for (Pessoa p : sairam){
                falas.append("\n").append(p.getNome()).append(": ").append(frasesSucesso.get(random.nextInt(frasesSucesso.size())));
            }
            alert("Alguns passageiros sairam..." + falas);
            contagemPassageiros.setText("Contagem de passageiros: " + elevador.getPassageiros().size());

          verificaBotao();
        });


        // Layout principal
        VBox root = new VBox(20, infoBox, tecladoComContador, botoesInferiores);
        root.setPadding(new Insets(20));
        root.setAlignment(Pos.TOP_CENTER);

        Scene scene = new Scene(root, 400, 450);
        stage.setTitle("Simulador de Elevador");
        stage.setScene(scene);
        stage.show();

    }

    private void adicionarPassageiros() {
        Random math = new Random();
        Pessoa p = new Pessoa();

        //Adiciona um nome aleatorio
        p.setNome(nomes.get(math.nextInt(nomes.size())));
        //manda pra control fazer a logica (se ta na capacidade ou nao)
        try{
            elevador = control.adicionarPassageiro(p, elevador);
            alert(p.getNome() + ": " + frases.get(math.nextInt(frases.size()))); //pega a pessoa e adiciona na lista do objeto elevador, retornando uma frase de confirmação
        } catch (Exception ex){
            alert("Erro: " + ex + "\n" + p.getNome() + ": " + frasesErro.get(math.nextInt(frasesErro.size()))); //(caso não esteja na capacidade, retornar com um alerta)
        }

        //atualiza a label
        contagemPassageiros.setText("Contagem de passageiros: " + elevador.getPassageiros().size());
    }

    private void verificaBotao() {
        if (!boxRota.getChildren().contains(btnAdicionar)) {
            boxRota.getChildren().add(btnAdicionar);
        }
        btnAdicionar.setVisible(true);
        btnAdicionar.setManaged(true);
        boxRota.setVisible(true);
    }

    private GridPane getGridPane() {
        GridPane teclado = new GridPane();
        teclado.setAlignment(Pos.CENTER);
        teclado.setHgap(10);
        teclado.setVgap(10);

        int numero = 1;
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                Button btn = new Button(String.valueOf(numero++));
                btn.setPrefSize(50, 50);
                String valor = btn.getText();


                btn.setOnAction(e -> {
                    try {
                        elevador = control.montarRota(Integer.parseInt(valor), elevador);
                    }catch (Exception ex){
                        alert("Erro: "+ ex);
                    }
                    System.out.println(elevador.toString());
                });


                teclado.add(btn, col, row);
            }
        }
        // Linha do 10 e 0
        Button btn10 = new Button("10");
        btn10.setOnAction(e -> {
            try{
            elevador = control.montarRota(Integer.parseInt(btn10.getText()), elevador);
            }catch (Exception ex){
                alert("Erro: "+ ex);
            }
            System.out.println(elevador.toString());
        });
        btn10.setPrefSize(50, 50);
        Button btn0 = new Button("0");
        btn0.setOnAction(e -> {
            try{
            elevador = control.montarRota(Integer.parseInt(btn0.getText()), elevador);
            }catch (Exception ex){
                alert("Erro: "+ ex);
            }
            System.out.println(elevador.toString());
        });
        btn0.setPrefSize(50, 50);
        teclado.add(btn10, 0, 3);
        teclado.add(btn0, 1, 3);
        return teclado;
    }

    public void alert(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
        alert.show();
    }

    public static void main(String[] args) {
        nomes = Arrays.asList("Jorge", "Pedro", "Paulo", "Caitlyn", "Camille", "Wyilson", "Luciano",
                "Evelyn", "Ben", "João", "Manoel da Silva", "Garfield", "Homer", "Bryan", "Beatriz", "Zoe", "Violeta",
                "Welington", "Sylas");
        frases = Arrays.asList( "Boa tarde, tudo bem?",
                "Pode segurar a porta, por favor?",
                "Obrigado(a)!",
                "Qual andar você vai?",
                "Pode apertar o 7 pra mim, por favor?",
                "Esse elevador vai até a garagem?",
                "Tá meio cheio hoje, né?",
                "Ainda bem que chegou rápido!",
                "Eu sempre me confundo com esse elevador.",
                "Nossa, o ar-condicionado tá forte aqui dentro.");
        frasesErro = Arrays.asList("Ah, claro, sempre cheio quando eu preciso...",
                "De novo não, já tô atrasado!",
                "Impossível caber mais alguém aí dentro.",
                "Parece que o prédio inteiro resolveu usar o elevador agora.",
                "Vou ter que esperar o próximo, que saco...",
                "Nem adianta tentar, já não cabe nem um alfinete.",
                "Toda vez a mesma novela.",
                "Se eu soubesse, tinha ido de escada.",
                "Mais um minuto perdido por causa desse elevador lotado.",
                "Um dia ainda vou ficar preso aqui embaixo esperando...");
        frasesSurrender = Arrays.asList("Ah, quer saber? Vou de escada mesmo.",
                "Se eu fosse esperar mais, envelhecia aqui.",
                "Esse elevador deve estar de folga hoje.",
                "Já dava pra ter subido e descido duas vezes a pé.",
                "Desisto, minha paciência não é tão longa assim.",
                "Acho que o elevador foi dar uma volta no quarteirão.",
                "Tá mais fácil subir no ombro de alguém do que esperar isso.",
                "Tô saindo antes que eu crie raiz aqui.",
                "Nunca pensei que fosse preferir escada...",
                "Se aparecer agora, azar o dele!");
        frasesSucesso = Arrays.asList("É aqui que eu desço.",
                "Chegamos, obrigado!",
                "Com licença, é o meu andar.",
                "Finalmente, achei que nunca ia chegar!",
                "É agora, bom dia pra vocês!",
                "Valeu, gente, até mais.",
                "Essa é a minha parada.",
                "É aqui que eu fico.",
                "Cheguei no paraíso... ou quase.",
                "Tchau, elevador!");


        launch();
    }
}
