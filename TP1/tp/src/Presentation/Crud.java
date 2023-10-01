package Presentation;

import java.io.RandomAccessFile;

import Model.Netflix;
import Arvore.ArvoreB;
import Hash.Hash;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

public class Crud {
    
    private int lastId = 0;
    private int hashPGlobal = 0;
    private int hashQtdBuckets = 0;
    private long arvoreRaiz = -1;
    private int arvoreQtdPgs = 0;

    public RandomAccessFile raf;


    Hash hash = new Hash();
    ArvoreB arvoreB = new ArvoreB();

    public Crud() {
        this.lastId = 0;
        this.hashPGlobal = 1;
        this.hashQtdBuckets = 0;
        this.arvoreRaiz = -1;
        this.arvoreQtdPgs = 0;
        try {
            this.raf = new RandomAccessFile("teste.bin", "rw"); // cria o arquivo
            if (raf.length() != 0) { // se o arquivo não estiver vazio, lê o lastId
                raf.seek(0);
                lastId = raf.readInt();
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao criar o raf: " + e.getMessage());
        }
    }

    public int getLastId() {
        return lastId;
    }

    public int getHashPGlobal() {
        return hashPGlobal;
    }

    public void setHashPGlobal(int hashPGlobal) {
        this.hashPGlobal = hashPGlobal;
    }

    public int getHashQtdBuckets() {
        return hashQtdBuckets;
    }

    public void setHashQtdBuckets(int hashQtdBuckets) {
        this.hashQtdBuckets = hashQtdBuckets;
    }

    public long getArvoreRaiz() {
        return arvoreRaiz;
    }

    public void setArvoreRaiz(long arvoreRaiz) {
        this.arvoreRaiz = arvoreRaiz;
    }

    public int getArvoreQtdPgs() {
        return arvoreQtdPgs;
    }

    public void setArvoreQtdPgs(int arvoreQtdPgs) {
        this.arvoreQtdPgs = arvoreQtdPgs;
    }

    public RandomAccessFile getRaf() {
        return raf;
    }

    public void convertToBin(Netflix netflix) {
        BufferedReader br = null;

        try {
            if (raf.length() == 0) { // se o arquivo estiver vazio, cria o binario
                try {
                    try {
                        br = new BufferedReader(
                                new InputStreamReader(new FileInputStream("tp/DataBase/netflix_titles.csv"), "iso-8859-1"));
                    } catch (Exception e) {
                        try {
                            br = new BufferedReader(
                                    new InputStreamReader(new FileInputStream("tp/DataBase/netflix_titles.csv"), "utf-8"));
                        } catch (Exception e2) {
                            System.err.println("Error: createBin");
                        }
                    }

                    System.out.println("Arquivos sendo criados...");
                    raf.writeInt(0); 
                    String line = br.readLine();
                    while (line != null) {
                        NetflixParser.readCSV(line, netflix); // le a linha do CSV e armazena no objeto
                        create(netflix); // cria o registro no arquivo binario
                        line = br.readLine(); // le a proxima linha
                    }

                    System.out.println();
                    System.out.println("Arquivos hash criados");
                    System.out.println("Arvore B criada");
                    System.out.println("Banco binario criado");
                    System.out.println();
                } catch (Exception e) {
                    System.out.println();
                    System.out.println("Erro ao criar o arquivo binario! " + e.getMessage());
                }
            }
            raf.seek(0); // posiciona o ponteiro no inicio do arquivo
        } catch (Exception e) {
            System.out.println();
            System.out.println("Arquivo binario nao esta vazio!");
        }
    }

    // Hash 

    public void getHashInfo() {
        hash.readQbAndPg();
        setHashPGlobal(hash.getpGlobal());
        setHashQtdBuckets(hash.getQtdBuckets());
    }

    public long findHash(int id) {
        return hash.find(id); // busca o id no hash
    }

    //  Arvore 

    public void getArvoreInfo() {
        setArvoreRaiz(arvoreB.readEndRaiz()); 
        setArvoreQtdPgs(arvoreB.readQtdPaginas()); 
    }

    public long findArvore(int id) {
        long raiz = arvoreB.readEndRaiz(); 
        long pagina = arvoreB.findPagina(raiz, id); 

        if (pagina != -1) { 
            return arvoreB.findRegistro(pagina, id); 
        } else { return -1; }
    }

    // Create

    public boolean create(Netflix netflix) {
        long endNetflix = 0;
        long endPagina = -1;

        try {
            byte[] ba = netflix.toByteArray();
            raf.seek(raf.length()); 
            endNetflix = raf.getFilePointer(); 
            raf.writeByte(0); // escreve a lapide (ativo)
            raf.writeInt(ba.length); 

            raf.write(ba); 
            hash.inserir(netflix.getShow_id(), endNetflix);

            endPagina = arvoreB.findPagina(arvoreB.readEndRaiz(), netflix.getShow_id());
            arvoreB.setPagina(arvoreB.readPagina(endPagina));
            arvoreB.inserir(endPagina, arvoreB.getPagina(), netflix.getShow_id(), endNetflix);

            lastId++;
            raf.seek(0); 
            raf.writeInt(lastId); 
            return true;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao criar registro!");
            return false;
        }
    }

    // Read
    public Netflix read(int id, int opcao) {
        try {
            Netflix netflix = new Netflix();
            long endereco = -1;

            if (opcao == 1) {
                endereco = hash.find(id);
            } else {
                long raiz = arvoreB.readEndRaiz(); 
                long pagina = arvoreB.findPagina(raiz, id); 
                
                if (pagina != -1) { 
                    endereco = arvoreB.findRegistro(pagina, id); 
                }

            }

            if (endereco != -1) {
                raf.seek(endereco + 1); // posiciona o ponteiro no endereco hash, pulando a lapide
                int tamanho = raf.readInt(); 

                byte[] netflixByte = new byte[tamanho]; 
                raf.read(netflixByte); 

                netflix.fromByteArray(netflixByte);

                return netflix;
            } else {
                return null;
            }

        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao ler registro!" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    // Update
    public boolean update(Netflix netflix) {
        try {
            long endereco = hash.find(netflix.getShow_id());
            byte[] ba = netflix.toByteArray();

            if (endereco != -1) {
                raf.seek(endereco + 1); // posiciona o ponteiro no endereco hash, pulando a lapide
                int tamanho = raf.readInt(); // tamanho do registro
                if (tamanho >= netflix.toByteArray().length) { // se o registro for maior ou igual ao novo registro
                    raf.write(ba);
                    return true;

                } else { // se o novo registro nao couber no registro existente
                    raf.writeByte(1); // exclui o registro (lapide 1)
                    return create(netflix);
                }
            }
            return false;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao atualizar registro!");
            return false;
        }
    }

    // Delete
    public boolean delete(Netflix netflix) {
        try {
            long endereco = hash.find(netflix.getShow_id());

            if (endereco != -1) {
                raf.seek(endereco); // posiciona o ponteiro no endereco hash
                raf.writeByte(1); // exclui o registro (lapide 1)
                getHashInfo();
                hash.excluir(netflix.getShow_id(), hash.funcaoHash(netflix.getShow_id(), getHashPGlobal())); // exclui o registro no hash
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao excluir registro!");
            return false;
        }
    }
}