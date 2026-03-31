public class Main {
    public static void main(String[] args) {

        // cria o grafo com 5 vértices
        Grafo grafo = new Grafo(5);

        // define os rótulos de cada vértice
        grafo.seta_informacao(0, "A");
        grafo.seta_informacao(1, "B");
        grafo.seta_informacao(2, "C");
        grafo.seta_informacao(3, "D");
        grafo.seta_informacao(4, "E");

        // cria as arestas (origem, destino, peso)
        grafo.cria_adjacencia(0, 1, 2.5); // A -> B
        grafo.cria_adjacencia(0, 2, 1.8); // A -> C
        grafo.cria_adjacencia(1, 3, 3.0); // B -> D
        grafo.cria_adjacencia(2, 4, 4.2); // C -> E

        // imprime o grafo inicial
        System.out.println("-- Grafo inicial --");
        grafo.imprime_adjacencias();

        // lista os vértices adjacentes ao vértice 0 (A)
        int[] vizinhosDeA    = new int[5];
        int quantidadeVizinhos = grafo.adjacentes(0, vizinhosDeA);

        System.out.println("\nAdjacentes do vértice 0 (A):");
        for (int indice = 0; indice < quantidadeVizinhos; indice++) {
            System.out.println(vizinhosDeA[indice]);
        }

        // testa Warshall e Dijkstra com o grafo completo
        grafo.imprime_warshall();
        grafo.dijkstra(0);

        // remove a aresta A -> C e imprime o grafo atualizado
        grafo.remove_adjacencia(0, 2);
        System.out.println("\n-- Grafo após remoção da aresta A -> C --");
        grafo.imprime_adjacencias();
    }
}