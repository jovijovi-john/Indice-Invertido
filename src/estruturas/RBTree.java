package estruturas;
import items.Dicionario;

public class RBTree<Key extends Comparable<Key>, Value> implements Dicionario<Key, Value> {

    protected static final boolean RED = true;
    protected static final boolean BLACK = false;

    protected class Node {
        public Key chave;
        public Value valor;
        public Node pai, esq, dir;

        boolean cor;
        int tam;

        Node(Key chave, Value valor, int tam, boolean cor, Node pai) {
            this.chave = chave;
            this.valor = valor;

            this.tam = tam;
            this.cor = cor;
            this.pai = pai;
        }

    }

    protected Node raiz;
    // Realizar a verificação das propriedades da árvore Rubro negra
    protected Node interestNode;
    protected boolean interestColor;

    // Iterador da árvore
    public Iterator Iterator() {
        return new Iterator();
    }

    // Acesso ao iterador da arvore
    public class Iterator {
        private Node[] nos;
        private int cur = 0;
        @SuppressWarnings("unchecked")
        private Iterator() {
            int numNodes = qtdPretos(raiz) + qtdVermelhos(raiz);
            this.nos = new RBTree.Node[numNodes];
            toArray(raiz);
            this.cur = -1;
        }
        // Transforma a árvore num array para operar os nós em sequência
        private void toArray(Node root) {
            if(root == null) return;
            nos[cur++] = root;
            toArray(root.esq);
            toArray(root.dir);
        }
        // Retorna o elemento do proximo no
        public Object[] next() {
            if(!proximo()) {
                cur = -2;
                return null;
            }
            cur++;
            Object[] temporario = new Object[2];
            temporario[0] = nos[cur].chave;
            temporario[1] = nos[cur].valor;
            return temporario;
        }
        // Indica se há um próximo elemento ou se está no fim da árvore
        public boolean proximo() {
            if(cur == nos.length - 1 || cur == -2){
                return false;
            } else {
                return true;
            }
        }
        // zera o iterador
        public void reset() {
            cur = -1;
        }
    }

    // Verifica a cor do nó
    private boolean ehVermelho(Node n) {
        if(n == null){
            return false;
        } else {
            // Se o nó for preto, retorna o valor false
            return RED && n.cor;
        }
    }

    private boolean ehPreto(Node n) {
        if(n == null) {
            return true;
        } else{
            // Se o nó for vermelho, retorna o valor false
            return !(BLACK || n.cor);
        }

    }

    // Quantidade de nós na árvore
    public int tam() {
        return tam(raiz);
    }

    // Retorna quantos nós tem na subárvore
    protected int tam(Node no) {
        if (no == null) {
            return 0;
        }

        return no.tam;
    }

    // Verifica se há elementos na árvore
    public boolean isEmpty() {
        return tam(raiz) == 0;
    }

    // Realiza rotação à esqueda
    protected Node rotacaoEsquerda(Node no) {
        if (no == null || no.dir == null) {
            return no;
        }

        // A direita do nó sobe
        Node novaRaiz = no.dir;

        // o nó que desce recebe a direita do nó que sobe
        no.dir = novaRaiz.esq;
        if(novaRaiz.esq != null) {
            novaRaiz.esq.pai = no;
        }
        novaRaiz.esq = no;
        novaRaiz.pai = no.pai;
        no.pai = novaRaiz;
        novaRaiz.tam = tam(novaRaiz.esq) + 1 + tam(novaRaiz.dir);
        no.tam = tam(no.esq) + 1 + tam(no.dir);

        return novaRaiz;
    }

    //Realiza rotação à direita
    private Node rotacaoDireita(Node no) {
        if (no == null || no.esq == null) {
            return no;
        }

        // A esquerda do nó sobe
        Node novaRaiz = no.esq;

        // o nó que sobe recebe a direita do nó que desce
        no.esq = novaRaiz.dir;
        if(novaRaiz.dir != null) {
            novaRaiz.dir.pai = no;
        }

        novaRaiz.dir = no;
        novaRaiz.pai = no.pai;
        no.pai = novaRaiz;
        novaRaiz.tam = tam(novaRaiz.esq) + 1 + tam(novaRaiz.dir);
        no.tam = tam(no.esq) + 1 + tam(no.dir);

        return novaRaiz;
    }

    //Muda a cor do nó
    private void trocaCor(Node n) {
        if(n == null)
            return;
        n.cor = !n.cor;
    }


    // Insere um elemento na árvore
    public void put(Key key, Value val) {
        interestNode = null;
        raiz = put(raiz, key, val, null);
//Verificar as propriedades da árvore, caso necessário
        if(interestNode != null)
            insertFixUp(interestNode);
    }

    // Adiciona um elemento na árvore
    private Node put(Node n, Key chave, Value valor, Node pai)
    {

        if (n == null) {
            interestNode = new Node(chave, valor, 1, RED, pai);
            return interestNode;
        }

        int cmp = chave.compareTo(n.chave);
        // Busca binária pela posição do nó
        if (cmp < 0)
            n.esq = put(n.esq, chave, valor, n);
        else if (cmp > 0)
            n.dir = put(n.dir, chave, valor, n);
        else n.valor = valor;

        n.tam = tam(n.esq) + tam(n.dir) + 1;
        return n;
    }

