package model;

public class Elevador {
    private boolean portas; //true = fechadas, false = aberta
    private int andar;
    private String status; //parado, subindo ou descendo
    private int[] rota = new int[11]; // 11 andares de 0 (terreo) a 10

    public String getStatus() {
        return status;
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

    public void setPortas(boolean portas) {
        this.portas = portas;
    }

}
