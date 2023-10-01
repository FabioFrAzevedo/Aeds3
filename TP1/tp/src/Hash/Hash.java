package Hash;


import java.io.RandomAccessFile;

public class Hash {

    //Atributos 

    // Ponteiros
    private int pGlobal;
    private int qtdBuckets; // qtd de buckets escritos no arquivo
    private long endBucket;
    // Buckets
    private int pLocal;
    private int qtdRegistros; // qtd de registros escritos no bucket
    private int maxRegistros = 440; // 440 registros por bucket
    // Registros
    private int tamanhoReg = 12; // 12 bytes (1 int e 1 long)
    private int idRegistro;
    private long endRegistro;

    // Arquivos
    public RandomAccessFile hashPointers;
    public RandomAccessFile hashBuckets;

    // Getters e Setters

    // Pointers
    public int getpGlobal() {
        return pGlobal;
    }

    public void setpGlobal(int pGlobal) {
        this.pGlobal = pGlobal;
    }

    public int getQtdBuckets() {
        return qtdBuckets;
    }

    public void setQtdBuckets(int qtdBuckets) {
        this.qtdBuckets = qtdBuckets;
    }

    public long getEndBucket() {
        return endBucket;
    }

    public void setEndBucket(long endBucket) {
        this.endBucket = endBucket;
    }

    // Buckets
    public int getpLocal() {
        return pLocal;
    }

    public void setpLocal(int pLocal) {
        this.pLocal = pLocal;
    }

    public int getQtdRegistros() {
        return qtdRegistros;
    }

    public void setQtdRegistros(int qtdRegistros) {
        this.qtdRegistros = qtdRegistros;
    }

    public int getMaxRegistros() {
        return maxRegistros;
    }

