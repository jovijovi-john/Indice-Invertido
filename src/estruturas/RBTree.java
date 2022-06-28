package estruturas;
import items.Item;

public class RBTree<Key extends Comparable<Key>, Value> implements Item<Key, Value>{

    protected static final boolean RED = true;
    protected static final boolean BLACK = false;

    protected class Node {
        public Key key;
        public Value value;
        public Node pai, esq, dir;

        boolean cor;
        int size;

        Node(Key key, Value value, int size, boolean color, Node pai) {
            this.key = key;
            this.value = value;

            this.size = size;
            this.cor = color;
            this.pai = pai;
        }

    }

    protected Node raiz;
    // Para a verificação das propriedades da árvore
    protected Node interestNode;
    protected boolean interestColor;

    // Retorna o iterador da árvore
    public Iterator Iterator() {
        return new Iterator();
    }

    // Classe interna que fornece acesso a um iterador da árvore
    public class Iterator {
        private Node[] nodes;
        private int cur = 0;
        @SuppressWarnings("unchecked")
        private Iterator() {
            int numNodes = contaPretos(raiz) + contaVermelhos(raiz);
            this.nodes = new RBTree.Node[numNodes];
            toArray(raiz);
            this.cur = -1;
        }
        // Transforma a árvore num array para operar os nós em sequência
        // Árvore disposta em pré-ordem
        private void toArray(Node root) {
            if(root == null) return;
            nodes[cur++] = root;
            toArray(root.esq);
            toArray(root.dir);
        }
        // Retorna o item do próximo nó da árvore
        public Object[] next() {
            if(!hasNext()) {
                cur = -2;
                return null;
            }
            cur++;
            Object[] temp = new Object[2];
            temp[0] = nodes[cur].key;
            temp[1] = nodes[cur].value;
            return temp;
        }
        // Indica se o iterador chegou no fim da árvore
        public boolean hasNext() {
            if(cur == nodes.length - 1 || cur == -2) return false;
            return true;
        }
        // Reseta o iterador
        public void reset() {
            cur = -1;
        }
    }

    // Verifica se o nó é vermelho
    private boolean isRed(Node h) {
        if(h == null) return false;
        return RED && h.cor; // Se o nó for preto, retorna false
    }

    // Verifica se o nó é preto
    private boolean isBlack(Node h) {
        if(h == null) return true;
        return !(BLACK || h.cor); // Se o nó for vermelho, retorna !true
    }

    // Retorna quantos nós tem na árvore
    public int size() {
        return size(raiz);
    }

    // Retorna quantos nós tem na subárvore
    protected int size(Node no) {
        if (no == null) {
            return 0;
        }

        return no.size;
    }

    // Verifica se a árvore está vazia
    public boolean isEmpty() {
        return size(raiz) == 0;
    }

    // Rotaciona o nó à esquerda
    protected Node rotacaoEsquerda(Node no) {
        if (no == null || no.dir == null) {
            return no;
        }

        // A direita do nó sobe
        Node novaRaiz = no.dir;

        // A esquerda do nó que sobe passa para o que desce
        no.dir = novaRaiz.esq;
        if(novaRaiz.esq != null)
            novaRaiz.esq.pai = no;
        // O nó desce
        novaRaiz.esq = no;

        novaRaiz.pai = no.pai;
        no.pai = novaRaiz;

        novaRaiz.size = size(novaRaiz.esq) + 1 + size(novaRaiz.dir);
        no.size = size(no.esq) + 1 + size(no.dir);

        return novaRaiz;
    }

    private Node rotacaoDireita(Node no) {
        if (no == null || no.esq == null) {
            return no;
        }

        // A esquerda do nó sobe
        Node novaRaiz = no.esq;

        // A direita do nó que sobe passa para o que desce
        no.esq = novaRaiz.dir;
        if(novaRaiz.dir != null)
            novaRaiz.dir.pai = no;
        // O nó desce
        novaRaiz.dir = no;

        novaRaiz.pai = no.pai;
        no.pai = novaRaiz;

        novaRaiz.size = size(novaRaiz.esq) + 1 + size(novaRaiz.dir);
        no.size = size(no.esq) + 1 + size(no.dir);

        return novaRaiz;
    }

    // Troca a cor do nó
    // A nova cor do nó é o oposto do valor atual
    private void trocaCor(Node h) {
        if(h == null)
            return;
        h.cor = !h.cor;
    }


