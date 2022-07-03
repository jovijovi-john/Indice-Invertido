package indice;

import java.util.ArrayList;
import java.util.HashMap;

public class IndiceInvertidoItem {
    private String termo;
    private HashMap<Integer, Par> pares;

    public IndiceInvertidoItem(String termo, Par par) {
        this.termo = termo;
        this.pares = new HashMap<Integer, Par>();
        pares.put(par.getId(), par);
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public HashMap<Integer, Par> getPares() {
        return pares;
    }


}