    // Verifica e repara as propriedades da árvore
    private void insertFixUp(Node no) {

        Node avo, pai, tio;

        //Se tiver dois nós vermelhos seguidos
        while(ehVermelho(no.pai)) {

            pai = no.pai;
            avo = no.pai.pai;
            // pai == filho esquerdo do avô
            if(pai == avo.esq) {
                tio = avo.dir;
                //Se o tio for vermelho
                if(ehVermelho(tio)) {
                    trocaCor(avo);
                    trocaCor(pai);
                    trocaCor(tio);
                    no = avo; //
                } else {
                    // Nó está à direita do pai == dupla rotação à direita no avô
                    // Rotacionar o pai à esquerda
                    if(no == pai.dir) {
                        if(pai.pai != null)
                            pai.pai.esq = rotacaoEsquerda(pai);
                        else
                            raiz = rotacaoEsquerda(pai);
                    }
                    trocaCor(pai); //pai.cor = BLACK;
                    trocaCor(avo); //avo.cor = RED;

                    // Rotacionar o avô à direita
                    if(avo.pai == null) {
                        raiz = rotacaoDireita(avo);
                    } else {
                        if(avo == avo.pai.dir)
                            avo.pai.dir = rotacaoDireita(avo);
                        else
                            avo.pai.esq = rotacaoDireita(avo);
                    }
                }
            } else { // O pai é filho esquerdo do avô
                tio = avo.esq;

                if(ehVermelho(tio)) {
                    trocaCor(avo);
                    trocaCor(pai);
                    trocaCor(tio);
                    no = avo;
                } else { //
                    // O nó está à esquerda do pai ==necessária dupla rotação à esquerda no avô
                    if(no == pai.esq) {
                        if(pai.pai != null)
                            pai.pai.dir = rotacaoDireita(pai);
                        else
                            raiz = rotacaoDireita(pai);
                    }
                    trocaCor(pai); //pai.cor = BLACK;
                    trocaCor(avo); //avo.cor = RED;

                    // Rotacionar o avô à esquerda
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
        // Protege a propriedade de que a raiz deve ser preta
        raiz.cor = BLACK;

    }

    // apaga um elemento na árvore
    public void delete(Key chave) {

        if (chave == null) {
            return;
        }
        interestNode = null;

        raiz = delete(raiz, chave);
        // Verificar as propriedades da tabela
        if(interestNode != null && interestColor == BLACK)
            deleteFixUp(interestNode);

    }

    // Deleta um elemento a partir de um nó
    private Node delete(Node no, Key key) {

        if (no == null) {
            return null;
        }

        int compare = key.compareTo(no.chave);
        // Busca binária pelo nó
        if (compare < 0) {
            no.esq = delete(no.esq, key);
        } else if (compare > 0) {
            no.dir = delete(no.dir, key);
        } else {
            // Verifica se o nó tem filho esquerdo e direito
            if(no.esq != null && no.dir != null) {
                Node temp = min(no.dir);
                no.chave = temp.chave;
                no.valor = temp.valor;
                no.dir = delete(no.dir, temp.chave);
            }
            // Verificando propriedades

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

        no.tam = tam(no.esq) + 1 + tam(no.dir);

        return no;
    }

    // Retorna o menor nó da árvore
    private Node min(Node no) {
        if(no == null) return null;
        if(no.esq != null) return min(no.esq);
        return no;
    }

    // Verifica as propriedades da árvore
    private void deleteFixUp(Node no) {

        Node irmao;

        while(no != raiz && ehVermelho(no)) {

            if(no == no.pai.esq) {
                irmao = no.pai.dir;
                if(ehVermelho(irmao)) {
                    trocaCor(irmao);
                    no.pai.cor = RED;
                    no.pai.pai.esq = rotacaoEsquerda(no.pai);
                    irmao = no.pai.dir;
                }
                //verifica se os irmãos são pretos
                if(ehPreto(irmao.esq) && ehPreto(irmao.dir)) {
                    irmao.cor = RED;
                    no = no.pai;
                } else if(ehPreto(irmao.dir)) {
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
                if(ehVermelho(irmao)) {
                    trocaCor(irmao);
                    no.pai.cor = RED;
                    no.pai.pai.dir = rotacaoDireita(no.pai);
                    irmao = no.pai.esq;
                }
                if(ehPreto(irmao.esq) && ehPreto(irmao.dir)) {
                    irmao.cor = RED;
                    no = no.pai;
                } else if(ehPreto(irmao.esq)) {
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

    // Informa a qtd de nós vermelhos na árvore
    public double percentVermelhos() {
        double r, v, p;

        v = qtdVermelhos(raiz);
        p = qtdPretos(raiz);
        r = v/(v + p);
        return r;
    }

    // Retorna quantos nós vermelhos há na árvore
    private int qtdVermelhos(Node no) {

        int cont = 0;
        if(no == null) return cont;

        cont +=  qtdVermelhos(no.esq);
        cont +=  qtdVermelhos(no.dir);
        if(ehVermelho(no)) {
            cont++;
        }

        return cont;
    }

    // Retorna quantos nós pretos há na árvore
    private int qtdPretos(Node no) {

        int cont = 0;
        if(no == null) {
            return cont;
        }

        cont += qtdPretos(no.esq);
        cont += qtdPretos(no.dir);
        if(ehPreto(no)) {
            cont++;
        }

        return cont;
    }

    // Busca um elemento na árvore
    public Value get(Key chave) {
        if (chave == null) {
            return null;
        }

        return get(raiz, chave);
    }

    // Mostra a árvore
    public void view() {
        view(raiz);
    }


    // percorre na árvore e eibe os elementos
    private void view(Node no) {
        if(no == null) return;
        System.out.println(no.valor.toString());
        view(no.esq);
        view(no.dir);
    }

    // Busca um elemento na árvore
    private Value get(Node no, Key chave) {
        if (no == null) {
            return null;
        }

        // Busca binária pelo elemento
        int compare = chave.compareTo(no.chave);
        if (compare < 0) {
            return get(no.esq, chave);
        } else if (compare > 0) {
            return get(no.dir, chave);
        } else {
            return no.valor;
        }
    }



}