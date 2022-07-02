
package estruturas;

import java.util.LinkedList;
import java.util.Queue;

public class ListaEncadeada<Key, Value> {
    // Quantidade de pares na tabela
    private int qtdPares;
    // Pega o Primeiro nó da lista
    private Node nodeFirst;

    //nó na lista encadeada.
    private class Node{
        private Key key;
        private Value value;
        private Node next;

        public Node(Key key, Value val, Node next){
            this.key = key;
            this.value = val;
            this.next = next;
        }
    }



    public ListaEncadeada(){}

    //Pegar a qtd de pares na lista
    public int getSize(){
        return qtdPares;
    }

    public Boolean isEmpty(){
        return getSize() == 0;
    }



    public boolean contains(Key key){
        return get(key) != null;
    }

    //Pegar um elemento da lista
    public Value get(Key key){
        for(Node n = nodeFirst; n != null; n = n.next){
            if(key.equals(n.key))
                return n.value;
        }
        return null;
    }

    public void put(Key key, Value valor){
        if (valor == null){
            delete(key);
            return;
        }

        for(Node i= nodeFirst; i != null; i = i.next){
            if(key.equals(i.key)){
                i.value = valor;
                return;
            }
        }
        nodeFirst = new Node(key, valor, nodeFirst);
        qtdPares++;
    }
    public void delete(Key key){
        nodeFirst = delete(nodeFirst, key);
    }


    public Iterable<Key> keys(){
        Queue<Key> fila = new LinkedList<Key>();
        for(Node x = nodeFirst; x != null; x = x.next){
            ((LinkedList<Key>) fila).add(x.key);
        }
        return fila;
    }

    //Método de deleção
    private Node delete(Node n, Key key){
        if(n == null){
            return null;
        }
        if (key.equals(n.key)){
            qtdPares--;
            return n.next;
        }
        n.next = delete(n.next, key);
        return n;
    }

}