    // Insere um elemento na árvore
    public void put(Key key, Value val) {
        interestNode = null;
        // Elemento é inserido normalmente
        raiz = put(raiz, key, val, null);
        // Se há um novo nó, devem ser verificadas as propriedades da árvore
        if(interestNode != null)
            insertFixUp(interestNode);
    }

    // Insere um elemento na árvore a partir de um nó
    private Node put(Node h, Key key, Value val, Node pai)
    {
        // O nó encontrou seu lugar e é o nó de interesse
        if (h == null) {
            interestNode = new Node(key, val, 1, RED, pai);
            return interestNode;
        }

        int cmp = key.compareTo(h.key);
        // Busca binária pela posição do nó
        if (cmp < 0)
            h.esq = put(h.esq, key, val, h);
        else if (cmp > 0)
            h.dir = put(h.dir, key, val, h);
        else h.value = val;// Nó já existente atualiza seu valor

        h.size = size(h.esq) + size(h.dir) + 1;
        return h;
    }

    // Verifica e repara as propriedades da árvore
    private void insertFixUp(Node no) {

        Node avo, pai, tio;

        // Enquanto houver dois vermelhos seguidos
        while(isRed(no.pai)) {

            pai = no.pai;
            avo = no.pai.pai;
            // O pai sendo a esquerda do avô
            if(pai == avo.esq) {
                tio = avo.dir;
                // Tio vermelho, recolore avô, tio e pai
                if(isRed(tio)) {
                    trocaCor(avo);
                    trocaCor(pai);
                    trocaCor(tio);
                    no = avo; // Verifica as propriedades no avô
                } else { // Tio preto, necessárias rotações
                    // Nó está à direita do pai, é necessário dupla rotação à direita no avô
                    // Então rotaciona o pai à esquerda
                    if(no == pai.dir) {
                        if(pai.pai != null)
                            pai.pai.esq = rotacaoEsquerda(pai);
                        else
                            raiz = rotacaoEsquerda(pai);
                    }
                    // E recolore-se pai e avô
                    trocaCor(pai); //pai.cor = BLACK;
                    trocaCor(avo); //avo.cor = RED;

                    // Rotaciona o avô à direita
                    if(avo.pai == null) {
                        raiz = rotacaoDireita(avo);
                    } else {
                        if(avo == avo.pai.dir)
                            avo.pai.dir = rotacaoDireita(avo);
                        else
                            avo.pai.esq = rotacaoDireita(avo);
                    }
                }
            } else { // O pai estando à direita do avô
                tio = avo.esq;
                // Tio vermelho, recolore pai, tio e avô
                if(isRed(tio)) {
                    trocaCor(avo);
                    trocaCor(pai);
                    trocaCor(tio);
                    no = avo; // Verifica as propriedades no avô
                } else { // Tio preto, são necessárias rotações
                    // O nó está à esquerda do pai, é necessária dupla rotação à esquerda no avô
                    // Então rotaciona o pai à direita
                    if(no == pai.esq) {
                        if(pai.pai != null)
                            pai.pai.dir = rotacaoDireita(pai);
                        else
                            raiz = rotacaoDireita(pai);
                    }
                    // Recolore pai e avô
                    trocaCor(pai); //pai.cor = BLACK;
                    trocaCor(avo); //avo.cor = RED;

                    // Rotaciona o avô à esquerda
                    if(avo.pai == null) {
                        raiz = rotacaoEsquerda(avo);
                    } else {
                        if(avo == avo.pai.esq)
                            avo.pai.esq = rotacaoEsquerda(avo);
                        else
                            avo.pai.dir = rotacaoEsquerda(avo);
                    }
                }
            }

        }
        // Garante que a raiz seja preta
        raiz.cor = BLACK;

    }

    // Deleta um elemento da árvore
    public void delete(Key chave) {

        if (chave == null) {
            return;
        }
        interestNode = null;
        // Deleta normalmente o elemento
        raiz = delete(raiz, chave);
        // Se o nó deletado era preto, verificam-se as propriedades da árvore
        if(interestNode != null && interestColor == BLACK)
            deleteFixUp(interestNode);

    }

