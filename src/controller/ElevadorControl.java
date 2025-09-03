package controller;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.layout.VBox;
import model.Elevador;

import model.Pessoa;


import java.util.*;
import java.util.function.Consumer;

public class ElevadorControl {
    private String statusPassado;

    public Elevador adicionarPassageiro(Pessoa p, Elevador e) {
        int c = e.getCapacidade();

        if (c < 10) {
            e.getPassageiros().add(p);
            c++;
            e.setCapacidade(c);
            return e;
        } else throw new IllegalStateException("Capacidade Máxima atingida!");
    }

    public Elevador removerPassageiro(Elevador e, String n) {
        if (e.getCapacidade() == 0){
            throw new NoSuchElementException("Não há ninguémno elevador para ser retirado.");
        }
        Pessoa pessoa = new Pessoa();
        int c = e.getCapacidade();

        pessoa.setNome(n);
        e.getPassageiros().remove(pessoa);
        c--;
        e.setCapacidade(c);
        return e;
    }

    // O status parado só ocorre quando a porta é aberta, a mudança para subindo ou
    // descendo ocorre quando a porta se fecha;

    public Elevador montarRota(int i, Elevador e) {
        String status = e.getStatus();
        List<Integer> rota = e.getRota();
        int andarAtual = e.getAndar();

        if (rota.contains(i)) throw new IllegalArgumentException("Andar já selecionado");

        boolean elevadorParado = status.equals("Parado");
        boolean rotaVazia = rota.isEmpty();
        boolean emExtremos = andarAtual == 0 || andarAtual == 10;

        if (elevadorParado && rotaVazia || emExtremos) {//Parado sem ter iniciado uma rota
            if (!e.isPortas() && andarAtual != i) {
                if (i > e.getAndar()) {
                    statusPassado = "Subindo";
                    e.setStatus("Subindo");
                    e.setRota(organizarRota(i, e, true));
                } else if (i < e.getAndar()) {
                    statusPassado = "Descendo";
                    e.setStatus("Descendo");
                    e.setRota(organizarRota(i, e, false));
                }
                return e;
            } else throw new IllegalArgumentException("O andar " + i + " já é o andar atual, ou as portas estão fechadas.");
        }

        if (status.equals("Subindo") || (statusPassado.equals("Subindo") && !rotaVazia)) { //Elevador Subindo
            if (i > andarAtual) {
                if (!e.isPortas()) {
                    e.setRota(organizarRota(i, e, true));
                    return e;
                } else throw new IllegalStateException("O Elevador não pode receber novos andares no momento, tente novamente com as portas abertas.");
            }
        }

        if (status.equals("Descendo") || (!rotaVazia && statusPassado.equals("Descendo"))) { //Elevador Descendo
            if (i < andarAtual) {
                if (!e.isPortas()) {
                    e.setRota(organizarRota(i, e, false));
                    return e;
                } else throw new IllegalStateException("O Elevador não pode receber novos andares no momento, tente novamente com as portas abertas.");
            }
        }
        throw new IllegalArgumentException("Andar não acessível no momento");
    }


    public List<Integer> organizarRota(int i, Elevador e, Boolean subindo) {
        List<Integer> rota = new ArrayList<>(e.getRota());
        int index = 0;

        while (index < rota.size()) {
            int atual = rota.get(index);
            if ((subindo && i < atual) || (!subindo && i > atual)) {
                break; // achou onde inserir o novo andar
            }
            index++;
        }

        rota.add(index, i); // insere no ponto correto
        return rota;
    }

    public Task<Void> mover(Elevador elevador, Consumer<Integer> atualizarAndar, VBox rota) {
        return new Task<>() {
            @Override
            protected Void call() throws Exception {
                int proximo = elevador.getRota().get(0); // pega o primeiro andar da rota
                int atual = elevador.getAndar();

                    while (atual != proximo) {
                        Thread.sleep(2000); //1 segundo para andar por cada andar
                        atual += (proximo > atual) ? 1 : -1;
                        elevador.setAndar(atual);

                        int finalAtual = atual;

                        // Atualiza a interface com o novo andar
                        Platform.runLater(() -> atualizarAndar.accept(finalAtual));

                    }
                Platform.runLater(() ->
                    rota.setVisible(true));
                    elevador.getRota().remove(0);
                System.out.println(elevador);
                    return null;
            }

        };
    }

    public Elevador saidaPassageiros(Elevador e) {
       List<Pessoa> p = e.getPassageiros();
       Random random = new Random();
        int index1 = random.nextInt(p.size());
        Collections.shuffle(p);

        p.subList(0, index1 + 1).clear();
        e.setPassageiros(p);
        e.setCapacidade(p.size());
        return e;
    }

    public String getStatusPassado(){
        return statusPassado;
    }
}