package Sort;


import java.util.Scanner;

import Model.Netflix;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.ArrayList;
//import java.util.Scanner;

public class Ext {
    protected static RandomAccessFile arq;
    protected static byte[] bytesArray;
    private static String filename = "banco.bin";

    /* Menu */
    public static void menu() throws IOException {
        int option = -1;
        Scanner sc = new Scanner(System.in);
        while (option != 4) {
            System.out.println("------- Ordenação externa ------");
            System.out.println("- 1) Commum                     -");
            System.out.println("- 2) Blocos de tamanho variável-");
            System.out.println("- 3) Seleção por substituição  -");         
            System.out.println("- 4) Terminar                  -");
            System.out.println("--------------------------------");

            System.out.print("Escolha um número -> ");

            option = sc.nextInt();
            clearBuffer(sc);

            switch (option) {
                case 1:
                    System.out.println("------ Intercalação balanceada comum ------");

                    System.out.println("- Tamanho registro: ");
                    int regsize = sc.nextInt();
                    clearBuffer(sc);

                    System.out.println("- Caminhos: ");
                    int ncaminhos = sc.nextInt();
                    clearBuffer(sc);

                    Comum.comum(regsize, ncaminhos);
                    break;
                    
                case 2:
                    System.out.println("------ Intercalação balanceada com blocos de tamanho variável ------");


                    break;

                case 3:
                    System.out.println("----- Intercalação balanceada com seleção por substituição -----");
                    
                    break;

                case 4:
                    break;
                    
                default:
                System.out.println("Opção inválida.");}}
        sc.close();
    }

    public static void clearBuffer(Scanner sc) {
        if (sc.hasNextLine()) {
            sc.nextLine();
        }
    }

    // Métodos de intercalação externos

