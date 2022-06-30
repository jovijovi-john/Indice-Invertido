package indice;

public class Par {
    private Integer ocorrencias;
    private Integer id;

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