    // Deleta um elemento a partir de um nó
    private Node delete(Node no, Key chave) {

        if (no == null) {
            return null;
        }

        int compare = chave.compareTo(no.key);
        // Busca binária pelo nó
        if (compare < 0) {
            no.esq = delete(no.esq, chave);
        } else if (compare > 0) {
            no.dir = delete(no.dir, chave);
        } else {
            // Se o nó tem filho esquerdo e direito, deve ser substituído pelo menor de sua direita
            // O substituto passa a ser o nó de interesse
            if(no.esq != null && no.dir != null) {
                Node temp = min(no.dir);
                no.key = temp.key;
                no.value = temp.value;
                no.dir = delete(no.dir, temp.key);
            }
            // Se tiver apenas filho direito ou esquerdo ou nenhum, o nó é substituído por seu filho
            // Aqui o nó e cor de interesse são definidos
            else if(no.esq != null) {
                interestColor = no.cor;
                interestNode = no.esq;
                no.esq.pai = no.pai;
                return no.esq;
            }
            else {
                interestColor = no.cor;
                interestNode = no.dir;
                no.dir.pai = no.pai;
                return no.dir;
            }
        }

        no.size = size(no.esq) + 1 + size(no.dir);

        return no;
    }

    // Encontra o menor nó da árvore
    // O menor nó é o primeiro que não possuir esquerda
    private Node min(Node no) {
        if(no == null) return null;
        if(no.esq != null) return min(no.esq);
        return no;
    }

    // Verifica e repara as propriedades da árvore
    private void deleteFixUp(Node no) {

        Node irmao;

        while(no != raiz && isRed(no)) {

            if(no == no.pai.esq) {
                irmao = no.pai.dir;
                if(isRed(irmao)) {
                    trocaCor(irmao);
                    no.pai.cor = RED;
                    no.pai.pai.esq = rotacaoEsquerda(no.pai);
                    irmao = no.pai.dir;
                }
                if(isBlack(irmao.esq) && isBlack(irmao.dir)) {
                    irmao.cor = RED;
                    no = no.pai;
                } else if(isBlack(irmao.dir)) {
                    irmao.esq.cor = BLACK;
                    irmao.cor = RED;
                    irmao.pai.dir = rotacaoDireita(irmao);
                    irmao = no.pai.dir;
                }
                irmao.cor = no.pai.cor;
                no.pai.cor = BLACK;
                irmao.dir.cor = BLACK;
                no.pai.pai.esq = rotacaoEsquerda(no.pai);
                no = raiz;
            } else {
                irmao = no.pai.esq;
                if(isRed(irmao)) {
                    trocaCor(irmao);
                    no.pai.cor = RED;
                    no.pai.pai.dir = rotacaoDireita(no.pai);
                    irmao = no.pai.esq;
                }
                if(isBlack(irmao.esq) && isBlack(irmao.dir)) {
                    irmao.cor = RED;
                    no = no.pai;
                } else if(isBlack(irmao.esq)) {
                    irmao.dir.cor = BLACK;
                    irmao.cor = RED;
                    irmao.pai.esq = rotacaoEsquerda(irmao);
                    irmao = no.pai.esq;
                }
                irmao.cor = no.pai.cor;
                no.pai.cor = BLACK;
                irmao.esq.cor = BLACK;
                no.pai.pai.dir = rotacaoDireita(no.pai);
                no = raiz;
            }

        }

        no.cor = BLACK;

    }

    // Retorna o percentual de nós vermelhos na árvore usando contaVermelhos e contaPretos
    public double percentVermelhos() {
        double r, v, p;

        v = contaVermelhos(raiz);
        p = contaPretos(raiz);
        r = v/(v + p);
        return r;
    }

    // Retorna quantos nós vermelhos há na árvore
    private int contaVermelhos(Node no) {

        int cont = 0;
        if(no == null) return cont;

        cont += contaVermelhos(no.esq);
        cont += contaVermelhos(no.dir);
        if(isRed(no))
            cont++;

        return cont;
    }

    // Retorna quantos nós pretos há na árvore
    private int contaPretos(Node no) {

        int cont = 0;
        if(no == null) return cont;

        cont += contaPretos(no.esq);
        cont += contaPretos(no.dir);
        if(isBlack(no))
            cont++;

        return cont;
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
        // Chegou na folha e não achou o nó, ele é inexistente
        if (no == null) {
            return null;
        }

        // Busca binária pelo elemento
        int compare = chave.compareTo(no.key);
        if (compare < 0) {
            return get(no.esq, chave);
        } else if (compare > 0) {
            return get(no.dir, chave);
        } else {
            return no.value;
        }
    }

    // Exibe a árvore
    public void view() {
        view(raiz);
    }

    // Caminha na árvore em pré-ordem e imprime cada elemento
    private void view(Node no) {
        if(no == null) return;
        System.out.println(no.value.toString());
        view(no.esq);
        view(no.dir);
    }

}