    /* Comum */
    private static class Comum {
        // ArrayList<Netflix>
        public static boolean comum(int regsize, int ncaminhos) throws IOException {
            ArrayList<Netflix> Netflixs = getRecords(filename);
            ArrayList<Netflix> tempfile1 = new ArrayList<Netflix>();            
            ArrayList<Netflix> tempfile2 = new ArrayList<Netflix>();
            ArrayList<Netflix> tempfile3 = new ArrayList<Netflix>();            
            ArrayList<Netflix> tempfile4 = new ArrayList<Netflix>();                
            ArrayList<Netflix> toSort = new ArrayList<Netflix>();
            
            /* Partition - Pt. 1 */    

            int numfile = 1;
            int recordsCount = 1;
            boolean sort = false;

            for (int i = 0; i <= Netflixs.size(); i++) {
                
                if (recordsCount == regsize) {
                    if (numfile == ncaminhos) {
                        numfile = 1;
                        sort = true;

                    } else {
                        numfile++;
                        sort = true;}
                    recordsCount = 1;}

                if (numfile == 1) {
                    if (toSort.size() > 0 && sort) {
                        
                        toSort = Sort.sort(toSort);
                        for (Netflix Netflix : toSort) { 
                            tempfile2.add(Netflix); }

                        sort = false;
                        toSort.clear();}

                    if (Netflixs.size()-1 > i) 
                        toSort.add(Netflixs.get(i));


                } else if (numfile == 2) {
                    if (toSort.size() > 0 && sort) {
                        toSort = Sort.sort(toSort);

                        for (Netflix Netflix : toSort) { 
                            tempfile1.add(Netflix); }

                        sort = false;
                        toSort.clear();}

                    if (Netflixs.size()-1 > i) toSort.add(Netflixs.get(i));}

                recordsCount++;}

            System.out.println("\n" + "---- Arquivo temporario 1 ----" + "\n");
            for (Netflix Netflix : tempfile1) {
                System.out.println(Netflix);
            }

            System.out.println("\n" + "---- Arquivo temporario 2 ----" + "\n");
            for (Netflix Netflix : tempfile2) {
                System.out.println(Netflix);
            }

            System.out.println("\n" + "Arquivo temporario 1: " + tempfile1.size() + "\n" + "Arquivo temporario 2: " + tempfile2.size() + "\n");

            System.out.println("\n" + "Número de registros da memória secundária: " + Netflixs.size() + "\n");

            for (Netflix record : tempfile1) {
                System.out.println(record.show_id);}

            for (Netflix record : tempfile2) {
                System.out.println(record.show_id);}
            
            /* Balanceada - Pt 2.1 */
            
            int pointer1 = 0;
            int pointer2 = 0;
            int sumSizes = tempfile1.size() + tempfile2.size();
            boolean p1Final = false;
            boolean p2Final = false;
            numfile = 3;
            recordsCount = 1;

            for (int i = 0; i < sumSizes+1; i++) {
                if (recordsCount == regsize) {
                    if (numfile == ncaminhos+2) {
                        numfile = 3;
                    } else {
                        numfile++;}
                    recordsCount = 1;}

                while ((!p1Final) && (pointer1 < tempfile1.size() && pointer2 < tempfile2.size()) &&
                (tempfile1.get(pointer1).show_id < tempfile2.get(pointer2).show_id)) {

                    if (numfile == 3) 
                    tempfile3.add(tempfile1.get(pointer1));

                    else 
                    tempfile4.add(tempfile1.get(pointer1));

                    if (tempfile1.size()-1 > pointer1) 
                    pointer1++;

                    else 
                    p1Final = true;}
                
                while ((!p2Final) && (pointer1 < tempfile1.size() && pointer2 < tempfile2.size()) &&
                (tempfile2.get(pointer2).show_id < tempfile1.get(pointer1).show_id)) {
                    
                    if (numfile == 3) 
                    tempfile3.add(tempfile2.get(pointer2));

                    else 
                    tempfile4.add(tempfile2.get(pointer2));

                    if (tempfile2.size()-1 > pointer2 + 1) 
                    pointer2++;

                    else 
                    p2Final = true; }

                while (!p1Final && p2Final) {
                    if (numfile == 3) 
                    tempfile3.add(tempfile1.get(pointer1));
                    
                    else 
                    tempfile4.add(tempfile1.get(pointer1));
                    
                    if (tempfile1.size()-1 > pointer1) 
                    pointer1++;
                    
                    else 
                    p1Final = true;}

                while (!p2Final && p1Final) {
                    if (numfile == 3) 
                    tempfile3.add(tempfile2.get(pointer2));
                    
                    else 
                    tempfile4.add(tempfile2.get(pointer2));
                    
                    if (tempfile2.size()-1 > pointer2 + 1) 
                    pointer2++;
                    
                    else 
                    p2Final = true;}
                recordsCount++; }

            System.out.println("\n" + "---- Arquivo temporario 3 ----" + "\n");
            for (Netflix Netflix : tempfile3) {
                System.out.println(Netflix);}

            System.out.println("\n" + "---- Arquivo temporario 4 ----" + "\n");
            for (Netflix Netflix : tempfile4) {
                System.out.println(Netflix); }

            System.out.println("\n" + "Arquivo temporario 3: " + tempfile3.size() + "\n" + "Arquivo temporario 4: " + tempfile4.size() + "\n");

            for (Netflix record : tempfile3) {
                System.out.println(record.show_id);}

            for (Netflix record : tempfile4) {
                System.out.println(record.show_id);}

            /* Balancaeda - Pt 2.2 */

            tempfile1.clear();
            tempfile2.clear();
            
            int pointer3 = 0;
            int pointer4 = 0;
            sumSizes = tempfile3.size() + tempfile4.size();
            boolean p3Final = false;
            boolean p4Final = false;
            numfile = 1;
            recordsCount = 1;

            for (int i = 0; i < sumSizes+1; i++) {
                if (recordsCount == regsize) {
                    if (numfile == ncaminhos) {

                        numfile = 1;}
                        
                    else {
                        numfile++;}
                    recordsCount = 1;}
                recordsCount++;

                while ((!p1Final) && (pointer1 < tempfile1.size() && pointer2 < tempfile2.size()) &&
                (tempfile1.get(pointer1).show_id < tempfile2.get(pointer2).show_id)) {

                    if (numfile == 1) 
                    tempfile1.add(tempfile3.get(pointer3));

                    else 
                    tempfile2.add(tempfile3.get(pointer3));

                    if (tempfile3.size()-1 > pointer3) 
                    pointer3++;

                    else 
                    p3Final = true;}
                
                while ((!p4Final) &&(tempfile4.get(pointer4).show_id < tempfile3.get(pointer3).show_id)) {
                   
                    if (numfile == 1) 
                    tempfile1.add(tempfile4.get(pointer4));

                    else 
                    tempfile2.add(tempfile4.get(pointer4));

                    if (tempfile4.size()-1 > pointer4 + 1) 
                    pointer4++;

                    else 
                    p4Final = true;}

                while (!p3Final && p4Final) {

                    if (numfile == 1)
                     tempfile1.add(tempfile3.get(pointer3));

                    else 
                    tempfile2.add(tempfile3.get(pointer3));

                    if (tempfile3.size()-1 > pointer3) 
                    pointer3++;

                    else 
                    p3Final = true;}

                while (!p4Final && p3Final) {

                    if (numfile == 1) 
                    tempfile1.add(tempfile4.get(pointer4));

                    else 
                    tempfile2.add(tempfile4.get(pointer4));

                    if (tempfile4.size()-1 > pointer4 + 1) 
                    pointer4++;

                    else 
                    p4Final = true;}}

            System.out.println("\n" + "---- Arquivo temporario 1 ----" + "\n");
            for (Netflix Netflix : tempfile1) {
                System.out.println(Netflix);}

            System.out.println("\n" + "---- Arquivo temporario 2 ----" + "\n");
            for (Netflix Netflix : tempfile2) {
                System.out.println(Netflix);}

            System.out.println("\n" + "Arquivo temporario 1: " + tempfile1.size() + "\n" + "Arquivo temporario 2: " + tempfile2.size() + "\n");

            for (Netflix record : tempfile1) {
                System.out.println("tempfile1 ID: " + record.show_id); }

            for (Netflix record : tempfile2) {
                System.out.println("tempfile2 ID: " + record.show_id);}

            //return true;
        
        
        // // Em arquivos
        // public static boolean comumFiles(int regsize, int ncaminhos) throws IOException {
        //     ArrayList<Netflix> Netflixs = getRecords(filename);

        //     RandomAccessArquivo temporariofile1 = new RandomAccessFile("DataBase/tempfile1.db", "rw");            
        //     RandomAccessArquivo temporariofile2 = new RandomAccessFile("DataBase/tempfile2.db", "rw");

        //     ArrayList<Netflix> toSort = new ArrayList<Netflix>();
            
        //     /* Partition - Pt 1 */

        //     int numfile = 1;
        //     int recordsCount = 1;
        //     boolean sort = false;

        //     for (int i = 0; i <= Netflixs.size(); i++) {
                
        //         if (recordsCount == regsize) {
        //             if (numfile == ncaminhos) {
        //                 numfile = 1;
        //                 sort = true;

        //             } else {
        //                 numfile++;
        //                 sort = true;
        //             }
        //             recordsCount = 1;
        //         }

        //         if (numfile == 1) {
        //             if (toSort.size() > 0 && sort) {
        //                 toSort = Int.Sort.sort(toSort);
        //                 for (Netflix Netflix : toSort) { 
        //                     bytesArray = Netflix.toByteArray();
        //                     int len = bytesArray.length;
        //                     tempfile2.writeInt(len);
        //                     tempfile2.write(bytesArray);
        //                 }
        //                 sort = false;
        //                 toSort.clear();
        //             }
        //             if (Netflixs.size()-1 > i) toSort.add(Netflixs.get(i));


        //         } else if (numfile == 2) {
        //             if (toSort.size() > 0 && sort) {
        //                 toSort = Int.Sort.sort(toSort);
        //                 for (Netflix Netflix : toSort) { 
        //                     bytesArray = Netflix.toByteArray();
        //                     int len = bytesArray.length;
        //                     tempfile1.writeInt(len);
        //                     tempfile1.write(bytesArray); 
        //                 }
        //                 sort = false;
        //                 toSort.clear();
        //             }
        //             if (Netflixs.size()-1 > i) toSort.add(Netflixs.get(i));
        //         }
        //         recordsCount++;
        //     }

        //     tempfile1.close();
        //     tempfile2.close();            

        //     System.out.println("\n" + "Arquivo temporario 1" + "\n");
        //     read("DataBase/tempfile1.db");

        //     System.out.println("\n" + "Arquivo temporario 2" + "\n");
        //     read("DataBase/tempfile2.db");

            
            /* 
             * Balanceada - Pt 2.1 
             */

            // tempfile1 = new RandomAccessFile("DataBase/tempfile1.db", "rw");            
            // tempfile2 = new RandomAccessFile("DataBase/tempfile2.db", "rw");
            // RandomAccessArquivo temporariofile3 = new RandomAccessFile("DataBase/tempfile3.db", "rw");            
            // RandomAccessArquivo temporariofile4 = new RandomAccessFile("DataBase/tempfile4.db", "rw");

            // long pointer1 = 0;
            // long pointer2 = 0;            
            // long pointer3 = 0;
            // long pointer4 = 0;

            // boolean EOFtempfile1 = false;            
            // boolean EOFtempfile2 = false;


            // numfile = 1;
            // recordsCount = 1;

            // for (int i = 0; i < Netflixs.size(); i++) {
            //     if (recordsCount == regsize) {
            //         if (numfile == ncaminhos) {
            //             numfile = 1;
            //         } else {
            //             numfile++;
            //         }
            //         recordsCount = 1;
            //     }

            //     if (numfile == 1) {
            //         pointer1 = tempfile1.getFilePointer();
            //         pointer2 = tempfile2.getFilePointer();                    
            //         pointer3 = tempfile3.getFilePointer();                    
            //         pointer4 = tempfile4.getFilePointer();

            //         /* Read */
            //         Netflix Netflix1 = new Netflix();                    
            //         Netflix Netflix2 = new Netflix();

            //         tempfile1.seek(pointer1);                    
            //         tempfile2.seek(pointer2);
            //         tempfile3.seek(pointer3);                    
            //         tempfile4.seek(pointer4);

            //         if (!EOFtempfile1 && !EOFtempfile2) {

            //             System.out.println("\nPointer2: " + pointer2);

            //             int len1 = tempfile1.readInt();
            //             int len2 = tempfile2.readInt();

            //             pointer1 = tempfile1.getFilePointer();
            //             pointer2 = tempfile2.getFilePointer();

            //             /* Record tempfile1 */
            //             bytesArray = new byte[len1];
            //             tempfile1.read(bytesArray);
            //             Netflix1.fromByteArray(bytesArray);

            //             /* Record tempfile2 */
            //             bytesArray = new byte[len2];
            //             tempfile2.read(bytesArray);
            //             Netflix2.fromByteArray(bytesArray);

            //             if (Netflix1.id < Netflix2.show_id) {
            //                 long sumLen1 = pointer1 + len1;
            //                 tempfile1.seek(sumLen1);

            //                 pointer1 = tempfile1.getFilePointer();

            //                 if (pointer1 >= tempfile1.length()) {
            //                     EOFtempfile1 = true;
            //                 }

            //                 tempfile3.writeInt(len1);
            //                 tempfile3.write(Netflix1.toByteArray());                        
            //                 pointer3 = tempfile3.getFilePointer();

            //             } else {
            //                 long sumLen2 = pointer2 + len2;
            //                 tempfile2.seek(sumLen2);

            //                 pointer2 = tempfile2.getFilePointer();
            //                 System.out.println("\nLength: " + tempfile2.length());
            //                 if (pointer2 >= tempfile2.length()) {
                                
            //                     EOFtempfile2 = true;
            //                 }

            //                 tempfile3.writeInt(len2);
            //                 tempfile3.write(Netflix2.toByteArray());
            //                 pointer3 = tempfile3.getFilePointer();

            //             }
            //         }

            //         if (EOFtempfile2 && !EOFtempfile1) {
            //             int len1 = tempfile1.readInt();
            //             pointer1 = tempfile1.getFilePointer();

            //             /* Record tempfile1 */
            //             bytesArray = new byte[len1];
            //             tempfile1.read(bytesArray);
            //             Netflix1.fromByteArray(bytesArray);

            //             pointer1 = tempfile1.getFilePointer();

            //             if (pointer1 >= tempfile1.length()) {
            //                 EOFtempfile1 = true;
            //             }

            //             tempfile3.writeInt(len1);
            //             tempfile3.write(Netflix1.toByteArray());                        
            //             pointer3 = tempfile3.getFilePointer();
            //         }

            //         if (EOFtempfile1 && !EOFtempfile2) {
            //             int len2 = tempfile2.readInt();
            //             pointer2 = tempfile2.getFilePointer();
                        
            //             /* Record tempfile2 */
            //             bytesArray = new byte[len2];
            //             tempfile2.read(bytesArray);
            //             Netflix2.fromByteArray(bytesArray);

            //             pointer2 = tempfile2.getFilePointer();

            //             if (pointer2 >= tempfile2.length()) {
            //                 EOFtempfile2 = true;
            //             }

            //             tempfile3.writeInt(len2);
            //             tempfile3.write(Netflix2.toByteArray());                        
            //             pointer3 = tempfile3.getFilePointer();
            //         }
                                      
            //     } else {
            //         pointer1 = tempfile1.getFilePointer();
            //         pointer2 = tempfile2.getFilePointer();                    
            //         pointer3 = tempfile3.getFilePointer();                    
            //         pointer4 = tempfile4.getFilePointer();


            //         /* Leitura */
            //         Netflix Netflix1 = new Netflix();                    
            //         Netflix Netflix2 = new Netflix();

            //         tempfile1.seek(pointer1);
            //         tempfile2.seek(pointer2);
            //         tempfile3.seek(pointer3);
            //         tempfile4.seek(pointer4);

            //         if (!EOFtempfile1 && !EOFtempfile2) {
            //             int len1 = tempfile1.readInt();
            //             int len2 = tempfile2.readInt();

            //             pointer1 = tempfile1.getFilePointer();
            //             pointer2 = tempfile2.getFilePointer();

            //             /* Record tempfile1 */
            //             bytesArray = new byte[len1];
            //             tempfile1.read(bytesArray);
            //             Netflix1.fromByteArray(bytesArray);

            //             /* Record tempfile2 */
            //             bytesArray = new byte[len2];
            //             tempfile2.read(bytesArray);
            //             Netflix2.fromByteArray(bytesArray);

            //             if (Netflix1.id < Netflix2.show_id) {
            //                 long sumLen1 = pointer1 + len1;
            //                 tempfile1.seek(sumLen1);

            //                 pointer1 = tempfile1.getFilePointer();

            //                 if (pointer1 >= tempfile1.length()) {
            //                     EOFtempfile1 = true;
            //                 }

            //                 tempfile4.writeInt(len1);
            //                 tempfile4.write(Netflix1.toByteArray());                        
            //                 pointer4 = tempfile4.getFilePointer();

            //             } else {
            //                 long sumLen2 = pointer2 + len2;
            //                 tempfile2.seek(sumLen2);

            //                 pointer2 = tempfile2.getFilePointer();

            //                 if (pointer2 >= tempfile2.length()) {
            //                     EOFtempfile2 = true;
            //                 }

            //                 tempfile4.writeInt(len2);
            //                 tempfile4.write(Netflix2.toByteArray());
            //                 pointer4 = tempfile4.getFilePointer();
            //             }
            //         }

            //         if (EOFtempfile2 && !EOFtempfile1) {
            //             int len1 = tempfile1.readInt();
            //             pointer1 = tempfile1.getFilePointer();

            //             /* Record tempfile1 */
            //             bytesArray = new byte[len1];
            //             tempfile1.read(bytesArray);
            //             Netflix1.fromByteArray(bytesArray);

            //             pointer1 = tempfile1.getFilePointer();

            //             if (pointer1 >= tempfile1.length()) {
            //                 EOFtempfile1 = true;
            //             }

            //             tempfile4.writeInt(len1);
            //             tempfile4.write(Netflix1.toByteArray());                        
            //             pointer4 = tempfile4.getFilePointer();
            //         }

            //         if (EOFtempfile1 && !EOFtempfile2) {
            //             int len2 = tempfile2.readInt();
            //             pointer2 = tempfile2.getFilePointer();
                        
            //             /* Record tempfile2 */
            //             bytesArray = new byte[len2];
            //             tempfile2.read(bytesArray);
            //             Netflix2.fromByteArray(bytesArray);

            //             pointer2 = tempfile2.getFilePointer();

            //             if (pointer2 >= tempfile2.length()) {
            //                 EOFtempfile2 = true;
            //             }

            //             tempfile4.writeInt(len2);
            //             tempfile4.write(Netflix2.toByteArray());                        
            //             pointer4 = tempfile4.getFilePointer();
            //         }
            //     }
               
            //     recordsCount++;
            // }

            // tempfile1.close();                    
            // tempfile2.close();            
            // tempfile3.close();            
            // tempfile4.close();

            // read("DataBase/tempfile3.db");            
            // read("DataBase/tempfile4.db");
            /* Balanceada - Pt 2.2 */

            return true;
        }


