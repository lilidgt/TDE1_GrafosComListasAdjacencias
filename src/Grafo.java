public class Grafo {
    private Aresta[] listaDeAdjacencias; // cd posi aponta p lista encadeada de arestas daquele vértice
    public String[] rotulosVertices;
    public int totalVertices; // q tem no grafo

    public Grafo(int totalVertices) {
        this.totalVertices = totalVertices;
        this.listaDeAdjacencias = new Aresta[totalVertices];
        this.rotulosVertices = new String[totalVertices];
    }

    // origem -> destino cm peso
    public void cria_adjacencia(int verticeOrigem, int verticeDestino, double peso) {
        boolean origemValida  = verticeOrigem  >= 0 && verticeOrigem  < totalVertices;
        boolean destinoValido = verticeDestino >= 0 && verticeDestino < totalVertices;

        if (origemValida && destinoValido) {
            Aresta novaAresta = new Aresta(verticeDestino, peso);
            novaAresta.proximaAresta = listaDeAdjacencias[verticeOrigem]; // insere no início da lista
            listaDeAdjacencias[verticeOrigem] = novaAresta;
        }
    }

    // origem -> destino
    public void remove_adjacencia(int verticeOrigem, int verticeDestino) {
        boolean origemValida      = verticeOrigem >= 0 && verticeOrigem < totalVertices;
        boolean listaNaoVazia     = listaDeAdjacencias[verticeOrigem] != null;

        if (origemValida && listaNaoVazia) {
            // primeira da lista
            if (listaDeAdjacencias[verticeOrigem].verticeDestino == verticeDestino) {
                listaDeAdjacencias[verticeOrigem] = listaDeAdjacencias[verticeOrigem].proximaAresta;
            } else {
                // percorre até achar aresta anterior
                Aresta arestaAtual = listaDeAdjacencias[verticeOrigem];
                while (arestaAtual.proximaAresta != null &&
                        arestaAtual.proximaAresta.verticeDestino != verticeDestino) {
                    arestaAtual = arestaAtual.proximaAresta;
                }
                // tira aresta encontrada da list
                if (arestaAtual.proximaAresta != null) {
                    arestaAtual.proximaAresta = arestaAtual.proximaAresta.proximaAresta;
                }
            }
        }
    }

    // retorna a quantidade de adjacentes encontrados
    public int adjacentes(int vertice, int[] vizinhos) {
        int quantidadeVizinhos = 0;

        if (vertice >= 0 && vertice < totalVertices) {
            Aresta arestaAtual = listaDeAdjacencias[vertice];
            while (arestaAtual != null) {
                vizinhos[quantidadeVizinhos] = arestaAtual.verticeDestino;
                quantidadeVizinhos++;
                arestaAtual = arestaAtual.proximaAresta;
            }
        }
        return quantidadeVizinhos;
    }

    public void imprime_adjacencias() {
        for (int vertice = 0; vertice < totalVertices; vertice++) {
            System.out.print(vertice + " (" + rotulosVertices[vertice] + ") -> ");
            Aresta arestaAtual = listaDeAdjacencias[vertice];
            while (arestaAtual != null) {
                // índice (rótulo) -> [destino, peso]
                System.out.print("[destino: " + arestaAtual.verticeDestino + ", peso: " + arestaAtual.peso + "] ");
                arestaAtual = arestaAtual.proximaAresta;
            }
            System.out.println();
        }
    }

    // dfn nome
    public void seta_informacao(int vertice, String rotulo) {
        if (vertice >= 0 && vertice < totalVertices) {
            rotulosVertices[vertice] = rotulo;
        }
    }

    // se true, tem caminho :p
    public boolean[][] warshall() {
        boolean[][] matrizAlcancavel = new boolean[totalVertices][totalVertices];

        // marca arestas diretas q ja tem no grafo
        for (int vertice = 0; vertice < totalVertices; vertice++) {
            matrizAlcancavel[vertice][vertice] = true; // o vértice alcança ele msm smpre né
            Aresta arestaAtual = listaDeAdjacencias[vertice];
            while (arestaAtual != null) {
                matrizAlcancavel[vertice][arestaAtual.verticeDestino] = true;
                arestaAtual = arestaAtual.proximaAresta;
            }
        }

        // p cada vértice intermediário possível,
        // verifica se cria novos caminhos entre outros pares de vértices
        for (int intermediario = 0; intermediario < totalVertices; intermediario++) {
            for (int origem = 0; origem < totalVertices; origem++) {
                for (int destino = 0; destino < totalVertices; destino++) {
                    // se da p ir origem -> intermediario
                    // E de 'intermediario' até 'destino',
                    // então consigo ir de 'origem' até 'destino'
                    if (matrizAlcancavel[origem][intermediario] && matrizAlcancavel[intermediario][destino]) {
                        matrizAlcancavel[origem][destino] = true;
                    }
                }
            }
        }

        return matrizAlcancavel;
    }

    public void imprime_warshall() {
        boolean[][] matrizAlcancavel = warshall();

        System.out.println("\n-- Matriz de Alcançabilidade (Warshall) --");

        // cabeçalho com rótulosd das colunas
        System.out.print("     ");
        for (int coluna = 0; coluna < totalVertices; coluna++) {
            System.out.printf("%4s", rotulosVertices[coluna]);
        }
        System.out.println();

        // linhas da matriz
        for (int linha = 0; linha < totalVertices; linha++) {
            System.out.printf("%4s ", rotulosVertices[linha]);
            for (int coluna = 0; coluna < totalVertices; coluna++) {
                System.out.printf("%4s", matrizAlcancavel[linha][coluna] ? "1" : "0");
            }
            System.out.println();
        }
    }

    // menor caminho do vértice 'origem' até todos os outros vértices
    // return do vetor de menores dists (Double.MAX_VALUE = inalcançável)
    public double[] dijkstra(int origem) {
        double[] menorDistancia = new double[totalVertices];
        boolean[] jaVisitado = new boolean[totalVertices]; // v ja visto
        int[] verticeAnterior = new int[totalVertices]; // p reconstruir caminho

        // dist infinita (-origem)
        for (int vertice = 0; vertice < totalVertices; vertice++) {
            menorDistancia[vertice] = Double.MAX_VALUE;
            jaVisitado[vertice] = false;
            verticeAnterior[vertice] = -1;
        }
        menorDistancia[origem] = 0.0;

        for (int passo = 0; passo < totalVertices; passo++) {
            int verticeMaisProximo = -1;
            for (int vertice = 0; vertice < totalVertices; vertice++) {
                //n visitado com menor dist
                if (!jaVisitado[vertice] && (verticeMaisProximo == -1 ||
                        menorDistancia[vertice] < menorDistancia[verticeMaisProximo])
                ) {
                    verticeMaisProximo = vertice;
                }
            }

            if (verticeMaisProximo == -1 || menorDistancia[verticeMaisProximo] == Double.MAX_VALUE)
                break;

            jaVisitado[verticeMaisProximo] = true;

            // verifica se passar por verticeMaisProximo melhora o caminho para os vizinhos
            Aresta arestaAtual = listaDeAdjacencias[verticeMaisProximo];
            while (arestaAtual != null) {
                int vizinho = arestaAtual.verticeDestino;
                double distanciaViaAtual = menorDistancia[verticeMaisProximo] + arestaAtual.peso;

                if (!jaVisitado[vizinho] && distanciaViaAtual < menorDistancia[vizinho]) {
                    menorDistancia[vizinho] = distanciaViaAtual;
                    verticeAnterior[vizinho]  = verticeMaisProximo;
                }
                arestaAtual = arestaAtual.proximaAresta;
            }
        }

        // imprime os resultados
        System.out.println("\n-- Dijkstra a partir de " + rotulosVertices[origem] + " --");
        for (int vertice = 0; vertice < totalVertices; vertice++) {
            if (menorDistancia[vertice] == Double.MAX_VALUE) {
                System.out.println(rotulosVertices[origem] + " -> " + rotulosVertices[vertice] + ": inalcançável");
            } else {
                System.out.print(rotulosVertices[origem] + " -> " + rotulosVertices[vertice]
                        + ": " + menorDistancia[vertice] + "  |  caminho: ");
                imprimeCaminho(verticeAnterior, vertice);
                System.out.println();
            }
        }

        return menorDistancia;
    }

    // recursivo: imprime até o vértice
    // voltar até origem -> verticeAnterior
    private void imprimeCaminho(int[] verticeAnterior, int vertice) {
        if (verticeAnterior[vertice] == -1) {
            System.out.print(rotulosVertices[vertice]); // chegou na origem, imprime e para
            return;
        }
        imprimeCaminho(verticeAnterior, verticeAnterior[vertice]); // imprime o trecho anterior primeiro
        System.out.print(" -> " + rotulosVertices[vertice]);
    }
}