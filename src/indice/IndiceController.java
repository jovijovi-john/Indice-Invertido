package indice;

import estruturas.AVLTree;
import estruturas.HashEncadeado;
import estruturas.Produto;
import estruturas.RBTree;
import items.Dicionario;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndiceController {
    private static Runtime runtime = Runtime.getRuntime();
    public static HashMap<Integer, HashMap<String, HashMap<Integer, Par>>> create(int option) throws IOException {

        //String nome_arquivo = "src/indice/victoriassecret_com.csv";
        //String nome_arquivo = "src/indice/hanky.csv";
        String nome_arquivo = "src/indice/amz.csv";

        int qtdTotalLinhas = 0;

        System.out.println("Memória usada antes da criação da estrutura: " + (runtime.totalMemory()-runtime.freeMemory())/(1024*1024) + " MB");
        long start = System.currentTimeMillis();
        Dicionario<String, HashMap<Integer, Par>> dicionario;

        switch (option) {
            case 0 -> {
                System.out.println("Saindo...");
                return null;
            }
            case 1 -> dicionario = new AVLTree<String, HashMap<Integer, Par>>();
            case 2 -> dicionario = new RBTree<String, HashMap<Integer, Par>>();
            case 3 -> dicionario = new HashEncadeado<String, HashMap<Integer, Par>>();
            default -> {
                System.out.println("Informe uma opção válida!");
                return null;
            }
        }

        HashMap<String, HashMap<Integer, Par>> hashAuxiliar = new HashMap<String, HashMap<Integer, Par>>();

        try {
            FileReader arquivo = new FileReader(nome_arquivo);
            BufferedReader reader = new BufferedReader(arquivo);

            String linha = reader.readLine();
            linha = reader.readLine();

            while (linha != null) {
                String[] conteudo_linha = linha.split(",");

                // regex para identificar conteúdos entre aspas
                String regex = "\"([^\"]*)\"";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(linha);

                int id_produto = Integer.parseInt(conteudo_linha[0]);
                qtdTotalLinhas = id_produto;

                String nome_produto = conteudo_linha[1];

                //System.out.println("ID do Produto: " + id_produto);
                //System.out.println("Nome do Produto: " + nome_produto);

                String descricao_produto;

                if (matcher.find()) {
                    // caso tenha aspas no produto (quer dizer que na descrição há vírgulas)
                    descricao_produto = matcher.group(1);
                } else {
                    // caso não tenha aspas no produto (quer dizer que na descrição não há vírgulas)
                    descricao_produto = conteudo_linha[2];
                }
                //System.out.println("Descrição do produto: " + descricao_produto +"\n");

                // limpando os caracteres
                descricao_produto = descricao_produto.replaceAll("[.,+=*&:;%$#@|!?_\"\'\\r’•]", "");
                descricao_produto = descricao_produto.replaceAll("[\\|\\!\\/\\#\\½\\”\\’\\'\\“\\\"\\+\\.\\^\\:\\,]"," ");
                descricao_produto = descricao_produto.replaceAll("[-.,+=“”*&:®…()•\u00AD–—’;%$#@|!?_\"\']", " ");
                descricao_produto = descricao_produto.replaceAll("[-/\u00A0]", " ");
                descricao_produto = descricao_produto.replace("-", "");

                String [] descricao_splitada = descricao_produto.split(" ");

                for (String termoPalavra : descricao_splitada) {

                    String aux = termoPalavra.toLowerCase();

                    Par par = new Par(id_produto);
                    IndiceInvertidoItem termo = new IndiceInvertidoItem(aux, par);

                    if (termo.getTermo().length() >= 10) {
                        // limitando para 10 caracteres
                        termo.setTermo(termo.getTermo().substring(0, 10));

                        if (dicionario.get(termo.getTermo()) == null) { // verifica se o elemento ja existe na arvore
                            dicionario.put(termo.getTermo(), termo.getPares());
                            hashAuxiliar.put(termo.getTermo(), termo.getPares());

                        } else { // ja existe na árvore

                            HashMap<Integer, Par> pares = dicionario.get(termo.getTermo()); // pega os pares daquele termo
                            HashMap<Integer, Par> paresHashAux = hashAuxiliar.get(termo.getTermo());

                            boolean parJaExiste = false;

                            // verificando se o produto ja existia no array de pares =>>> hashauxiliar
                            if(paresHashAux.containsKey(id_produto)){
                                paresHashAux.get(id_produto).incrementarOcorrencias();
                            } else {
                                Par novoParAux = new Par(id_produto);
                                paresHashAux.put(novoParAux.getId(), novoParAux);
                            }

                            // iterando sobre os pares do termo atual =>>>> dicionario(arvorse, hash)
                            for (Par parAtual : pares.values()) {
                                if (parAtual.getId() == id_produto) {
                                    parJaExiste = true;
                                    break;
                                }
                            }

                            if (!parJaExiste) { // cria um novo par
                                Par novoPar = new Par(id_produto);
                                pares.put(novoPar.getId(), novoPar);
                            }

                        }
                    }

                    //System.out.println("...");
                }

                linha = reader.readLine();
            }
            System.out.println("Memória usada depois da criação da estrutura: " + (runtime.totalMemory()-runtime.freeMemory())/(1024*1024) + " MB");
            long finish = System.currentTimeMillis();
            System.out.println("Tempo de execução: " + (finish - start) + "ms");
            arquivo.close();
        } catch (IOException exception) {
            System.err.printf("Erro ao abrir arquivo.\n" + exception.getMessage());
        }
        //System.out.println(dicionario.get("sexy"));
        HashMap<Integer, HashMap<String, HashMap<Integer, Par>>> retorno = new HashMap<Integer, HashMap<String, HashMap<Integer, Par>>>();
        retorno.put(qtdTotalLinhas, hashAuxiliar);


        return retorno;
    }

    public static ArrayList<String> arrumarTermos(String termos) {
        // transformar em lowerCase e corta a string para 10 caracteres

        ArrayList<String> termosSplitados = new ArrayList<String>();
        String[] termosCortados = termos.split(" ");
        ArrayList<String> retorno = new ArrayList<String>();

        for (int j = 0; j < termosCortados.length; j++ ){
            termosSplitados.add(termosCortados[j]);
        }

        int cont = 0;
        for (String termo : termosSplitados){
            termo = termo.toLowerCase();
            if (termo.length() >= 10){
                retorno.add(termo.substring(0, 10));
            }
            cont++;
        }
        return retorno;
    }

    public static ArrayList<String> retiraInvalidos(HashMap<String, HashMap<Integer, Par>> dicionario, ArrayList<String> termosSplitados) {
        int cont = 0;
        ArrayList<String> termosInvalidos = new ArrayList<String>();
        for (String termo : termosSplitados){
            if (dicionario.containsKey(termosSplitados.get(cont))) {
                System.out.print("");
            } else {
                termosInvalidos.add(termo);
            }
            cont++;
        }

        for (String termo : termosInvalidos){
            termosSplitados.remove(termo);
        }

        return termosSplitados;
    }


    public static ArrayList<String> verificaTamanhoElementos(ArrayList<String> termosSplitados) {
        ArrayList<String> termosArrumados = new ArrayList<String>();
        for (String termo : termosSplitados){
            if (termo.length() >= 10) {
                termosArrumados.add(termo);
            }
        }

        return termosArrumados;
    };

    public static ArrayList<Integer> intersecao (HashMap<String, HashMap<Integer, Par>> dicionario, ArrayList<String> termosUsuario) {

        ArrayList<String> termosSplitados = verificaTamanhoElementos(termosUsuario);
        HashMap<Integer, Par> pares = new HashMap<Integer, Par>();

        int cont = 0;
        ArrayList<String> termosInvalidos = new ArrayList<String>();

        for (String termo : termosSplitados){
            if (dicionario.containsKey(termosSplitados.get(cont))) {
                pares = dicionario.get(termosSplitados.get(cont));
                break;
            }
            System.out.println("O termo " + termo + " não existe no índice");
            termosInvalidos.add(termo);
            cont++;
        }

        if (cont == termosSplitados.size()) {
            System.out.println("Nenhum termo informado existe no índice");
            return null;
        }

        ArrayList<Integer> excluidos = new ArrayList<>();
        ArrayList<Integer> indices = new ArrayList<Integer>();

        // copia tudo do primeiro array para o array auxiliar
        for (Par par : pares.values()) {
            indices.add(par.getId());
        }

        // iterando sobre os termos
        for (String termo : termosSplitados) {
            // verificando se o termo tem tamanho válido
            if (termo.length() < 10) {
                System.out.println("O termo " + termo + " não foi considerado pois tem menos de 10 caracteres");
            } else {
                if (!termosInvalidos.contains(termo)){
                    // iterando sobre o array auxiliar
                    for (Integer indice : indices) {
                        // se o termo contem o indice
                        if (!dicionario.get(termo).containsKey(indice)) {
                            excluidos.add(indice);
                        }
                    }

                    // removendo os itens da interseção que nao existiam no array de pares do item atual
                    for (Integer excluido : excluidos) {
                        indices.remove(excluido);
                    }
                }
            }
        }

        return indices;
    }

    public static HashMap<String, HashMap<Integer, Par>> pegarTodosEmLinha(HashMap<String, HashMap<Integer, Par>> dicionario, Integer linha){
        HashMap<String, HashMap<Integer, Par>> retorno = new HashMap<String, HashMap<Integer, Par>>();
        // procura todas as palavras na linha determinada e retorna elas dentro de um hashmap
        for (String palavra : dicionario.keySet()) {
            // verifica se nos pares do item atual contem a linha procurada
            if (dicionario.get(palavra).containsKey(linha)) {
                retorno.put(palavra, dicionario.get(palavra));
            }
        }
        return retorno;
    }

    public static void getNomesProdutos(HashMap<Integer, Produto> produtos) throws FileNotFoundException {

        //String nome_arquivo = "src/indice/victoriassecret_com.csv";
        String nome_arquivo = "src/indice/amz.csv";
        //String nome_arquivo = "src/indice/hanky.csv";
        try {
            FileReader arquivo = new FileReader(nome_arquivo);
            BufferedReader reader = new BufferedReader(arquivo);

            String linha = reader.readLine();
            linha = reader.readLine();

            while (linha != null) {

                String[] conteudo_linha = linha.split(",");
                int id_produto = Integer.parseInt(conteudo_linha[0]);
                String nome_produto = conteudo_linha[1];

                if (produtos.containsKey(id_produto)){
                    produtos.get(id_produto).setNomeProduto(nome_produto);
                }

                linha = reader.readLine();
            }
            arquivo.close();
        } catch (IOException exception) {
            System.err.printf("Erro ao abrir arquivo.\n" + exception.getMessage());
        }
    }

    public static double definirLimiar(HashMap<Integer, Produto> produtos){
        // calcula o limiar:      limiar = media das relevancias
       Integer size = produtos.size();
        double limiar = 0;

        for (Produto produto : produtos.values()){
            limiar += produto.getRelevancia();
        }

        limiar = (limiar / (double)size);
        return limiar;
    }
}

