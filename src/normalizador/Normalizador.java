package normalizador;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Normalizador {

    public static List<String> normalizarParaLista(String string, int c) {

        List<String> palavras = new ArrayList<String>();

        //remove acentuação
        string = removerAcentuacao(string);

        //tira pontuação
        string = removerPontuacao(string);

        //deixa tudo em minúsculo
        string = string.toLowerCase();

        //transforma em lista
        palavras = split(string);

        //rejeita palavras menores que c
        palavras = rejeitaPalavrasPequenas(palavras, c);

        //rejeita palavras que começam com números
        palavras = rejeitaNumeros(palavras);

        return palavras;
    }

    public static List<String> apenasCLetras(List<String> palavras, int c, boolean removeIguais) {

        List<String> aux = palavras;
        palavras = new ArrayList<String>();

        for (String palavra : aux) {
            String str = palavra;
            str = str.substring(0, c);

            if (removeIguais) {
                boolean palavraExiste = false;
                for (String palavraCortada : palavras) {
                    if (palavraCortada.equals(str)) {
                        palavraExiste = true;
                        break;
                    }
                }

                if (!palavraExiste) {
                    palavras.add(str);
                }
            } else {
                palavras.add(str);
            }
        }

        return palavras;
    }

    private static String removerAcentuacao(String string) {

        string = Normalizer.normalize(string, Normalizer.Form.NFD);
        string = string.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");

        return string;
    }

    private static String removerPontuacao(String string) {
        String regex = "[^a-zA-Z0-9\\s]+";
        String subst = "";

        Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
        Matcher matcher = pattern.matcher(string);

        string = matcher.replaceAll(subst);

        return string;
    }

    private static ArrayList<String> split(String s) {
        int l = s.length();
        String word = "";
        ArrayList<String> words = new ArrayList();
        for (int i = 0; i < l; i++) {
            char ch = s.charAt(i);
            if (ch != ' ') // append to current word
            {
                word = word + ch;
            } else // if space is found print the previously formed word
            {
                words.add(word);
                word = "";
            }
        }
        words.add(word);

        return words;
    }

    private static List<String> rejeitaPalavrasPequenas(List<String> palavras, int c) {

        List<String> aux = palavras;
        palavras = new ArrayList<String>();

        for (String palavra : aux) {

            if (palavra.length() >= c) {
                palavras.add(palavra);

            }
        }

        return palavras;
    }

    private static List<String> rejeitaNumeros(List<String> palavras) {
        List<String> aux = palavras;
        palavras = new ArrayList<String>();

        String regex = "(^\\d)";

        for (String palavra : aux) {
            Pattern pattern = Pattern.compile(regex, Pattern.MULTILINE);
            Matcher matcher = pattern.matcher(palavra);

            if (!matcher.find()) {
                palavras.add(palavra);
            }

        }
        return palavras;
    }

}