        public static ArrayList<Netflix> getRecords(String filename) throws IOException {
            arq = new RandomAccessFile(filename, "rw");
            arq.seek(0);

            int totalRecords = arq.readInt();

            ArrayList<Netflix> records = new ArrayList<Netflix>();

            int len;
            int i = 0;

            while (i < totalRecords) {
                if (arq.getFilePointer() < arq.length()) {
                    char gravestone = arq.readChar();
                    len = arq.readInt();

                    Netflix Netflix = new Netflix();

                    bytesArray = new byte[len];
                    arq.read(bytesArray);

                    if (gravestone != '*') {
                        Netflix.fromByteArray(bytesArray);
                        records.add(Netflix);
                        i++;}
                } else {
                    break;}}

            for (Netflix record : records) {
                System.out.println(record.show_id);}

            System.out.println("\n" + records.size());
            
            return records;}
        
        // /* Leitura records */
        // public static void read(String filename) throws IOException {
        //     /* RandomAccessFile */
        //     arq = new RandomAccessFile(filename, "rw");

        //     /* Leitura */
        //     Netflix Netflix = new Netflix();

        //     int len;

        //     arq.seek(0);
        //     long pointer = arq.getFilePointer();

        //     while (pointer < arq.length()) {
        //         len = arq.readInt();
                
        //         bytesArray = new byte[len];
        //         arq.read(bytesArray);
        //         Netflix.fromByteArray(bytesArray);

        //         System.out.println(Netflix.show_id);

        //         pointer = arq.getFilePointer();}

        //     arq.close();}

        protected static class Sort {
            protected static ArrayList<Netflix> sort(ArrayList<Netflix> arr) {
                int n = arr.size();

                for (int i = 1; i < n; ++i) {

                    int key = arr.get(i).getShow_id();
                    int j = i - 1;
        
                    while (j >= 0 && arr.get(j).getShow_id() > key) {
                        
                        arr.get(j + 1).show_id = arr.get(j).show_id = arr.get(j).show_id;
                        j = j - 1;}

                    arr.get(j+1).show_id = key;}
    
                return arr;}}}}   
