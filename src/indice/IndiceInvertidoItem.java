package indice;

import java.util.ArrayList;

public class IndiceInvertidoItem {
    private String termo;
    private ArrayList<Par> pares;

    public IndiceInvertidoItem(String termo, Par par) {
        this.termo = termo;
        this.pares = new ArrayList<>();
        pares.add(par);
    }

    public String getTermo() {
        return termo;
    }

    public void setTermo(String termo) {
        this.termo = termo;
    }

    public ArrayList<Par> getPares() {
        return pares;
    }

    public void setPares(ArrayList<Par> pares) {
        this.pares = pares;
    }

}
