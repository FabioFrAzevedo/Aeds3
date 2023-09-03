package Main;

import java.util.Scanner;

import Model.Classe;

public class Main{
    /**
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception{
        Classe classe = new Classe();
        Crud crud = new Crud();
        Scanner sc = new Scanner(System.in);

        String input = "";
        boolean sair = false; // controle do menu
        int menu1 = 0; // opção do menu


        crud.conBinario(classe); // converte o arquivo csv para binario


        while(!sair){
            System.out.println("MENU");
            System.out.println(" 0 - Sair  ");
            System.out.println(" 1 - Cadastrar ");
            System.out.println(" 2 - Ler registro  ");
            System.out.println(" 3 - Atualizar registro ");
            System.out.println(" 4 - Excluir registro ");
            System.out.print("-> ");;

            do{
                try{
                    input = sc.nextLine();
                    menu1 = Integer.parseInt(input);

                    if(menu1 < 0 || menu1 > 4){
                        System.out.println("Inválido");
                        System.out.println(" > ");
                    }
                } catch (Exception e) { // se o usuario digitar algo que não seja um número inteiro 
                    System.out.println("Digite um número");
                    System.out.println(" > ");
                    System.out.println("ERRO: " + e.getMessage());
                    e.printStackTrace();
                    sc.nextLine();
                    break;
                }
            } while (menu1 < 0 || menu1 > 4); //se a opção for inválidade

            switch (menu1) {
                case 0:
                    System.out.println("Saindo...");
                    sair = true;
                break;

                case 1: // cadastrar o registro
                    System.out.println("Cadastrar registro");

                    // TYPE
                    System.out.print("Digite o tipo: ");
                    System.out.println("1- Filme");
                    System.out.println("2- Série");
                    System.out.println(" > ");

                    do {
                        try {
                            input = sc.nextLine();
                            menu1 = Integer.parseInt(input);
                            if (menu1 < 1 || menu1 > 2) {
                                System.out.println("Opcao invalida!");
                                System.out.print("-> ");
                            }
                        } catch (Exception e) { // se o usuario digitar algo que não seja um numero inteiro
                            System.out.println("Digite um numero!");
                            System.out.print("-> ");
                            sc.nextLine();
                            break;
                        }
                    } while (menu1 < 1 || menu1 > 2); // enquanto a opcao for invalida

                    if (menu1 == 1) {
                        classe.setType("Movies");
                    } else {
                        classe.setType("Series");
                    }

                    // Director
                    System.out.println("Digite a quantidade de diretores: ");
                    System.out.print("-> ");
                    input = sc.nextLine();
                    classe.setcontDirector(Integer.parseInt(input));
                    if (classe.getContDirector() > 0) {
                        String[] directors = new String[classe.getContDirector()];
                        for (int i = 0; i < classe.getContDirector(); i++) {
                            System.out.println("Nome do Diretor" + (i + 1) + ": ");
                            System.out.print("-> ");
                            directors[i] = sc.nextLine();
                        }
                        classe.setDirector(directors);
                    }

                    // Cast
                    System.out.println("Digite a quantidade de atores: ");
                    System.out.print("-> ");
                    input = sc.nextLine();
                    classe.setcontCast(Integer.parseInt(input));
                    if (classe.getContCast() > 0) {
                        String[] actors = new String[classe.getContCast()];
                        for (int i = 0; i < classe.getContCast(); i++) {
                            System.out.println("Nome do ator" + (i + 1) + ": ");
                            System.out.print("-> ");
                            actors[i] = sc.nextLine();
                        }
                        classe.setCast(actors);
                    }

                     // Date Added
                     System.out.println("Digite a data de adicao (aaaa-mm-dd): ");
                     System.out.print("-> ");
                     classe.setDateString(sc.nextLine());
                     classe.setDate_added(classe.getDateString());
 
                     // Release Year
                     System.out.println("Digite o ano de lancamento: ");
                     System.out.print("-> ");
                     input = sc.nextLine();
                     classe.setRelease_year(Integer.parseInt(input));

                     classe.setShow_id(crud.getLastID() + 1); // incrementa o ultimo id utilizado e armazena no objeto

                     // crud create


                     // case 2 (ler registro)
                     // case 3 (atualizar registro)

            }

        }
        
    }

}