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
            this.rf = new RandomAccessFile("banco.bin", "rw"); // cria o arquivo
            if(rf.length() != 0) { // se o arquivo não ta vazio (le o ultimo id)
                rf.seek(0);
                lastID = rf.readInt();
            }
        } catch (Exception e) {
            System.out.println();
            System.out.println("Erro ->  " + e.getMessage());
        }
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

public void create(Classe netflix) {

}

}


