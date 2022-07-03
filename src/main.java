import estruturas.Produto;
import estruturas.Relevancia;
import indice.IndiceController;
import indice.Par;
import items.Dicionario;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class main {
    private static Runtime runtime = Runtime.getRuntime();
    public static void main(String args[]) throws IOException {
        System.out.println("==============================================================================");
        System.out.println("|                      [ 1 ] Árvore AVL                                      |");
        System.out.println("|                      [ 2 ] Árvore RedBlack                                 |");
        System.out.println("|                      [ 3 ] Hash Encadeado                                  |");
        System.out.println("|                      [ 0 ] Sair                                            |");
        System.out.println("==============================================================================");

        Scanner leitor = new Scanner(System.in);
        System.out.print("Informe a estrutura para o índice: ");
        int option = leitor.nextInt();
        leitor.nextLine();
        System.out.println("==============================================================================");

        HashMap<Integer, HashMap<String, HashMap<Integer, Par>>> retorno = IndiceController.create(option);
        HashMap<String, HashMap<Integer, Par>> dicionario = new HashMap<String, HashMap<Integer, Par>>();
        Integer qtdTotalLinhas = 0;

        assert retorno != null;
        for (Integer i : retorno.keySet()) {
            qtdTotalLinhas = i;
            dicionario = retorno.get(i);
        }

        System.out.print("Informe o(s) termo(s) desejados: ");
        String termos = leitor.nextLine();
        ArrayList<String> termosSplitados = IndiceController.arrumarTermos(termos);
        termosSplitados = IndiceController.verificaTamanhoElementos(termosSplitados);
        termosSplitados = IndiceController.retiraInvalidos(dicionario, termosSplitados);

        ArrayList<Integer> intersecao = IndiceController.intersecao(dicionario, termosSplitados);
        HashMap<Integer, Produto> itensRelevantesHash = new HashMap<Integer, Produto>();
        LinkedList<Produto> itensRelevantes = new LinkedList<Produto>();

        if (intersecao != null) {

            for (Integer linha : intersecao) {
                double relevanciaItemAtual = Relevancia.Relevancia(termosSplitados, linha, dicionario, qtdTotalLinhas);
                Produto novoProduto = new Produto(linha, relevanciaItemAtual);
                itensRelevantesHash.put(novoProduto.getId(), novoProduto);
                itensRelevantes.add(novoProduto);
            }

            double limiar = IndiceController.definirLimiar(itensRelevantesHash);
            ArrayList<Integer> excluidos = new ArrayList<Integer>();

            for (Produto produto : itensRelevantes){
                if (produto.getRelevancia() < limiar){
                    excluidos.add(produto.getId());
                }
            }

            for (Integer excluido : excluidos) {

                //removendo os itens excluido da lista encadeada
                ArrayList<Produto> excluidosLista = new ArrayList<Produto>();
                for (Produto produto : itensRelevantes){
                    if (Objects.equals(produto.getId(), excluido)){
                        //itensRelevantes.remove(produto);
                        excluidosLista.add(produto);
                    }
                }

                for (Produto pExcluido : excluidosLista){
                    itensRelevantes.remove(pExcluido);
                }

                // removendo do hash
                itensRelevantesHash.remove(excluido);
            }


            IndiceController.getNomesProdutos(itensRelevantesHash);
            itensRelevantes.sort((o2, o1) -> Double.compare(o1.getRelevancia(), o2.getRelevancia()));
            System.out.println("=======================================================================================================");
            System.out.println("                                        Produtos relevantes: ");
            System.out.println("=======================================================================================================");
            System.out.println("     O limiar é de: " + limiar);

            // [id] nomeProduto(relevância)
            for (Produto produto : itensRelevantes) {
                System.out.print(produto);
            }

            System.out.println("\n=======================================================================================================");

            System.out.println("Aperte qualquer tecla para mostrar o indice invertido...");
            leitor.nextLine();
            TreeMap<String, HashMap<Integer, Par>> dicionarioOrdenado = new TreeMap<String, HashMap<Integer, Par>>();
            for (String termo : dicionario.keySet()){
                dicionarioOrdenado.put(termo, dicionario.get(termo));
            }

            System.out.println("Quantidade de termos: " + dicionarioOrdenado.keySet().size() + "\n");
            for (String par : dicionarioOrdenado.keySet()) {
                TreeMap<Integer, Par> paresOrdenados = new TreeMap<Integer, Par>();
                for (Integer idProduto : dicionarioOrdenado.get(par).keySet()){
                    paresOrdenados.put(idProduto, dicionarioOrdenado.get(par).get(idProduto));
                }
                System.out.print(par + ": " );
                for (Par parAtual : paresOrdenados.values()){
                    System.out.print(parAtual + " ");
                };
                System.out.println();
            }

        }

    }
}
