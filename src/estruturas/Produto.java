package estruturas;

public class Produto implements Comparable{
    Integer id;
    String nomeProduto;
    double relevancia;

    public Produto(Integer id, double relevancia) {
        this.id = id;
        this.relevancia = relevancia;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public double getRelevancia() {
        return relevancia;
    }

    public void setRelevancia(double relevancia) {
        this.relevancia = relevancia;
    }

    @Override
    public int compareTo(Object o) {
        Produto produto = (Produto) o;

       return Double.compare(this.getRelevancia(), produto.getRelevancia());
    }

    public String getNomeProduto() {
        return nomeProduto;
    }

    public void setNomeProduto(String nomeProduto) {
        this.nomeProduto = nomeProduto;
    }

    public String toStringCompleto(){
        return
                "ID: " + id + " Nome: " + nomeProduto + " relevância: " + relevancia + "\n";
    }
    @Override
    public String toString() {
        return
                "\n" + "       ==>  [" + id + "]  " + nomeProduto +
                " (" + relevancia + ")";
    }
}
