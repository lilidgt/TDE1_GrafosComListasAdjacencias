public class Aresta {
    public int destino;
    public double peso;
    public Aresta proximo;

    public Aresta(int destino, double peso) {
        this.destino = destino;
        this.peso = peso;
        this.proximo = null;
    }
}