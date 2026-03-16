public class Main {
    public static void main(String[] args) {

        Grafo g = new Grafo(5);

        g.seta_informacao(0, "A");
        g.seta_informacao(2, "C");

        System.out.println("Vértice 0: " + g.vertices[0]);
        System.out.println("Vértice 2: " + g.vertices[2]);

        g.cria_adjacencia(0, 1, 2.5);
        g.cria_adjacencia(0, 2, 1.8);
        g.cria_adjacencia(1, 3, 3.0);
        g.cria_adjacencia(2, 4, 4.2);

        int[] adj = new int[5];

        int quantidade = g.adjacentes(0, adj);

        System.out.println("Adjacentes do vértice 0:");

        for (int k = 0; k < quantidade; k++) {
            System.out.println(adj[k]);
        }

        g.remove_adjacencia(0,1);

        System.out.println("Depois de remover a aresta 0 -> 1:");

        quantidade = g.adjacentes(0, adj);

        for (int k = 0; k < quantidade; k++) {
            System.out.println(adj[k]);
        }
    }
}