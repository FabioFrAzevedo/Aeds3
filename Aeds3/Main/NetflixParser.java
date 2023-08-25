package Main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.text.ParseException;

import Model.Classe;


public class NetflixParser extends Crud {
     static String[] getLines(String csvFilePath) {
        String[] lines = new String[8450];
        try {
            BufferedReader reader = new BufferedReader(new FileReader(csvFilePath));
            int index = 0;
            reader.readLine();
            while ((lines[index] = reader.readLine()) != null) {
                index++;
            }
            reader.close();
        } catch (Exception e) {
            // TODO: handle exception
        }
        return lines;
    }
}

