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

    public int adjacentes(int i, int[] adj) {
        int cont = 0;

        if (i >= 0 && i < tamanho) {
            Aresta atual = lista[i];

            while (atual != null) {
                adj[cont] = atual.destino;
                cont++;
                atual = atual.proximo;
            }
        }

        return cont;
    }

    public void imprime_adjacencias() {
        for (int i = 0; i < tamanho; i++) {
            System.out.print(i + " (" + vertices[i] + ") -> ");
            Aresta atual = lista[i];
            while (atual != null) {
                System.out.print("[destino: " + atual.destino + ", peso: " + atual.peso + "] ");
                atual = atual.proximo;
            }
            System.out.println();
        }
    }
    public void seta_informacao(int i, String V) {
        if (i >= 0 && i < tamanho) {
            vertices[i] = V;
        }
    }
}