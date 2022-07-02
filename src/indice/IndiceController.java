package indice;

import estruturas.AVLTree;
import estruturas.HashEncadeado;
import estruturas.RBTree;
import items.Dicionario;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndiceController {

    public static Dicionario<String, ArrayList<Par>> create(int option) throws IOException {

        String nome_arquivo = "src/indice/victoriassecret_com.csv";

        Dicionario<String, ArrayList<Par>> dicionario;

        switch (option) {
            case 1 -> dicionario = new AVLTree<String, ArrayList<Par>>();
            case 2 -> dicionario = new RBTree<String, ArrayList<Par>>();
            case 3 -> dicionario = new HashEncadeado<String, ArrayList<Par>>();
            default -> {
                System.out.println("Informe uma opção válida!");
                return null;
            }
        }

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


                // pegar cada termo da descrição
                String [] descricao_splitada = descricao_produto.split(" ");

                // tratamento dos termos

                for (String termoPalavra : descricao_splitada) {

                    String aux = termoPalavra.toLowerCase();
//                    System.out.println("está "+ aux);

                    aux = aux.replaceAll("[-.,+=*&:;%$#@|!?_\"\'’\\r•]", "");
                    aux = termoPalavra.replaceAll("[/\u00A0]", " ");

                    aux = aux.replace(" ", "");

                    if(aux.replaceAll("\\u00A0"," ").split(" ").length > 1) {
                        aux = aux.replaceAll("\\u00A0"," ").split(" ")[0];
                    }/* else if(palavraAux.replaceAll("\\-"," ").toLowerCase().split(" ").length > 1) {
						palavraAux = palavraAux.replaceAll("\\u00A0"," ").toLowerCase().split(" ")[1];
					}*/

                    aux = aux.toLowerCase();

                    Par par = new Par(id_produto);
                    IndiceInvertidoItem termo = new IndiceInvertidoItem(aux, par);

                    if (dicionario.get(termo.getTermo()) == null) { // verifica se o elemento ja existe na arvore
                        dicionario.put(termo.getTermo(), termo.getPares());
                    } else { // ja existe na árvore

                        ArrayList<Par> pares = dicionario.get(termo.getTermo()); // pega os pares daquele termo
                        boolean parJaExiste = false;

                        for (Par parAtual : pares) {
                            if (parAtual.getId() == id_produto) {
                                parAtual.incrementarOcorrencias();
                                parJaExiste = true;
                                break;
                            }
                        }

                        if (!parJaExiste) { // cria um novo par
                            Par novoPar = new Par(id_produto);
                            pares.add(novoPar);
                        }

                    }

                    //System.out.println("...");
                }
                linha = reader.readLine();
            }

            arquivo.close();
        } catch (IOException exception) {
            System.err.printf("Erro ao abrir arquivo.\n" + exception.getMessage());
        }
        return dicionario;
    }
}
