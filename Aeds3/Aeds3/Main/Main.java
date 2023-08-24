package Main;

import java.util.Scanner;

import Model.Classe;

public class Main{
    public static void Main(String[] args) throws Exception{
        Classe classe = new Classe();
        Crud crud = new Crud();
        Scanner sc = new Scanner(System.in);


        crud.conBinario(classe); // converte o arquivo csv para binario
        
    }

}