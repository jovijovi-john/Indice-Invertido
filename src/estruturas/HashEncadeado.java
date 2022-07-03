package estruturas;
import items.Dicionario;


import java.util.LinkedList;
import java.util.Queue;


public class HashEncadeado<Key, Value> implements Dicionario<Key, Value> {
    // Quantidade de pares de valores presentes na tabela hash
    private int qtdPares;
    // Tamanho M  da tabela  --> tratamento linear
    private int M = 16;
    private ListaEncadeada<Key, Value>[] list;


    public HashEncadeado(int size) {
        this.M = size;
        list = (ListaEncadeada<Key,Value>[]) new ListaEncadeada[M] ;
        for (int i = 0; i < M; i++)
            list[i] = new ListaEncadeada<Key, Value>();
    }


    public HashEncadeado() {
        list = (ListaEncadeada<Key,Value>[]) new ListaEncadeada[M] ;
        for (int i = 0; i < M; i++)
            list[i] = new ListaEncadeada<Key, Value>();
    }


    //Função que realiza o calculo com hash Auxiliar se houver colisão
    private int hashAux(Key key){
        return 1 + ((key.hashCode() & 0x7fffffff) % M-1);
    }

    //Calcula o hash entre o intervalo 0 e m--
    private int hash(Key key)
    {
        return (key.hashCode() & 0x7fffffff) % M;
    }


    private void resize(int node) {
        HashEncadeado<Key, Value> temporario;
        temporario = new HashEncadeado<Key, Value>(node);
        for(int i = 0;i < M; i++){
            for(Key key : list[i].keys()){
                temporario.put(key,list[i].get(key));
            }
        }
        this.M = temporario.M;
        this.qtdPares = temporario.qtdPares;
        this.list = temporario.list;
    }

    int getSize(){

        return qtdPares;
    }


    @Override
    public void put(Key key, Value val) {
        if (val == null){
            delete(key);
            return;
        }

        if(qtdPares>= 10*M){
            resize(2*M);
        }

        int i = hash(key);
        if (!list[i].contains(key)){
            qtdPares++;
        }
        list[i].put(key, val);
    }


    public boolean isEmpty(){

        return getSize() == 0;
    }


    public boolean contains(Key key){

        return get(key) != null;
    }


    @Override
    public Value get(Key key){
        int i = hash(key);
        return list[i].get(key);
    }


    public Iterable<Key> keys(){
        Queue<Key> queue = new LinkedList<Key>();
        for (int i=0; i<M;i++){
            for(Key key : list[i].keys())
                ((LinkedList<Key>) queue).add(key);
        }
        return queue;
    }

    public void delete(Key key)
    {
        if (key == null)
            throw new IllegalArgumentException("Error");

        int i = hash(key);
        if(list[i].contains(key)){
            qtdPares--;
            list[i].delete(key);
        }
        if(M > 16 && qtdPares< 2*M){
            resize(M/2);
        }
    }

    public void view() {

    }

}

