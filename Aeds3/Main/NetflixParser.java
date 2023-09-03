package Main;

import java.text.ParseException;
import Model.Classe;


public class NetflixParser extends Crud {

    ////////////////////////////////////////////////////
    // fazer a leitura do CSV
    public static void lerCSV(String line, Classe classe) throws ParseException{
        String x1 = ""; // string auxiliar
        int pont1 = 0; // ponteiro

        // SHOW ID 
        for (int i=0; i< 4; i++){ // leitura dos 4 primeiros caracteres do id
            x1 += line.charAt(i); // adiciona o caractere na string auxiliar
            pont1 ++; // incrementa o ponteiro
        }

        classe.setShow_id(Integer.parseInt(x1)); // converte string para inteiro e armazena no objeto
        x1 = ""; // limpa a string 
        pont1 ++; // incrementa o ponteiro (pula a ",")

        // TYPE
        for (int i = pont1; i<11; i++){
            x1 += line.charAt(i);
            pont1 ++;
        }
        pont1 ++;
        classe.setType(x1);
        x1 = "";
    

    // DIRETOR
    if(line.charAt(pont1) == '\"'){
        pont1 ++;
        for (int i = pont1; i < line.length(); i++){
            if (line.charAt(i) != '\"' || (line.charAt(i) == '\"' && line.charAt(i + 1) == '\"') || (line.charAt(i) == '\"' && line.charAt(i - 1) == '\"')) {
                x1 += line.charAt(i);
                pont1++;
        } else {
            pont1 += 2;
            i = line.length();
        }
    }
  } else {
    for (int i = pont1; i < line.length(); i++) {
        if (line.charAt(i) != ',') {
            x1 += line.charAt(i);
            pont1++;
        } else {
            pont1++;
            i = line.length();
        }
    }
  }
    if (x1 != ""){
        classe.setDirector(x1.split(","));
        classe.setcontDirector(classe.getDirector().length);
    } else {
        classe.setcontDirector(0);
    }
        x1 = "";

        // CAST
        if(line.charAt(pont1) == '\"'){
            pont1++;
            for (int i = pont1; i < line.length(); i++){
                if (line.charAt(i) != '\"' || (line.charAt(i) == '\"' && line.charAt(i + 1) == '\"') || (line.charAt(i) == '\"' && line.charAt(i - 1) == '\"')) {
                    x1 += line.charAt(i);
                    pont1 ++;
            } else {
                pont1 += 2;
                i = line.length();
            }
        }
    } else {
        for(int i = pont1; i < line.length(); i++){
            if(line.charAt(i) != ','){
                x1 += line.charAt(i);
                pont1 ++;
            } else {
                pont1 ++;
                i = line.length();
            }
        }
    }
    if (x1 != ""){
        classe.setCast(x1.split(","));
        classe.setcontCast(classe.getCast().length);
    } else {
        classe.setcontCast(0);
    }
    x1 = "";

    // DATE ADDED
    String ano = ""; String mes = ""; String dia = "";
    String[] x2 = new String[2]; 

    if (line.charAt(pont1) == '\"'){
        pont1++; // pula aspas
        for(int i = pont1; i < line.length(); i++){
            if(line.charAt(i) != '\"'){
                x1 += line.charAt(i);
                pont1++;
            } else {
                pont1 += 2;
                i = line.length();
            }
        }
        
        x2 = x1.split(",");

        // separar os meses
        for (int i=0; i< x1.length(); i++){
            if (x2[0].charAt(i) == ' '){
                dia += x1.charAt(i + 1);
                if(x1.charAt(i + 2) != '.'){
                    dia += x1.charAt(i +2);
                } 
                i = x1.length();
            }
        }

        mes = mes.toLowerCase();
        switch (mes) {
            case "january":
                mes = "01";
                break;
            case "february":
                mes = "02";
                break;
            case "march":
                mes = "03";
                break;
            case "april":
                mes = "04";
                break;
            case "may":
                mes = "05";
                break;
            case "june":
                mes = "06";
                break;
            case "july":
                mes = "07";
                break;
            case "august":
                mes = "08";
                break;
            case "september":
                mes = "09";
                break;
            case "october":
                mes = "10";
                break;
            case "november":
                mes = "11";
                break;
            case "december":
                mes = "12";
                break;
        }

        if (dia.length() < 2) { // se o dia for menor que 10, acrescenta um 0 na frente
            x1 = ano + "-" + mes + "-" + "0" + dia;
        } else {
            x1 = ano + "-" + mes + "-" + dia;
        }
        
        classe.setDateString(x1);
        classe.setDate_added(x1);
        x1 = "";
    } else {
        // se nao tiver aspas, a data nao existe
        classe.setDateString("Sem data");
        classe.setDate_added("2014-01-01"); // data ficticia
        pont1 += 2;
    }

         // separar ano
         for (int i = 0; i < 4; i++) {
            ano += x2[1].charAt(i);
        }

        // RELEASE YEAR
        for (int i = pont1; i < line.length(); i++) {
            if (line.charAt(i) != ',') {
                x1 += line.charAt(i);
                pont1++;
            } else {
                pont1++;
                i = line.length();
            }
        }
        classe.setRelease_year(Integer.parseInt(x1));
        x1 = "";
        
        // 
    }

  // PRINT
     public static void print(Classe classe) {
        // imprime o id do registro
        if (classe.getShow_id() < 10) {
            System.out.println("_REGISTRO " + "000" + classe.getShow_id() + "___");
        } else if (classe.getShow_id() < 100) {
            System.out.println("_REGISTRO " + "00" + classe.getShow_id() + "___");
        } else if (classe.getShow_id() < 1000) {
            System.out.println("_REGISTRO " + "0" + classe.getShow_id() + "___");
        } else {
            System.out.println("_REGISTRO " + classe.getShow_id() + "___");
        }
        // imprime os dados do registro
        System.out.println("ID: " + classe.getShow_id());
        System.out.println("Tipo: " + classe.getType());
        System.out.println("Diretores: " + classe.getContDirector());
        if (classe.getContDirector() > 0) {
            for (int i = 0; i < classe.getContDirector(); i++) {
                System.out.println("Diretor " + (i + 1) + ": " + classe.getDirector()[i]);
            }
        }   
        System.out.println("Elenco: " + classe.getContCast());
        if (classe.getContCast() > 0) {
            for (int i = 0; i < classe.getContCast(); i++) {
                System.out.println("Ator(a) " + (i + 1) + ": " + classe.getCast()[i]);
            }
        }
       
        if (classe.getDateString().equals("1010-10-10")) {
            System.out.println("Data adicionada: Sem data");
        } else {
            System.out.println("Data adicionada: " + classe.getDate_added());
        }
        System.out.println("Lançamento: " + classe.getRelease_year());

        System.out.println("___"); // 
        System.out.println();
    }

}

