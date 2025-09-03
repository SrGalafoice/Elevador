package model;


import java.util.ArrayList;
import java.util.List;

public class Elevador {
    private List<Integer> rota = new ArrayList<>();
    private List<Pessoa> passageiros = new ArrayList<>();// 11 andares de 0 (terreo) a 10
    private boolean portas; //true = fechadas, false = aberta
    private int andar;
    private int capacidade;
    private String status; //parado, subindo ou descendo

    public int getCapacidade() {
        return capacidade;
    }

    public void setCapacidade(int capacidade) {
        this.capacidade = capacidade;
    }

    public List<Pessoa> getPassageiros() {
        return passageiros;
    }

    public void setPassageiros(List<Pessoa> passageiros) {
        this.passageiros = passageiros;
    }

    public List<Integer> getRota() {
        return rota;
    }

    public void setRota(List<Integer> rota) {
        if (rota.isEmpty()){
            throw new IllegalArgumentException("Para uma rota ser iniciada, é necessário selecionar pelo menos 1 andar.");
        } else this.rota = rota;
    }

    public String getStatus() {
       return status == null ? "Parado" : status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getAndar() {
        return andar;
    }

    public void setAndar(int andar) {
        this.andar = andar;
    }

    public boolean isPortas() {
        return portas;
    }

    public String getStatusPortas() {
        return portas ? "Fechada" : "Aberta";
    }

    public void setPortas(boolean portas) {
        this.portas = portas;
    }

    @Override
    public String toString() {
        return "Elevador{" +
                "rota=" + rota +
                '}';
    }

}
