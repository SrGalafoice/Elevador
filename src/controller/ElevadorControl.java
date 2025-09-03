package controller;

import model.Elevador;

import java.util.Observable;

public class ElevadorControl {
    public int mover(Elevador e){
       if(e.isPortas()){
      return 1;
       } else return -1;
    }
    public void criarRota()

    public boolean verificaStatus(Elevador e){
        return e.isPortas();
    }
}
