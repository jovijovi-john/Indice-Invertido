package estruturas;
import items.Dicionario;


public class AVLTree<Key extends Comparable<Key>, Value> implements Dicionario<Key, Value>{

    private class Node {
        Key key;
        Value value;
        Node left, right;
        int alt, tamanho;

        Node(Key key, Value value, int tamanho, int alt) {
            this.key = key;
            this.value = value;
            this.tamanho = tamanho;
            this.alt = alt;
        }

    }

    private Node root;


    // Devolve a quantidade de nós da árvore
    public int tamanho() {
        return tamanho(root);
    }


    // Retorna a quantidade de nós nas subárvores
    private int tamanho(Node node) {
        if (node == null) {
            return 0;
        }

        return node.tamanho;
    }

    // Informa a altura da árvore
    public int alt() {
        return alt(root);
    }

    // Retorna a altura da subárvore
    private int alt(Node node) {
        if (node == null) {
            return -1;
        }

        return node.alt;
    }

    // Verifica se há elementos na árvore
    public boolean isEmpty() {
        return tamanho(root) == 0;
    }


    // Realiza a rotação à esquerda
    private Node rotacaoEsquerda(Node node) {
        if(node == null || node.right == null) {
            return node;
        }
        // O nó a direita sobe
        Node novaRaiz = node.right;
        // O nó que desce recebe o nó à esquerda do nó que sobe
        node.right = novaRaiz.left;
        // O nó desce
        novaRaiz.left = node;
        node.alt = 1 + Math.max(alt(node.left), alt(node.right));
        novaRaiz.alt = 1 + Math.max(alt(novaRaiz.left), alt(novaRaiz.right));
        return novaRaiz;
    }

    // Realiza a rotação à direita
    private Node rotacaoDireita(Node node) {

        if(node == null || node.left == null) {
            return node;
        }
        //  O nó a direita sobe
        Node novaRaiz = node.left;

        // O nó que desce recebe o nó à direita do nó que sobe
        node.left = novaRaiz.right;

        // O nó desce
        novaRaiz.right = node;
        node.alt = 1 + Math.max(alt(node.left), alt(node.right));
        novaRaiz.alt = 1 + Math.max(alt(novaRaiz.left), alt(novaRaiz.right));
        return novaRaiz;
    }

    // adiciona um item na árvore
    public void put(Key key, Value value) {

        if (key == null) {
            return;
        }

        if (value == null) {
            delete(key);
            return;
        }

        root = put(root, key, value);
    }

    // Adiciona item na arvore por meio de um nó
    private Node put(Node node, Key key, Value value) {

        // Encontra posição do nó
        if (node == null) {
            return new Node(key, value, 1, 0);
        }

        int comparar = key.compareTo(node.key);

        // Realiza Busca binária da posição do referido nó ( mais eficiente)
        if (comparar < 0) {
            node.left = put(node.left, key, value);
        } else if (comparar > 0) {
            node.right = put(node.right, key, value);
        } else {
            node.value = value;
        }
        node.alt = 1 + Math.max(alt(node.left), alt(node.right));
        node.tamanho = tamanho(node.left) + 1 + tamanho(node.right);

        // Verificando balanceamento da árvore novamente
        return balanceamento(node);
    }

    // Balanceia a árvore
    private Node balanceamento(Node node) {

        // FB = 2
        if (fatorBalanceamento(node) > 1) {
            if (fatorBalanceamento(node.right) < 0) {
                node.right = rotacaoDireita(node.right);
            }
            node = rotacaoEsquerda(node);
        }

        // FB = -2
        if (fatorBalanceamento(node) < -1) {
            if (fatorBalanceamento(node.left) > 0) {
                node.left = rotacaoEsquerda(node.left);
            }
            node = rotacaoDireita(node);
        }

        return node;
    }

    // Informa o fator de balanceamento ( FB) do nó
    private int fatorBalanceamento(Node node) {
        if(node == null){
            return 0;
        }
        return alt(node.right) - alt(node.left);
    }

    // Realiza a busca de um ítem na árvore
    public Value get(Key key) {
        if (key == null) {
            return null;
        }

        return get(root, key);
    }

    // Busca um item na árvore
    private Value get(Node node, Key key) {
        if (node == null) {
            return null;
        }

        // Realiza uma busca binária
        int comparar = key.compareTo(node.key);
        if (comparar < 0) {
            return get(node.left, key);
        } else if (comparar > 0) {
            return get(node.right, key);
        } else {
            return node.value;
        }
    }

    // Informa se um nó está presente ou não na árvore
    public boolean contains(Key key) {
        if (key == null) {
            throw new IllegalArgumentException("Error");
        }
        return get(key) != null;
    }

    // Apaga um elemento
    public void delete(Key key) {

        if (key == null) {
            return;
        }
        root = delete(root, key);

    }

    // Apaga um elemento da árvore
    private Node delete(Node node, Key key) {
        if (node == null) {
            return null;
        }

        int comparar = key.compareTo(node.key);
        if (comparar < 0) {
            node.left = delete(node.left, key);
        } else if (comparar > 0) {
            node.right = delete(node.right, key);
        } else {
            // Verifica quantidade de filhos do nó
            if(node.left != null && node.right != null) {
                Node temporario = min(node.right);
                node.key = temporario.key;
                node.value = temporario.value;
                node.right= delete(node.right, temporario.key);
            }

            else if(node.left != null)
                return node.left;
            else
                return node.right;
        }

        node.alt = 1 + Math.max(alt(node.left), alt(node.right));
        node.tamanho = tamanho(node.left) + 1 + tamanho(node.right);

        // Verificando o balanceamento da árvore
        return balanceamento(node);
    }

    // Devolve o menor elemento de uma subárvore
    private Node min(Node node) {
        if(node == null) return null;
        if(node.left != null) return min(node.left);
        return node;
    }

    // Balanceamento da árvore
    @SuppressWarnings("unused")
    private boolean ehAVL() {
        return ehAVL(root);
    }

    // Verifica se a subárvore está balanceada
    private boolean ehAVL(Node node) {
        if (node == null) {
            return true;
        }

        int fatorBalanceamento = fatorBalanceamento(node);
        if (fatorBalanceamento < -1 || fatorBalanceamento > 1) {
            return false;
        }

        // Verificando cada nó
        return ehAVL(node.left) && ehAVL(node.right);
    }

    // Percorre toda a árvore
    public void view() {
        view(root);
    }

    private void view(Node node) {
        if(node == null) return;
        System.out.print(node.key + " ");
        System.out.println(node.value.toString());
        view(node.left);
        view(node.right);
    }

}