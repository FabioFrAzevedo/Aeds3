package Main;

import java.io.RandomAccessFile;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.FileInputStream;

import Model.Classe;

public class Crud{
    /**
     *
     */
    public int lastID = 0;
    public RandomAccessFile rf;

    public Crud(){
        this.lastID = 0;

        try {
            this.rf = new RandomAccessFile("teste.bin", "rw"); // cria o arquivo
            if(rf.length() != 0) { // se o arquivo não ta vazio (le o ultimo id)
                rf.seek(0);
                lastID = rf.readInt();
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ->  " + e.getMessage());
        }
    }

    public int getLastID(){
        return lastID;
    }

    
    // CONVERTER PARA BINÁRIO
    public void conBinario(Classe classe) {
        BufferedReader br = null;

        try {
            if (rf.length() == 0) { // se o arquivo estiver vazio, cria o binario
                try {
                    try {
                        br = new BufferedReader(new InputStreamReader(new FileInputStream("netflix_titles2.csv"), "iso-8859-1"));
                    } catch (Exception e) {
                        try {
                            br = new BufferedReader(new InputStreamReader(new FileInputStream("netflix_titles2.csv"), "utf-8"));
                        } catch (Exception e2) {
                            System.err.println("ERRO");
                        }
                    }

                    rf.writeInt(0); // escreve o lastId
                    String line = br.readLine();
                    while (line != null) {
                        NetflixParser.lerCSV(line, classe); // le a linha do CSV e armazena no objeto
                        create(classe); // cria o registro no arquivo binario
                        line = br.readLine(); // le a proxima linha
                    }

                    System.out.println();
                    System.out.println("Arquivo criado");
                } catch (Exception e) {
                    System.out.println();
                    System.out.println("ERRO ao criar o arquivo binario! " + e.getMessage());
                }
            }
            rf.seek(0); // posiciona o ponteiro no inicio do arquivo
        } catch (Exception e) {
            System.out.println();
            System.out.println("Arquivo não esta vazio");
        }
    }

    // CREATE
    public boolean create(Classe classe) {
        try {
            byte[] by = classe.toByteArray();
            rf.seek(rf.length()); // posiciona o ponteiro no final do arquivo
            rf.writeByte(0); // escreve a lapide (ativo)
            rf.writeInt(by.length); // escreve o tamanho do registro

            // escreve o registro
            rf.write(by);
            lastID++;
            rf.seek(0); // posiciona o ponteiro no inicio do arquivo
            rf.writeInt(lastID); // incrementa o lastId e escreve no arquivo
            return true;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao criar registro!");
            return false;
        }
    }

    // READ


    // UPDATE

    //DELETE
    
    public boolean delete(Classe classe) {
        try {
            rf.seek(4); // posiciona o ponteiro no inicio do arquivo, pulando o lastId

            while (rf.getFilePointer() < rf.length()) { // enquanto o ponteiro não chegar no final do arquivo
                if (rf.readByte() == 0) { // se o registro estiver ativo (lapide 0)
                    int tamanho = rf.readInt(); // tamanho do registro
                    int id = rf.readInt(); // id do registro

                    if (id == classe.getShow_id()) { // se o id do registro for igual ao id a ser apagado
                        rf.seek(rf.getFilePointer() - 9); // volta o ponteiro pro inicio do registro (1 byte da lapide e 4 bytes do tamanho do registro)
                        rf.writeByte(1); // exclui o registro (lapide 1)
                        return true;
                    } else {
                        rf.skipBytes(tamanho - 4); // pula o restante do registro (menos o id)
                    }
                } else {
                    rf.skipBytes(rf.readInt()); // pula o registro
                }
            }
            return true;
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ao excluir registro!");
            return false;
        }
    }
}




