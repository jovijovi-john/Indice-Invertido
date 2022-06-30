package indice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IndiceController {

    public static void main(String args[]) throws IOException {

        String nome_arquivo = "src/indice/text.csv";

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

                System.out.println("ID do Produto: " + id_produto);
                System.out.println("Nome do Produto: " + nome_produto);

                if (matcher.find()) {
                    // caso tenha aspas no produto (quer dizer que na descrição há vírgulas)
                    String descricao_produto = matcher.group(1);
                    System.out.println("Descrição do produto: " + descricao_produto +"\n");
                } else {
                    // caso não tenha aspas no produto (quer dizer que na descrição não há vírgulas)
                    System.out.println("Descrição do produto: " + conteudo_linha[2] + "\n");
                }

                linha = reader.readLine();
            }

            arquivo.close();
        } catch (IOException exception) {
            System.err.printf("Erro ao abrir arquivo.\n" + exception.getMessage());
        }

    }
}
