package estruturas;

import indice.IndiceController;
import indice.Par;

import java.util.ArrayList;
import java.util.HashMap;

public class Relevancia {
    public static double Relevancia(ArrayList<String> termos, Integer numLinha, HashMap<String, HashMap<Integer, Par>> dicionario, Integer qtdTotalLinhas) {
        HashMap<String, HashMap<Integer, Par>> palavrasDaLinha = IndiceController.pegarTodosEmLinha(dicionario, numLinha);

        Integer qtdPalavrasDaLinha = palavrasDaLinha.size();
        double conta = ((double) 1/qtdPalavrasDaLinha) * (somatorio(termos, palavrasDaLinha, numLinha, qtdTotalLinhas));
        return conta;
    }

    public static double somatorio(ArrayList<String> termos, HashMap<String, HashMap<Integer, Par>> palavrasDaLinha, Integer numLinha, Integer numTotalLinhas) {
        double retorno = 0;
        for (String termo : termos) {
            retorno += peso(palavrasDaLinha.get(termo).get(numLinha).getOcorrencias(), palavrasDaLinha.get(termo).size(), numTotalLinhas);
        }
        return retorno;
    }

    public static double peso (Integer ocorrencias, Integer numProdutos, Integer qtdLinhas) {
        return ocorrencias * (Math.log(qtdLinhas) / numProdutos);
    }
}