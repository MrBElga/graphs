package Lista;

import java.util.*;

public class Lista {
    private No inicio;

    public Lista() {
        this.inicio = null;
    }
    public void inicializa() {
        inicio = null;
    }
    public No getInicio() {
        return inicio;
    }

    public void inserirFim(String aresta, int custo) {
        No nova = new No(aresta, custo, null, null), atual;
        if (inicio == null) {
            inicio = nova;
        } else {
            atual = inicio;
            while (atual.getProx() != null) {
                atual = atual.getProx();
            }
            atual.setProx(nova);
            nova.setAnt(atual);
        }
    }

    public void exibirListap() {
        No atual = inicio;
        if (atual == null) {
            System.out.println("Lista vazia");
        } else {
            System.out.print(atual.getAresta());
            atual = atual.getProx();
            while (atual != null) {
                System.out.print(" -> " + atual.getCusto() + " | " + atual.getAresta());
                atual = atual.getProx();
            }
            System.out.println();
        }
    }

    public void exibirLista() {
        No atual = inicio;
        if (atual == null) {
            System.out.println("Lista vazia");
        } else {
            System.out.println("=========" + atual.getAresta() + "========");
            atual = atual.getProx();
            while (atual != null) {
                System.out.println(" " + atual.getAresta());
                atual = atual.getProx();
            }
            System.out.println("=======================");
        }
    }

    public void verificaArticulacao(Lista[] listaAdjacencia, String[] rotulos) {
        int n = listaAdjacencia.length,vetAtual,ordem=0;
        boolean[] visitado = new boolean[n];
        int[] prenum = new int[n];
        int[] menor = new int[n];
        int[] pais = new int[n];
        boolean[] articulacao = new boolean[n];
        Integer[] indices = new Integer[n];
        Arrays.fill(pais, -1);
        Arrays.fill(visitado, false);
        Arrays.fill(articulacao, false);

        for (int i = 0; i < n; i++)
            indices[i] = i;
        Arrays.sort(indices, Comparator.comparingInt(i -> rotulos[i].charAt(0)));
        for (int i = 0; i < n; i++) {
            vetAtual = indices[i];
            if (!visitado[vetAtual])
                dfs(listaAdjacencia, vetAtual, visitado, prenum, menor, pais, articulacao, ordem, rotulos);
        }
        exibirArvoreDFSVisual(rotulos, pais, listaAdjacencia);
        System.out.println("======================================================");
        System.out.println("Influencers (PONTO DE ARTICULAçAO):");
        for (int i = 0; i < n; i++) {
            if (articulacao[i]) {
                System.out.println(rotulos[i]);
            }
        }
        System.out.println("======================================================");
    }

    private void dfs(Lista[] listaAdjacencia, int vetAtual, boolean[] visitado, int[] prenum, int[] menor, int[] pais, boolean[] articulacao, int ordem, String[] rotulos) {
        visitado[vetAtual] = true;
        prenum[vetAtual] = menor[vetAtual] = ++ordem; // Definir tempo de visita
        int filhos = 0;
        List<Integer> adjacentes = new ArrayList<>();
        No atual = listaAdjacencia[vetAtual].getInicio();

        while (atual != null) {
            adjacentes.add(findIndex(rotulos, atual.getAresta()));
            atual = atual.getProx();
        }
        adjacentes.sort(Comparator.comparingInt(i -> rotulos[i].charAt(0)));
        for (int adj : adjacentes) {
            if (!visitado[adj]) {
                filhos++;
                pais[adj] = vetAtual;
                dfs(listaAdjacencia, adj, visitado, prenum, menor, pais, articulacao, ordem, rotulos);

                // Atualiza o valor menor[v]
                menor[vetAtual] = Math.min(menor[vetAtual], menor[adj]);

                // Condições para identificar pontos de articulação
                if (pais[vetAtual] == -1 && filhos > 1)
                    articulacao[vetAtual] = true; // A raiz é articulação se tiver mais de um filho
                if (pais[vetAtual] != -1 && menor[adj] >= prenum[vetAtual])
                    articulacao[vetAtual] = true; // Verifica a condição para vértices não raiz
            } else if (adj != pais[vetAtual]) {
                // Atualiza o valor menor[v] para o ancestral
                menor[vetAtual] = Math.min(menor[vetAtual], prenum[adj]);
            }
        }
    }

    public static int findIndex(String[] rotulos, String rotulo) {
        for (int i = 0; i < rotulos.length; i++) {
            if (rotulos[i].equals(rotulo)) {
                return i;
            }
        }
        return -1;
    }

    public void exibirArvoreDFSVisual(String[] rotulos, int[] pais, Lista[] listaAdjacencia) {
        System.out.println("=============================================");
        System.out.println("Rede (ARVORE GERADORA):");
        System.out.println("ANTENCÃO TUDO QUE ESTA ENTRE [] SÃO ARESTAS QUE ESTÁ CONECTADA POREM A ARVORE ESCOLHEU EM ORDEM ALFABETICA");
        for (int i = 0; i < pais.length; i++)
            if (pais[i] == -1)
                exibirSubArvore(i, 0, rotulos, pais, listaAdjacencia);
        System.out.println("=============================================");
    }

    private void exibirSubArvore(int vertice, int nivel, String[] rotulos, int[] pais, Lista[] listaAdjacencia) {
        No atual;
        for (int i = 0; i < nivel; i++)
            System.out.print("   ");
        System.out.print(rotulos[vertice]);
        atual = listaAdjacencia[vertice].getInicio();
        System.out.print(" [ ");
        while (atual != null) {
            if (atual.getCusto() > 0)
                System.out.print(atual.getAresta() + " ");
            atual = atual.getProx();
        }
        System.out.println("]");
        for (int i = 0; i < pais.length; i++)
            if (pais[i] == vertice)
                exibirSubArvore(i, nivel + 1, rotulos, pais, listaAdjacencia);
    }

}
