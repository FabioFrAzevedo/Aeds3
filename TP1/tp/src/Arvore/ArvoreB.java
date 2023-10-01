package Arvore;

import java.io.RandomAccessFile;
import java.util.Arrays;
import java.util.Comparator;


public class ArvoreB {
    

    // ATRIBUTOS

    private long endRaiz;
    private int qtdPaginas;
    private int maxElementos;

    private Pagina pagina;
    private RandomAccessFile arvoreB;


    // GETTERS E SETTERS

    public long getEndRaiz() {
        return endRaiz;
    }
    public void setEndRaiz(long endRaiz) {
        this.endRaiz = endRaiz;
    }
    public Pagina getPagina() {
        return pagina;
    }
    public void setPagina(Pagina pagina) {
        this.pagina = pagina;
    }

    // CONSTRUTOR

    public ArvoreB() {
        this.endRaiz = -1;
        this.qtdPaginas = 0;
        this.maxElementos = 7;

        this.pagina = new Pagina();

        try {
            this.arvoreB = new RandomAccessFile("arvoreB.bin", "rw"); // cria o arquivo

            // se o arquivo estiver vazio, cria a raiz
            if (arvoreB.length() == 0) {
                Pagina raiz = new Pagina();
                raiz.setFolha(true);
                raiz.setQtdElementos(0);
                raiz.setElementos(new Elemento[maxElementos]);
                raiz.setFilhos(new long[maxElementos+1]);

                // escreve a raiz no arquivo
                arvoreB.seek(0);
                arvoreB.writeLong(12); // endRaiz
                arvoreB.writeInt(1); // qtdPaginas
                arvoreB.writeBoolean(raiz.getFolha());
                arvoreB.writeInt(raiz.getQtdElementos());
                for (int i = 0; i < maxElementos; i++) {
                    arvoreB.writeLong(-1); // filho
                    arvoreB.writeInt(-1); // idRegistro
                    arvoreB.writeLong(-1); // endRegistro
                }
                arvoreB.writeLong(-1); // ultimo filho
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao criar o arquivo de arvoreB: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // CLASSE
    public class Elemento {
        private int idRegistro;
        private long endRegistro;
        
        public Elemento() {
            this.idRegistro = -1;
            this.endRegistro = -1;
        }

        public Elemento(int idRegistro, long endRegistro) {
            this.idRegistro = idRegistro;
            this.endRegistro = endRegistro;
        }

        public int getIdRegistro() {
            return idRegistro;
        }
        public void setIdRegistro(int idRegistro) {
            this.idRegistro = idRegistro;
        }
        public long getEndRegistro() 
        {return endRegistro;
        }
        public void setEndRegistro(long endRegistro) {
            this.endRegistro = endRegistro;
        }
    }

    public class Pagina {
        private boolean isFolha;
        private int qtdElementos;
        private Elemento[] elementos;
        private long[] filhos;

        public Pagina() {
            this.isFolha = true;
            this.qtdElementos = 0;
            this.elementos = new Elemento[maxElementos];
            for (int i = 0; i < elementos.length; i++) {
                elementos[i] = new Elemento();
            }
            this.filhos = new long[maxElementos+1];
            for (int i = 0; i < filhos.length; i++) {
                filhos[i] = -1;
            }
        }

        public Pagina(boolean isFolha, int qtdElementos) {
            this.isFolha = isFolha;
            this.qtdElementos = qtdElementos;
            this.elementos = new Elemento[maxElementos];
            for (int i = 0; i < elementos.length; i++) {
                elementos[i] = new Elemento();
            }
            this.filhos = new long[maxElementos+1];
            for (int i = 0; i < filhos.length; i++) {
                filhos[i] = -1;
            }
        }

        public void excluirElemento(int i) {
            this.elementos[i] = new Elemento();
            this.filhos[i+1] = -1;
        }

        public boolean getFolha() {
            return isFolha;
        }
        public void setFolha(boolean isFolha) {
            this.isFolha = isFolha;
        }
        public int getQtdElementos() {
            return qtdElementos;
        }
        public void setQtdElementos(int qtdElementos) {
            this.qtdElementos = qtdElementos;
        }
        public Elemento[] getElementos() {
            return elementos;
        }
        public void setElementos(Elemento[] elementos) {
            this.elementos = elementos;
        }
        public long[] getFilhos() {
            return filhos;
        }
        public void setFilhos(long[] filhos) {
            this.filhos = filhos;
        }
    }

    // METODOS

    public long readEndRaiz() {
        try {
            arvoreB.seek(0);
            return arvoreB.readLong();
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao ler o endereco da raiz: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public int readQtdPaginas() {
        try {
            arvoreB.seek(8);
            return arvoreB.readInt();
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao ler o a quantidade de paginas: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public long findPagina(long endPagina, int id) {
        try {
            int qtdElementos = -1;
            int idLido = -1;
            long endFilho = -1;
            boolean isFolha = false;

            arvoreB.seek(endPagina); // vai para o inicio da pagina (comeca na raiz)
            isFolha = arvoreB.readBoolean(); 
            qtdElementos = arvoreB.readInt(); 

            if (!isFolha) {
                if (qtdElementos > 1) {
                    arvoreB.skipBytes(((qtdElementos - 1) * 20) + 8); // pula para o inicio do ultimo elemento
                } else {
                    arvoreB.skipBytes(8); // pula o primeiro filho
                }

                for (int i = qtdElementos; i >= 1; i++) {
                    if (i != 1) {
                        idLido = arvoreB.readInt(); 
                        if (id > idLido) { 
                            arvoreB.skipBytes(8); 
                            endFilho = arvoreB.readLong(); 
                            return findPagina(endFilho, id); 
                        } else {
                            arvoreB.seek(arvoreB.getFilePointer() - 24); 
                        }
                    } else { 
                        idLido = arvoreB.readInt(); 
                        if (id > idLido) { 
                            arvoreB.skipBytes(8); 
                            endFilho = arvoreB.readLong(); 
                            return findPagina(endFilho, id); 
                        } else {
                            arvoreB.seek(arvoreB.getFilePointer() - 12); 
                            endFilho = arvoreB.readLong(); 
                            return findPagina(endFilho, id); 
                        }
                    }
                }

            } else { 
                return endPagina; 
            }
            return -1;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao procurar pagina: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public long findRegistro(long pagina, int id) {
        try {
            int qtdElementos = -1;
            int idLido = -1;
            long endRegistro = -1;

            arvoreB.seek(pagina); 
            arvoreB.skipBytes(1); 
            qtdElementos = arvoreB.readInt(); 
            arvoreB.skipBytes(8); 

            for (int i = 0; i < qtdElementos; i++) {
                idLido = arvoreB.readInt(); 
                if (id == idLido) { 
                    endRegistro = arvoreB.readLong(); 
                    return endRegistro; 
                } else {
                    arvoreB.skipBytes(16); 
                }
            }
            return -1; // se nao encontrar o registro retorna -1
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao procurar registro na arvore b: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public long writePagina(Pagina pagina) {
        long endereco = -1;
        try {
            arvoreB.seek(arvoreB.length()); 
            endereco = arvoreB.getFilePointer(); 
            arvoreB.writeBoolean(pagina.getFolha()); 
            arvoreB.writeInt(pagina.getQtdElementos()); 

            for (int i = 0; i < maxElementos; i++) { // escreve os elementos
                arvoreB.writeLong(pagina.getFilhos()[i]);
                arvoreB.writeInt(pagina.getElementos()[i].getIdRegistro());
                arvoreB.writeLong(pagina.getElementos()[i].getEndRegistro());
            }
            arvoreB.writeLong(pagina.getFilhos()[maxElementos]); // escreve o ultimo filho
            return endereco;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao escrever pagina: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public Pagina readPagina(long endPagina) {
        try {
            Pagina pagina = new Pagina();
            long[] filhos = new long[maxElementos+1];
            arvoreB.seek(endPagina); 
            pagina.setFolha(arvoreB.readBoolean()); 
            pagina.setQtdElementos(arvoreB.readInt()); 
            for (int i = 0; i < maxElementos; i++) {
                filhos[i] = arvoreB.readLong();
                pagina.getElementos()[i].setIdRegistro(arvoreB.readInt()); 
                pagina.getElementos()[i].setEndRegistro(arvoreB.readLong()); 
            }
            filhos[7] = arvoreB.readLong(); // le o ultimo filho
            pagina.setFilhos(filhos);
            return pagina;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao ler pagina: " + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    public long shiftElementos(long endPagina, int id) {
        try {
            arvoreB.seek(endPagina); 
            arvoreB.skipBytes(1); 
            int qtdElementos = arvoreB.readInt(); 
            int qtdArredados = 0; 
            int index = 0; 
            Elemento[] vetor = new Elemento[maxElementos]; 
            Elemento aux = new Elemento(); 
            long espacoVazio = -1;

            arvoreB.skipBytes(8); 
            for (int i = 0; i < qtdElementos; i++) {
                if (arvoreB.readInt() == id) { 
                    arvoreB.seek(arvoreB.getFilePointer() - 4); 
                    for (int j = i; j < qtdElementos; j++) { 
                        aux.setIdRegistro(arvoreB.readInt()); 
                        aux.setEndRegistro(arvoreB.readLong()); 
                        vetor[index] = aux; 
                        index++;
                        qtdArredados++; 
                        arvoreB.skipBytes(8); 
                    }
                    i = qtdElementos; 
                } else {
                    arvoreB.skipBytes(16); 
                }
            }
            arvoreB.seek(arvoreB.getFilePointer() - (qtdArredados * 20)); // volta para o inicio dos elementos arredados
            espacoVazio = arvoreB.getFilePointer(); // pega o endereco do espaco vazio
            arvoreB.writeInt(-1); // libera espaco do elemento a ser arredado
            arvoreB.writeLong(-1); // libera espaco do elemento a ser arredado
            arvoreB.skipBytes(8); // pula o filho
            for (int i = 0; i < qtdArredados; i++) { // escreve os elementos arredados
                arvoreB.writeInt(vetor[i].getIdRegistro());
                arvoreB.writeLong(vetor[i].getEndRegistro());
                arvoreB.skipBytes(8); // pula o filho
            }
            return espacoVazio;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao arredar elementos: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public long inserir(long endPagina, Pagina pagina, int id, long posicao) {
        try {
            arvoreB.seek(0); 
            long endRaiz = arvoreB.readLong(); 
            long[] vetorFilhos = new long[maxElementos+1]; 
            long endMae = -1;
            long endInsercao = -1; 
            Elemento[] vetor = new Elemento[maxElementos+1]; 
            Elemento elemento = new Elemento(id, posicao); 
            int idAux = -1; 
            int posicaoNaPg = -1; // posicao do elemento na pagina

            for (int i = 0; i < maxElementos; i++) { // copia os elementos da pagina para o vetor
                vetor[i] = pagina.getElementos()[i];
                vetorFilhos[i] = pagina.getFilhos()[i];
            }
            vetor[7] = elemento; 
            vetorFilhos[7] = pagina.getFilhos()[7]; 

            // ordena o vetor por idRegistro
            Arrays.sort(vetor,new Comparator<Elemento>(){  
                @Override  
                public int compare(Elemento e1, Elemento e2){  
                    return e1.getIdRegistro() - e2.getIdRegistro();  
            }  
            } );

            // Mover todos os -1 para o final do vetor
            int j = 0;
            for (int i = 0; i < vetor.length; i++) {
                if (vetor[i].getIdRegistro() != -1) {
                    Elemento temp = vetor[j];
                    vetor[j] = vetor[i];
                    vetor[i] = temp;
                    j++;
                }
            }

            for (int i = 0; i < vetor.length; i++) {
                if (vetor[i].getIdRegistro() == id) { 
                    posicaoNaPg = i;
                    i = vetor.length; 
                }
            }

            arvoreB.seek(endPagina + 1); 
            int qtdElementos = arvoreB.readInt(); 

            if (qtdElementos == 0) {
                arvoreB.skipBytes(8); 
                arvoreB.writeInt(id); 
                arvoreB.writeLong(posicao); 
                arvoreB.seek(endPagina + 1); 
                arvoreB.writeInt(1); 
                return endPagina; // retorna o endereco da pagina na qual o elemento foi inserido
            }

            arvoreB.skipBytes(8); 
            if (qtdElementos != maxElementos) { 
                arvoreB.skipBytes(posicaoNaPg * 20); 
                idAux = arvoreB.readInt(); 
                if (idAux != -1) {
                    endInsercao = shiftElementos(endPagina, idAux); 
                } else {
                    endInsercao = arvoreB.getFilePointer() - 4; // pega o endereco de insercao
                }

                arvoreB.seek(endInsercao); 
                arvoreB.writeInt(id); 
                arvoreB.writeLong(posicao); 
                arvoreB.seek(endPagina + 1); 
                arvoreB.writeInt(qtdElementos + 1); 
                return endPagina; // retorna o endereco da pagina na qual o elemento foi inserido
            } else {
                endMae = findParent(endRaiz, pagina, endPagina);
                if (endMae == -1) { 
                    endInsercao = splitPagina(endPagina, readPagina(endPagina), true, vetor, vetorFilhos, id, posicao);
                } else { 
                    Elemento pivot = pagina.getElementos()[3]; 
                    inserir(endMae, readPagina(endMae), pivot.getIdRegistro(), pivot.getEndRegistro()); // insere o pivot na pagina mae
                    endInsercao = splitPagina(endPagina, pagina, false, vetor, vetorFilhos, id, posicao); // faz split na pagina cheia
                    //inserir(endPagina, pagina, id, posicao); // insere o elemento na pagina
                }
                return endInsercao;
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao inserir: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public long splitPagina(long endPagina, Pagina pagina, boolean isRaiz, Elemento[] vetor, long[] vetorFilhos, int idReg, long endReg) {
        Pagina novaPagina = new Pagina();
        Elemento pivot = vetor[3]; // pega o menor elemento do meio
        int id = -1;
        long endNovaPagina = -1;
        long endRaiz = -1;
        long endMae = -1;

        try {
            for (int i = 4; i < maxElementos; i++) {
                novaPagina.elementos[i - 4] = vetor[i]; 
                novaPagina.filhos[i-4] = vetorFilhos[i]; 
            }
            if (vetorFilhos[7] != -1) {
                novaPagina.filhos[3] = vetorFilhos[7]; 
            }
            
            novaPagina.setQtdElementos(3);
            endNovaPagina = writePagina(novaPagina); 
            this.qtdPaginas++; 

            if (isRaiz) {
                Pagina novaRaiz = new Pagina(false, 1);
                novaRaiz.elementos[0] = pivot; 
                novaRaiz.filhos[0] = endPagina; 
                novaRaiz.filhos[1] = endNovaPagina; 
                endRaiz = writePagina(novaRaiz); 
                arvoreB.seek(0); 
                arvoreB.writeLong(endRaiz); 
                this.qtdPaginas++; 

                // apaga o pivot e elementos a direita do pivot da pagina splitada
                arvoreB.seek(endPagina + 73); // vai para o pivot
                for (int i = 3; i < maxElementos; i++) {
                    arvoreB.writeInt(-1); 
                    arvoreB.writeLong(-1); 
                    arvoreB.writeLong(-1); 
                    pagina.excluirElemento(i); 
                    pagina.setQtdElementos(pagina.getQtdElementos()-1); 
                }
                arvoreB.seek(endPagina + 1); 
                arvoreB.writeInt(pagina.getQtdElementos()); 
                inserir(endNovaPagina, novaPagina, idReg, endReg); 

                for (int i = 0; i < vetorFilhos.length; i++) {
                    if (vetorFilhos[i] != -1) {
                        arvoreB.seek(endNovaPagina); 
                        arvoreB.writeBoolean(false); 
                        i = vetorFilhos.length; 
                    }
                }
            } else {
                arvoreB.seek(0);
                endRaiz = arvoreB.readLong();
                endMae = findParent(endRaiz, pagina, endPagina); 
                arvoreB.seek(endMae); 
                arvoreB.skipBytes(1); 
                int qtdElementos = arvoreB.readInt(); 
                for (int i = 0; i < qtdElementos; i++) {
                    arvoreB.skipBytes(8); 
                    id = arvoreB.readInt(); 
                    if (id > novaPagina.getElementos()[0].getIdRegistro()) { 
                        arvoreB.seek(arvoreB.getFilePointer() - 12); 
                        arvoreB.writeLong(endNovaPagina); 
                        i = qtdElementos; 
                    } else if (i == (qtdElementos - 1)) {
                        if (id < novaPagina.getElementos()[0].getIdRegistro()) { 
                            arvoreB.skipBytes(8); 
                            arvoreB.writeLong(endNovaPagina); 
                            i = qtdElementos; 
                        }
                    } else {
                        arvoreB.skipBytes(8); 
                    }
                }

                // apaga o pivot e elementos a direita do pivot da pagina splitada
                arvoreB.seek(endPagina + 73); 
                for (int i = 3; i < maxElementos; i++) {
                    arvoreB.writeInt(-1); 
                    arvoreB.writeLong(-1); 
                    arvoreB.writeLong(-1); 
                    pagina.excluirElemento(i); 
                    pagina.setQtdElementos(pagina.getQtdElementos()-1); 
                }
                arvoreB.seek(endPagina + 1); 
                arvoreB.writeInt(pagina.getQtdElementos()); 
                inserir(endNovaPagina, novaPagina, idReg, endReg); 

                for (int i = 0; i < vetorFilhos.length; i++) {
                    if (vetorFilhos[i] != -1) {
                        arvoreB.seek(endNovaPagina); 
                        arvoreB.writeBoolean(false); 
                        i = vetorFilhos.length; 
                    }
                }
            }
            arvoreB.seek(8); 
            arvoreB.writeInt(this.qtdPaginas); 
            return endNovaPagina;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao inserir em pagina cheia: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
    }

    public long findParent(long pgAtual, Pagina filho, long endFilho) {
        try {
            arvoreB.seek(pgAtual); 
            arvoreB.skipBytes(1); 
            int qtdElementos = arvoreB.readInt(); 
            arvoreB.seek(arvoreB.getFilePointer() + ((qtdElementos - 1) * 20) + 8); 

            while (arvoreB.getFilePointer() < arvoreB.length()) { // while !EOF
                for (int i = qtdElementos; i >= 0; i--) {
                    if (pgAtual == endFilho) {
                        return -1;
                    }
                    if (filho.getElementos()[0].getIdRegistro() > arvoreB.readInt()) {
                        arvoreB.skipBytes(8); 
                        if (arvoreB.readLong() == endFilho) { 
                            return pgAtual; 
                        } else {
                            arvoreB.seek(arvoreB.getFilePointer() - 8); 
                            pgAtual = arvoreB.readLong(); 
                            return findParent(pgAtual, filho, endFilho); 
                        }
                    } else if (i == 0) {
                        arvoreB.seek(arvoreB.getFilePointer() - 4); 
                        if (filho.getElementos()[0].getIdRegistro() < arvoreB.readInt()) {
                            arvoreB.skipBytes(8); 
                            if (arvoreB.readLong() == endFilho) { 
                                return pgAtual; 
                            } else {
                                arvoreB.seek(arvoreB.getFilePointer() - 28); 
                                pgAtual = arvoreB.readLong(); 
                                return findParent(pgAtual, filho, endFilho); 
                            }
                        }
                    } else {
                        arvoreB.seek(arvoreB.getFilePointer() - 24); 
                    }
                }
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao encontrar pagina mae: " + e.getMessage());
            e.printStackTrace();
            return -1;
        }
        return -1;
    }
}
