public class Grafo {
    private Aresta[] listaDeAdjacencias; // cada posição aponta para a lista encadeada de arestas daquele vértice
    public String[] rotulosVertices;     // rótulo/nome de cada vértice
    public int totalVertices;            // quantidade de vértices no grafo

    public Grafo(int totalVertices) {
        this.totalVertices = totalVertices;
        this.listaDeAdjacencias = new Aresta[totalVertices];
        this.rotulosVertices = new String[totalVertices];
    }

    // cria uma aresta direcionada de 'verticeOrigem' para 'verticeDestino' com o peso dado
    public void cria_adjacencia(int verticeOrigem, int verticeDestino, double peso) {
        boolean origemValida  = verticeOrigem  >= 0 && verticeOrigem  < totalVertices;
        boolean destinoValido = verticeDestino >= 0 && verticeDestino < totalVertices;

        if (origemValida && destinoValido) {
            Aresta novaAresta = new Aresta(verticeDestino, peso);
            novaAresta.proximaAresta = listaDeAdjacencias[verticeOrigem]; // insere no início da lista
            listaDeAdjacencias[verticeOrigem] = novaAresta;
        }
    }

    // remove a aresta que vai de 'verticeOrigem' para 'verticeDestino'
    public void remove_adjacencia(int verticeOrigem, int verticeDestino) {
        boolean origemValida      = verticeOrigem >= 0 && verticeOrigem < totalVertices;
        boolean listaNaoVazia     = listaDeAdjacencias[verticeOrigem] != null;

        if (origemValida && listaNaoVazia) {
            // caso especial: a aresta a remover é a primeira da lista
            if (listaDeAdjacencias[verticeOrigem].verticeDestino == verticeDestino) {
                listaDeAdjacencias[verticeOrigem] = listaDeAdjacencias[verticeOrigem].proximaAresta;
            } else {
                // percorre a lista até encontrar a aresta anterior à que será removida
                Aresta arestaAtual = listaDeAdjacencias[verticeOrigem];
                while (arestaAtual.proximaAresta != null &&
                        arestaAtual.proximaAresta.verticeDestino != verticeDestino) {
                    arestaAtual = arestaAtual.proximaAresta;
                }
                // desliga a aresta encontrada da lista
                if (arestaAtual.proximaAresta != null) {
                    arestaAtual.proximaAresta = arestaAtual.proximaAresta.proximaAresta;
                }
            }
        }
    }

    // preenche o vetor 'vizinhos' com os índices dos vértices adjacentes a 'vertice'
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

    // imprime todas as arestas do grafo no formato: índice (rótulo) -> [destino, peso]
    public void imprime_adjacencias() {
        for (int vertice = 0; vertice < totalVertices; vertice++) {
            System.out.print(vertice + " (" + rotulosVertices[vertice] + ") -> ");
            Aresta arestaAtual = listaDeAdjacencias[vertice];
            while (arestaAtual != null) {
                System.out.print("[destino: " + arestaAtual.verticeDestino + ", peso: " + arestaAtual.peso + "] ");
                arestaAtual = arestaAtual.proximaAresta;
            }
            System.out.println();
        }
    }

    // define o rótulo (nome) de um vértice
    public void seta_informacao(int vertice, String rotulo) {
        if (vertice >= 0 && vertice < totalVertices) {
            rotulosVertices[vertice] = rotulo;
        }
    }

    // ── Warshall ─────────────────────────────────────────────────────────────

    // calcula a matriz de alcançabilidade:
    // matrizAlcancavel[i][j] == true significa que existe algum caminho de i até j
    public boolean[][] warshall() {
        boolean[][] matrizAlcancavel = new boolean[totalVertices][totalVertices];

        // passo 1: marca as arestas diretas que já existem no grafo
        for (int vertice = 0; vertice < totalVertices; vertice++) {
            matrizAlcancavel[vertice][vertice] = true; // todo vértice alcança a si mesmo
            Aresta arestaAtual = listaDeAdjacencias[vertice];
            while (arestaAtual != null) {
                matrizAlcancavel[vertice][arestaAtual.verticeDestino] = true;
                arestaAtual = arestaAtual.proximaAresta;
            }
        }

        // passo 2: para cada vértice intermediário possível,
        // verifica se ele cria novos caminhos entre outros pares de vértices
        for (int intermediario = 0; intermediario < totalVertices; intermediario++) {
            for (int origem = 0; origem < totalVertices; origem++) {
                for (int destino = 0; destino < totalVertices; destino++) {
                    // se consigo ir de 'origem' até 'intermediario'
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

    // imprime a matriz de alcançabilidade com os rótulos dos vértices
    public void imprime_warshall() {
        boolean[][] matrizAlcancavel = warshall();

        System.out.println("\n-- Matriz de Alcançabilidade (Warshall) --");

        // cabeçalho com os rótulos das colunas
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

    // ── Dijkstra ─────────────────────────────────────────────────────────────

    // calcula o menor caminho do vértice 'origem' até todos os outros vértices
    // retorna o vetor de menores distâncias (Double.MAX_VALUE = inalcançável)
    public double[] dijkstra(int origem) {
        double[] menorDistancia  = new double[totalVertices];  // menor custo conhecido até cada vértice
        boolean[] jaVisitado     = new boolean[totalVertices]; // vértices já processados
        int[] verticeAnterior    = new int[totalVertices];     // usado para reconstruir o caminho

        // inicializa: distância infinita para todos, exceto a origem
        for (int vertice = 0; vertice < totalVertices; vertice++) {
            menorDistancia[vertice] = Double.MAX_VALUE;
            jaVisitado[vertice]     = false;
            verticeAnterior[vertice] = -1;
        }
        menorDistancia[origem] = 0.0;

        for (int passo = 0; passo < totalVertices; passo++) {

            // escolhe o vértice ainda não visitado com a menor distância conhecida
            int verticeMaisProximo = -1;
            for (int vertice = 0; vertice < totalVertices; vertice++) {
                if (!jaVisitado[vertice] &&
                        (verticeMaisProximo == -1 || menorDistancia[vertice] < menorDistancia[verticeMaisProximo])) {
                    verticeMaisProximo = vertice;
                }
            }

            // se não sobrou vértice alcançável, encerra
            if (verticeMaisProximo == -1 || menorDistancia[verticeMaisProximo] == Double.MAX_VALUE) break;

            jaVisitado[verticeMaisProximo] = true;

            // relaxamento: verifica se passar por 'verticeMaisProximo' melhora o caminho para os vizinhos
            Aresta arestaAtual = listaDeAdjacencias[verticeMaisProximo];
            while (arestaAtual != null) {
                int vizinho          = arestaAtual.verticeDestino;
                double distanciaViaAtual = menorDistancia[verticeMaisProximo] + arestaAtual.peso;

                if (!jaVisitado[vizinho] && distanciaViaAtual < menorDistancia[vizinho]) {
                    menorDistancia[vizinho]   = distanciaViaAtual;
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

    // auxiliar recursivo: imprime o caminho completo até o vértice 'vertice'
    // usando o vetor 'verticeAnterior' para voltar até a origem
    private void imprimeCaminho(int[] verticeAnterior, int vertice) {
        if (verticeAnterior[vertice] == -1) {
            System.out.print(rotulosVertices[vertice]); // chegou na origem, imprime e para
            return;
        }
        imprimeCaminho(verticeAnterior, verticeAnterior[vertice]); // imprime o trecho anterior primeiro
        System.out.print(" -> " + rotulosVertices[vertice]);
    }
}