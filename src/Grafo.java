public class Grafo {
    private Aresta[] lista; //cd posi aponta p uma lista encad
    public String vertices[]; //rotulo
    public int tamanho; //qnt vér

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

    public boolean[][] warshall() {
        boolean[][] alcancavel = new boolean[tamanho][tamanho];

        for (int i = 0; i < tamanho; i++) {
            alcancavel[i][i] = true;
            Aresta atual = lista[i];
            while (atual != null) {
                alcancavel[i][atual.destino] = true;
                atual = atual.proximo;
            }
        }

        for (int k = 0; k < tamanho; k++) {
            for (int i = 0; i < tamanho; i++) {
                for (int j = 0; j < tamanho; j++) {
                    if (alcancavel[i][k] && alcancavel[k][j]) {
                        alcancavel[i][j] = true;
                    }
                }
            }
        }

        return alcancavel;
    }

    public void imprime_warshall() {
        boolean[][] m = warshall();

        System.out.println("\n-- Matriz (Warshall) --");
        System.out.print("     ");
        for (int j = 0; j < tamanho; j++) {
            System.out.printf("%4s", vertices[j]);
        }
        System.out.println();

        for (int i = 0; i < tamanho; i++) {
            System.out.printf("%4s ", vertices[i]);
            for (int j = 0; j < tamanho; j++) {
                System.out.printf("%4s", m[i][j] ? "1" : "0");
            }
            System.out.println();
        }
    }

    public double[] dijkstra(int origem) {
        double[] dist = new double[tamanho];
        boolean[] visitado = new boolean[tamanho];
        int[] anterior = new int[tamanho];

        for (int i = 0; i < tamanho; i++) {
            dist[i] = Double.MAX_VALUE;
            visitado[i] = false;
            anterior[i] = -1;
        }
        dist[origem] = 0.0;

        for (int passo = 0; passo < tamanho; passo++) {
            int u = -1;
            for (int i = 0; i < tamanho; i++) {
                if (!visitado[i] && (u == -1 || dist[i] < dist[u])) {
                    u = i;
                }
            }

            if (u == -1 || dist[u] == Double.MAX_VALUE) break;
            visitado[u] = true;

            Aresta atual = lista[u];
            while (atual != null) {
                int v = atual.destino;
                double novaDist = dist[u] + atual.peso;
                if (!visitado[v] && novaDist < dist[v]) {
                    dist[v] = novaDist;
                    anterior[v] = u;
                }
                atual = atual.proximo;
            }
        }

        System.out.println("\n-- Dijkstra a partir de " + vertices[origem] + " --");
        for (int i = 0; i < tamanho; i++) {
            if (dist[i] == Double.MAX_VALUE) {
                System.out.println(vertices[origem] + " -> " + vertices[i] + ": inalcançável");
            } else {
                System.out.print(vertices[origem] + " -> " + vertices[i] + ": " + dist[i] + "  |  caminho: ");
                imprime_caminho(anterior, i, vertices);
                System.out.println();
            }
        }

        return dist;
    }

    private void imprime_caminho(int[] anterior, int v, String[] rotulos) {
        if (anterior[v] == -1) {
            System.out.print(rotulos[v]);
            return;
        }
        imprime_caminho(anterior, anterior[v], rotulos);
        System.out.print(" -> " + rotulos[v]);
    }
}