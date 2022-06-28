package estruturas;
import items.Item;


public class AVLTree<Key extends Comparable<Key>, Value> implements Item<Key, Value>{

    private class Node {
        Key chave;
        Value valor;
        Node esq, dir;

        int altura;
        int tamanho;

        Node(Key chave, Value valor, int tamanho, int altura) {
            this.chave = chave;
            this.valor = valor;

            this.tamanho = tamanho;
            this.altura = altura;
        }

    }

    private Node raiz;

    // Retorna quantos nós tem na árvore
    public int tamanho() {
        return tamanho(raiz);
    }

    // Retorna quantos nós tem na subárvore
    private int tamanho(Node no) {
        if (no == null) {
            return 0;
        }

        return no.tamanho;
    }

    // Retorna a altura da árvore
    public int altura() {
        return altura(raiz);
    }

    // Retorna a altura da subárvore
    private int altura(Node no) {
        if (no == null) {
            return -1;
        }

        return no.altura;
    }

    // Verifica se a árvore está vazia
    public boolean isEmpty() {
        return tamanho(raiz) == 0;
    }

    // Rotaciona o nó à esquerda
    private Node rotacaoEsquerda(Node no) {

        if(no == null || no.dir == null)
            return no;

        // A direita do nó sobe
        Node newRoot = no.dir;

        // A esquerda do nó que sobe passa para o que desce
        no.dir = newRoot.esq;
        // O nó desce
        newRoot.esq = no;

        no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
        newRoot.altura = 1 + Math.max(altura(newRoot.esq), altura(newRoot.dir));

        return newRoot;
    }

    // Rotaciona o nó à direita
    private Node rotacaoDireita(Node no) {

        if(no == null || no.esq == null)
            return no;

        // A esquerda do nó sobe
        Node newRoot = no.esq;

        // A direita do nó que sobe passa para o que desce
        no.esq = newRoot.dir;
        // O nó desce
        newRoot.dir = no;

        no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
        newRoot.altura = 1 + Math.max(altura(newRoot.esq), altura(newRoot.dir));

        return newRoot;
    }

    // Insere um item na árvore
    public void put(Key chave, Value valor) {

        if (chave == null) {
            return;
        }

        if (valor == null) {
            delete(chave);
            return;
        }

        raiz = put(raiz, chave, valor);
    }

    // Insere um item na árvore a partir de um nó
    private Node put(Node no, Key chave, Value valor) {

        // Nó encontrou sua posição
        if (no == null) {
            return new Node(chave, valor, 1, 0);
        }

        int compare = chave.compareTo(no.chave);

        // Busca binária da posição do nó
        if (compare < 0) {
            no.esq = put(no.esq, chave, valor);
        } else if (compare > 0) {
            no.dir = put(no.dir, chave, valor);
        } else {
            // Nó pré-existente atualiza o valor
            no.valor = valor;
        }

        no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
        no.tamanho = tamanho(no.esq) + 1 + tamanho(no.dir);

        // O balanceamento da árvore precisa ser verificado
        return balance(no);
    }

    // Balanceia a árvore
    private Node balance(Node no) {

        // Se direita - esquerda == 2
        if (fatorBalanceamento(no) > 1) {
            // Se o desbalanceamento vem da direita do filho 
            if (fatorBalanceamento(no.dir) < 0) {
                no.dir = rotacaoDireita(no.dir);
            }
            no = rotacaoEsquerda(no);
        }

        // Se direita - esquerda == -2
        if (fatorBalanceamento(no) < -1) {
            // Se o desbalanceamento vem da esquerda do filho
            if (fatorBalanceamento(no.esq) > 0) {
                no.esq = rotacaoEsquerda(no.esq);
            }
            no = rotacaoDireita(no);
        }

        return no;
    }

    // Retorna o fator de balanceamento do nó
    // Fator de balanceamento == direita.altura - esquerda.altura
    private int fatorBalanceamento(Node no) {
        if(no == null)
            return 0;
        return altura(no.dir) - altura(no.esq);
    }

    // Busca um elemento na árvore
    public Value get(Key chave) {
        if (chave == null) {
            return null;
        }

        return get(raiz, chave);
    }

    // Busca um elemento na árvore a partir de um nó
    private Value get(Node no, Key chave) {
        // Se não encontrou retorna nulo
        if (no == null) {
            return null;
        }

        // Busca binária na árvore
        int compare = chave.compareTo(no.chave);
        if (compare < 0) {
            return get(no.esq, chave);
        } else if (compare > 0) {
            return get(no.dir, chave);
        } else {
            return no.valor;
        }
    }

    // Verifica a existência de um nó na árvore, usando get
    public boolean contains(Key chave) {
        if (chave == null) {
            throw new IllegalArgumentException("Argument to contains() cannot be null");
        }
        return get(chave) != null;
    }

    // Deleta um elemento da árvore
    public void delete(Key chave) {

        if (chave == null) {
            return;
        }

        raiz = delete(raiz, chave);

    }

    // Deleta um elemento da árvore a partir de um nó
    private Node delete(Node no, Key chave) {

        // Se não encontrou a árvore não altera
        if (no == null) {
            return null;
        }

        int compare = chave.compareTo(no.chave);

        // Busca binária pelo nó
        if (compare < 0) {
            no.esq = delete(no.esq, chave);
        } else if (compare > 0) {
            no.dir = delete(no.dir, chave);
        } else {
            // Se nó tiver dois filhos substitui pelo menor da direita
            if(no.esq != null && no.dir != null) {
                Node temp = min(no.dir);
                no.chave = temp.chave;
                no.valor = temp.valor;
                no.dir = delete(no.dir, temp.chave);
            }
            // Se nó tiver apenas um filho ou nenhum, substitui pelo filho ou null
            else if(no.esq != null)
                return no.esq;
            else
                return no.dir;
        }

        no.altura = 1 + Math.max(altura(no.esq), altura(no.dir));
        no.tamanho = tamanho(no.esq) + 1 + tamanho(no.dir);

        // É necessário verificar o balanceamento
        return balance(no);
    }

    // Retorna o menor elemento de uma subárvore
    // O menor elemento é aquele que não possui esquerda
    private Node min(Node no) {
        if(no == null) return null;
        if(no.esq != null) return min(no.esq);
        return no;
    }

    // Verifica se a árvore está balanceada
    @SuppressWarnings("unused")
    private boolean isAVL() {
        return isAVL(raiz);
    }

    // Verifica se a subárvore está balanceada
    private boolean isAVL(Node no) {
        if (no == null) {
            return true;
        }

        int fatorBalanceamento = fatorBalanceamento(no);
        // Se fator de balanceamento for menor que -1 ou maior que 1, está desbalanceada
        if (fatorBalanceamento < -1 || fatorBalanceamento > 1) {
            return false;
        }

        // Verificação precisa ser feita em cada nó
        return isAVL(no.esq) && isAVL(no.dir);
    }

    // Caminha pela árvore
    public void view() {
        view(raiz);
    }

    // Caminha pela subárvore em pré-ordem, imprimindo o elemento do nó
    private void view(Node no) {
        if(no == null) return;
        System.out.println(no.valor.toString());
        view(no.esq);
        view(no.dir);
    }

}