    // Registros
    public int getTamanhoReg() {
        return tamanhoReg;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public long getEndRegistro() {
        return endRegistro;
    }

    public void setEndRegistro(long endRegistro) {
        this.endRegistro = endRegistro;
    }

    // Construtor-

    public Hash() {
        // Pointers
        this.pGlobal = 1;
        this.qtdBuckets = 0; // qtd de buckets escritos no arquivo
        this.endBucket = -1;
        // Buckets
        this.pLocal = 1;
        this.qtdRegistros = 0; // qtd de registros escritos no bucket
        this.maxRegistros = 440; // 440 registros por bucket
        // Registros
        this.tamanhoReg = 12; // 12 bytes (1 int e 1 long)
        this.idRegistro = -1;
        this.endRegistro = -1;

        try {
            this.hashPointers = new RandomAccessFile("hashPointers.bin", "rw"); // cria o arquivo de indice
            this.hashBuckets = new RandomAccessFile("hashBuckets.bin", "rw"); // cria o arquivo de buckets

            if (hashBuckets.length() == 0 && hashPointers.length() == 0) { // se os arquivos estiverem vazios
                hashPointers.seek(0); // posiciona o ponteiro no inicio do arquivo
                hashBuckets.seek(0); // posiciona o ponteiro no inicio do arquivo

                hashPointers.writeInt(1); // escreve o pGlobal (inicia em 1)
                hashPointers.writeInt(2); // escreve a qtd de buckets (inicia em 2)

                for (int i = 0; i < 2; i++) { // cria os 2 primeiros buckets
                    hashPointers.writeLong(hashBuckets.getFilePointer()); // escreve o endBucket
                    hashBuckets.writeInt(1); // escreve o pLocal (inicia em 1)
                    hashBuckets.writeInt(0); // escreve a qtd de registros (bucket inicia vazio)
                    for (int j = 0; j < maxRegistros; j++) { // escreve os registros vazios
                        hashBuckets.writeInt(-1); // escreve o idRegistro
                        hashBuckets.writeLong(-1); // escreve o endRegistro
                    }
                }
                setQtdBuckets(2); // atualiza a qtd de buckets escritos no arquivo
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao criar os rafs: " + e.getMessage());
            e.printStackTrace();
        }
    }

    //  Metodos

    public void readQbAndPg() { // le o pGlobal e a qtd de buckets do arquivo de pointers
        try {
            hashPointers.seek(0); // posiciona o ponteiro no inicio do arquivo
            setpGlobal(hashPointers.readInt()); // le a qtd de buckets
            setQtdBuckets(hashPointers.readInt()); // le a qtd de buckets
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao ler pGlobal e qtdBuckets! " + e.getMessage());
            e.printStackTrace();
        }
    }

    public long funcaoHash(int id, int p) {
        int qualBucket = id % ((int) Math.pow(2, p));
        long posBucket = -1; // posicao do bucket no arquivo

        try {
            hashPointers.seek(8); // posiciona o ponteiro no inicio do arquivo, pulando o pGlobal e a qtd de buckets
            hashPointers.skipBytes(qualBucket * 8); // pula para o endereco do bucket certo
            posBucket = hashPointers.readLong(); // le o endBucket

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao calcular funcao hash! " + e.getMessage());
            e.printStackTrace();
        }

        return posBucket;
    }

    public long find(int id) {
        long posBucket = funcaoHash(id, getpGlobal());

        try {

            hashBuckets.seek(posBucket + 4); // posiciona o ponteiro no inicio do bucket certo, pulando o pLocal
            setQtdRegistros(hashBuckets.readInt()); // le a qtd de registros do bucket

            for (int i = 0; i < getQtdRegistros(); i++) {
                if (hashBuckets.readInt() == id) { // se o id do registro for igual ao id procurado
                    setEndRegistro(hashBuckets.readLong()); // le o endRegistro
                    return getEndRegistro();
                } else {
                    hashBuckets.skipBytes(8); // pula para o proximo registro
                }
            }
            System.out.println();
            System.out.println("Registro nao encontrado!");
            System.out.println("______________________________________"); // formatacao
            System.out.println();

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao procurar registro: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    public boolean excluir(int id, long endBucket) {

        try {
            hashBuckets.seek(endBucket + 4); // posiciona o ponteiro no inicio do bucket certo, pulando o pLocal
            setQtdRegistros(hashBuckets.readInt()); // le a qtd de registros do bucket

            for (int i = 0; i < 440; i++) { // percorre o bucket inteiro
                if (hashBuckets.readInt() == id) { // se o id do registro for igual ao id do registro a excluir
                    hashBuckets.seek(hashBuckets.getFilePointer() - 4); // volta 4 bytes (idRegistro)
                    hashBuckets.writeInt(-1); // escreve -1 no idRegistro
                    hashBuckets.writeLong(-1); // escreve -1 no endRegistro
                    hashBuckets.seek(endBucket + 4); // posiciona o ponteiro no inicio do bucket certo, pulando o pLocal
                    hashBuckets.writeInt(getQtdRegistros() - 1); // atualiza a qtd de registros do bucket
                    return true;
                } else {
                    hashBuckets.skipBytes(8); // pula para o proximo registro
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao excluir registro: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public long inserir(int id, long endRegistro) {
        long endBucketCheio = -1;
        long aux = -1; // auxiliar para armazenar valores temporariamente

        try {
            if (hashBuckets.length() != 0 && hashPointers.length() != 0) { // se os arquivos ja existirem

                hashPointers.seek(0); // posiciona o ponteiro no inicio do arquivo
                setpGlobal(hashPointers.readInt()); // le o pGlobal
                int qualBucket = id % ((int) Math.pow(2, getpGlobal()));
                hashPointers.skipBytes(4); // pula a qtdBuckets
                hashPointers.skipBytes(qualBucket * 8); // pula para o endereco do bucket certo
                setEndBucket(hashPointers.readLong()); // le o endBucket

                hashBuckets.seek(getEndBucket()); // posiciona o ponteiro no bucket certo
                setpLocal(hashBuckets.readInt()); // le o pLocal
                setQtdRegistros(hashBuckets.readInt()); // le a qtd de registros

                // Caso 1: bucket possui espaco livre
                if (getQtdRegistros() < maxRegistros) {
                    hashBuckets.seek(getEndBucket() + 8); // posiciona o ponteiro no inicio do bucket, pulando o pLocal e a qtdRegistros
                    for (int i = 0; i < 440; i++) { // percorre o bucket
                        if (hashBuckets.readInt() == -1) {
                            hashBuckets.seek(hashBuckets.getFilePointer() - 4); // volta 4 bytes (idRegistro)
                            hashBuckets.writeInt(id); // escreve o idRegistro
                            hashBuckets.writeLong(endRegistro); // escreve o endRegistro
                            hashBuckets.seek(getEndBucket() + 4); // posiciona o ponteiro no inicio do bucket, pulando o pLocal
                            hashBuckets.writeInt(getQtdRegistros() + 1); // atualiza a qtd de registros
                            i = 440; // sai do for
                        } else {
                            hashBuckets.skipBytes(8); // pula para o proximo registro
                        }
                    }

                    return getEndBucket();
                }

                // Caso 2: bucket sem espaco e pLocal < pGlobal
                else if ((getQtdRegistros() == maxRegistros) && (getpLocal() < getpGlobal())) {
                    // atualiza o pLocal
                    hashBuckets.seek(getEndBucket()); // posiciona o ponteiro no inicio do bucket
                    setpLocal(getpGlobal()); // atualiza o pLocal
                    hashBuckets.writeInt(getpLocal()); // atualiza o pLocal no arquivo

                    // cria bucket novo
                    hashBuckets.seek(hashBuckets.length()); // posiciona o ponteiro no final do arquivo
                    endBucketCheio = getEndBucket(); // armazena o endBucket do bucket cheio
                    criaBucket(); // cria um novo bucket

                    // atualiza o ponteiro do novo bucket
                    hashPointers.seek(8); // posiciona o ponteiro no inicio do arquivo, pulando o pGlobal e a qtdBuckets
                    for (int i = 0; i < (hashPointers.length() - 8); i++) {
                        if (hashPointers.readLong() == endBucketCheio) { // se o endBucket for igual ao endBucket do bucket que foi dividido
                            i = (int) (hashPointers.length() - 8); // sai do loop
                        }
                    }
                    while (hashPointers.getFilePointer() < hashPointers.length()) { // percorre o arquivo de pointers
                        if (hashPointers.readLong() == endBucketCheio) { // se o endBucket for igual ao endBucket do bucket que foi dividido
                            hashPointers.seek(hashPointers.getFilePointer() - 8); // volta o ponteiro para o endBucket
                            hashPointers.writeLong(getEndBucket()); // move o ponteiro para o novo endBucket
                            break;
                        }
                    }

                    // redistribuir os registros do bucket cheio
                    aux = endBucketCheio + 8;
                    for (int i = 0; i < 440; i++) { // percorre o bucket
                        hashBuckets.seek(aux);
                        if (hashBuckets.readInt() != -1) {
                            hashBuckets.seek(hashBuckets.getFilePointer() - 4); // volta 4 bytes (idRegistro)
                            setIdRegistro(hashBuckets.readInt()); // le o idRegistro
                            setEndRegistro(hashBuckets.readLong()); // le o endRegistro
                            aux = hashBuckets.getFilePointer();
                            excluir(getIdRegistro(), endBucketCheio); // exclui o registro do bucket cheio
                            inserir(getIdRegistro(), getEndRegistro());
                        } else {
                            aux += 12; // pula para o proximo registro
                        }
                    }

                    // inserir o novo registro
                    inserir(id, endRegistro);
                }

                // Caso 3: bucket sem espaco e pLocal == pGlobal
                else if ((getQtdRegistros() == maxRegistros) && (getpLocal() == getpGlobal())) {
                    aumentaProfundidade();
                    inserir(id, endRegistro);
                    return getEndBucket();
                }
            }
            return -1;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao inserir: " + e.getMessage());
            e.getStackTrace();
            return -1;
        }
    }

    public void aumentaProfundidade() {
        try {
            int qtdPointersAntigo = ((int) Math.pow(2, getpGlobal())); // armazena a qtd de pointers antes do aumento do pGlobal
            setpGlobal(getpGlobal() + 1); // atualiza o pGlobal

            hashPointers.seek(0); // posiciona o ponteiro no inicio do arquivo
            hashPointers.writeInt(getpGlobal()); // escreve o novo pGlobal
            hashPointers.skipBytes(4); // pula os 4 bytes da qtd de buckets

            for (int i = 1; i <= qtdPointersAntigo; i++) {
                setEndBucket(hashPointers.readLong()); // le o endBucket
                hashPointers.seek(hashPointers.length()); // posiciona o ponteiro no final do arquivo
                hashPointers.writeLong(getEndBucket()); // escreve o endBucket do novo pointer
                hashPointers.seek((i * 8) + 8); // posiciona o ponteiro depois do pointer lido, pulando pGlobal e qtdBuckets
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro a aumentar profundidade! " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void criaBucket() {
        try {
            hashBuckets.seek(hashBuckets.length()); // posiciona o ponteiro no final do arquivo
            setEndBucket(hashBuckets.getFilePointer()); // armazena o novo endBucket

            hashBuckets.writeInt(getpGlobal()); // escreve o pLocal
            hashBuckets.writeInt(0); // escreve a qtd de registros (bucket inicia vazio)
            for (int i = 0; i < maxRegistros; i++) { // escreve os registros vazios
                hashBuckets.writeInt(-1); // escreve o idRegistro
                hashBuckets.writeLong(-1); // escreve o endRegistro
            }
            setQtdBuckets(getQtdBuckets() + 1); // atualiza a qtd de buckets escritos no arquivo
            hashPointers.seek(4); // posiciona o ponteiro no inicio do arquivo, pulando o pGlobal
            hashPointers.writeInt(getQtdBuckets()); // escreve a qtd de buckets

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao criar novo bucket: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
