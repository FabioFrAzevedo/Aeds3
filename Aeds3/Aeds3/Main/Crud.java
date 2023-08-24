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
    private int lastID = 0;
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
    }

}
    
