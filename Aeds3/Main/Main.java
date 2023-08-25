package Main;

import java.util.Scanner;

import Model.Classe;

public class Main{


        //static void breakingTheLine(String line) {
            //Classe classe = new Classe();
            //line = NetflixParser.setId(line, classe);
            //line = NetflixParser.setType(line, classe);
            //line = NetflixParser.setDirectior(line, classe);
            //line = NetflixParser.setCast(line, classe);
            // line = NetflixParser.setDate(line, classe);

        //}
    
        public static void main(String[] args) throws Exception {
            String csvFilePath = "/mnt/c/Users/fabio/OneDrive/\u00C1rea de Trabalho/Aeds3/Aeds3/DataBase/netflix_titles2.csv";
            String[] lines = NetflixParser.getLines(csvFilePath);
    
            /* for (int i = 0; i < lines.length; i++) {
                breakingTheLine(lines[i]);
            }
            */
        }
    }   




/* 
        
        Crud crud = new Crud();
        Scanner sc = new Scanner(System.in);


        crud.conBinario(classe); // converte o arquivo csv para binario
        
        */
