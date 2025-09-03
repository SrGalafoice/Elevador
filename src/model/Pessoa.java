package model;

public class Pessoa {
    private String nome;

    //Na hora de remover, vai comparar o nome e não o objeto
    //Pensei que faria sentido nesse caso, mas acabei confundindo
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pessoa)) return false;
        Pessoa outra = (Pessoa) obj;
        return this.nome != null && this.nome.equals(outra.nome);
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
