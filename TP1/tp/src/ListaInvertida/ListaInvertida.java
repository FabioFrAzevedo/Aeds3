package ListaInvertida;

import Model.*;
import Presentation.*;

import java.io.IOException;
import java.io.RandomAccessFile;

public class ListaInvertida {

    // Lista Invertida
   
    Crud crud = new Crud();
    Netflix listaNetflix = new Netflix();

    public boolean listarTipo(RandomAccessFile raf, String nomeTipo) throws IOException {
        RandomAccessFile lista = new RandomAccessFile("ListaInvertidaTipo.bin", "rw");
        if(lista.length() != 0){ // Se o arquivo não estiver vazio
            lista.setLength(0); // Zera o arquivo
        }

        System.out.println("\nTipo (Movies ou Series): " + nomeTipo);
        lista.writeUTF(nomeTipo); // Escreve o tipo filme/serie no arquivo

        raf.seek(4); // Pula o cabeçalho
        int contador = 0; // Contador de registros
        while (raf.getFilePointer() < raf.length()) { // enquanto o ponteiro não chegar no final do arquivo
            
            double pointer = raf.getFilePointer(); // Pega o ponteiro atual   
            if(raf.readByte() == 0) { // Se o registro estiver ativo
                raf.readInt(); // tamanho 
                int id = raf.readInt(); // show_id
                String type = raf.readUTF(); // type
                raf.readUTF(); // title
                int qntdDiretores = raf.readInt(); // qtdDirectors
                for (int i = 0; i < qntdDiretores; i++) {
                    raf.readUTF(); // diretores
                }
                int qntdCast = raf.readInt(); // qntCast
                for (int i = 0; i < qntdCast; i++) {
                    raf.readUTF(); // cast
                }
                int qntdPaises = raf.readInt(); // qntCountries
                for (int i = 0; i < qntdPaises; i++) {
                    raf.readUTF(); // countries
                }
                raf.readUTF();
                raf.readInt();
                raf.readUTF();
                raf.readUTF();
                if (type.equals(nomeTipo)) { // Se o tipo for igual ao passado por parâmetro
                    System.out.println("ID: " + id + " - Posição: " + (int) pointer); // Imprime o ID e a posição do registro
                    lista.writeInt(id); // Escreve o ID no arquivo de lista invertida
                    lista.writeDouble(pointer); // Escreve a posição do registro no arquivo de lista invertida
                    contador++;
                }
            } else { // Se o registro estiver inativo
                raf.skipBytes(raf.readInt()); // Pula o registro
            }
        }

        System.out.println("Quantidade de registros: " + contador);
        lista.writeInt(contador); // Escreve a quantidade de registros no arquivo de lista invertida

        lista.close();
        return true;
    }

    public boolean listarLancamento(RandomAccessFile raf, int anoLancamento) throws IOException {

        RandomAccessFile lista = new RandomAccessFile("listaInvertidaAno.bin", "rw");
        if(lista.length() != 0){ // Se o arquivo não estiver vazio
            lista.setLength(0); // Zera o arquivo
        }

        System.out.println("\nAno Lançamento: " + anoLancamento);
        lista.writeInt(anoLancamento); // Escreve o nome da cidade no arquivo

        raf.seek(4); // Pula o cabeçalho
        int contador2 = 0; // Contador de registros
        while (raf.getFilePointer() < raf.length()) { // enquanto o ponteiro não chegar no final do arquivo
            double pointer = raf.getFilePointer(); // Pega o ponteiro atual
            if(raf.readByte() == 0) { // Se o registro estiver ativo
                raf.readInt(); // tamanho 
                int id = raf.readInt(); // show_id
                raf.readUTF(); // type
                raf.readUTF(); // title
                int qntdDiretores = raf.readInt(); // qtdDirectors
                for (int i = 0; i < qntdDiretores; i++) {
                    raf.readUTF(); // diretores
                }
                int qntdCast = raf.readInt(); // qntCast
                for (int i = 0; i < qntdCast; i++) {
                    raf.readUTF(); // cast
                }
                int qntdPaises = raf.readInt(); // qntCountries
                for (int i = 0; i < qntdPaises; i++) {
                    raf.readUTF(); // countries
                }
                raf.readUTF();
                int lancamento = raf.readInt(); // release_year
                raf.readUTF();
                raf.readUTF();
                if (lancamento == anoLancamento) { // Se o titulo for igual a passada por parâmetro
                    System.out.println("ID: " + id + " - Posição: " + (int) pointer); // Imprime o ID e a posição do registro
                    lista.writeInt(id); // Escreve o ID no arquivo de lista invertida
                    lista.writeDouble(contador2); // Escreve a posição do registro no arquivo de lista invertida
                    contador2++;
                }
            }else { // Se o registro estiver inativo
                raf.skipBytes(raf.readInt()); // Pula o registro
            }
           
        }

        System.out.println("Quantidade de registros: " + contador2);
        lista.writeInt(contador2); // Escreve a quantidade de registros no arquivo de lista invertida

        lista.close();
        return true;
    }
}
