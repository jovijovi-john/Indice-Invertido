package indice;

public class Par {
    private Integer ocorrencias; // numero de ocorrencias do termo na descrição do produto
    private Integer id; // id do produto

    @Override
    public String toString() {
        return "<" +
                ocorrencias +
                ", " + id +
                '>';
    }

    public Par(Integer id){
        this.ocorrencias = 1;
        this.id = id;
    }

    public Integer getOcorrencias() {
        return ocorrencias;
    }

    public Integer getId() {
        return id;
    }

    public void incrementarOcorrencias() {
        this.ocorrencias++;
    }
}
