public class Main {
    public static void main(String[] args) {

        //cria grafo
        Grafo g = new Grafo(5);

        //seta infos (rótulos dos vértices)
        g.seta_informacao(0, "A");
        g.seta_informacao(1, "B");
        g.seta_informacao(2, "C");
        g.seta_informacao(3, "D");
        g.seta_informacao(4, "E");

        //cria adjac
        g.cria_adjacencia(0, 1, 2.5);
        g.cria_adjacencia(0, 2, 1.8);
        g.cria_adjacencia(1, 3, 3.0);
        g.cria_adjacencia(2, 4, 4.2);

        //imprime grafo
        System.out.println("--Grafo inicial--");
        g.imprime_adjacencias();

        //teste adjac
        int[] adj = new int[5];
        int quantidade = g.adjacentes(0, adj);

        System.out.println("\nAdjacentes do vértice 0:");
        for (int k = 0; k < quantidade; k++) {
            System.out.println(adj[k]);
        }

        //remove uma aresta
        g.remove_adjacencia(0, 1);

        //imprime dnv
        System.out.println("\n--Grafo dps da Remoção (0 -> 1)--");
        g.imprime_adjacencias();
    }
}