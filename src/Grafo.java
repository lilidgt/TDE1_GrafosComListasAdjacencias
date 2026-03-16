public class Grafo {
    private Aresta[] lista;
    public String vertices[];
    public int tamanho;

    public Grafo(int tamanho) {
        this.tamanho = tamanho;
        this.lista = new Aresta[tamanho];
        this.vertices = new String[tamanho];
    }

    public void cria_adjacencia(int i, int j, double P) {
        if (i>= 0 && i<tamanho && j>= 0 && j <tamanho) {
            Aresta nova = new Aresta(j, P);
            nova.proximo = lista[i];
            lista[i] = nova;
        }
    }

    public void remove_adjacencia(int i, int j) {
        if (i>= 0 && i<tamanho && lista[i] != null) {
            if (lista[i].destino == j) {
                lista[i] = lista[i].proximo;
            } else {
                Aresta atual = lista[i];
                while (atual.proximo != null && atual.proximo.destino != j) {
                    atual = atual.proximo;
                }
                if (atual.proximo != null) {
                    atual.proximo = atual.proximo.proximo;
                }
            }
        }
    }

    public void imprime_adjacencias() {
        for (int i = 0; i < tamanho; i++) {
            System.out.println(i + "->");

            Aresta atual = lista[i];

            while (atual.proximo != null) {
                System.out.println(atual.proximo);
                atual = atual.proximo;
            }
            System.out.println();
        }
    }
}