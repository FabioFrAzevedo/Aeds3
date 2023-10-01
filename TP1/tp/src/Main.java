
import java.io.RandomAccessFile;
import java.util.Scanner;

import Sort.*;
import Model.Netflix;
import Presentation.*;
import ListaInvertida.ListaInvertida;

public class Main {
    public static void main(String[] args) throws Exception {
        Netflix netflix = new Netflix();
        Crud Crud = new Crud();
        Scanner sc = new Scanner(System.in);

        ListaInvertida lista = new ListaInvertida();

        String input = "";
        int opcao = 0; // opcao do menu
        boolean sair = false; 

        Crud.convertToBin(netflix); 
        RandomAccessFile raf = new RandomAccessFile("teste.bin", "rw"); // Cria o arquivo
        if (raf.length() == 0) raf.writeInt(0); // Se o arquivo estiver vazio escreve 0 no inicio do arquivo para indicar o ultimo id utilizado
        raf.seek(0); 

        while (!sair) {
            System.out.println("MENU");
            System.out.println("0 - Sair");
            System.out.println("1 - Cadastrar filme/serie");
            System.out.println("2 - Ler registro");
            System.out.println("3 - Atualizar registro ");
            System.out.println("4 - Excluir registro ");
            System.out.println("5 - Ordenar registro");
            System.out.println("6 - Hash Extensivel");
            System.out.println("7 - Arvore B");
            System.out.println("8 - Lista Invertida");
            System.out.print("-> ");

           do {
                try {
                    input = sc.nextLine();
                    opcao = Integer.parseInt(input);
                    if (opcao < 0 || opcao > 8) {
                        System.out.println("Opcao invalida!");
                        System.out.print("->");
                    }
                } catch (Exception e) { // se o usuario digitar algo que não seja um numero inteiro
                    System.out.println("Digite um numero!");
                    System.out.print("-> ");;
                    System.out.println("erro menu: " + e.getMessage());
                    e.printStackTrace();
                    sc.nextLine();
                    break;
                }
            } while (opcao < 0 || opcao > 8); // enquanto a opcao for invalida

            switch (opcao) {
                case 0:
                    System.out.println("Saindo...");
                    System.out.println();
                    raf.close();
                    sair = true;
                break;
                case 1: // cadastrar registro
                    System.out.println("CADASTRAR REGISTRO");

                    // Type
                    System.out.println("Digite o tipo: ");
                    System.out.println("1 - Filme");
                    System.out.println("2 - Serie");
                    System.out.print("-> ");;

                    do {
                        try {
                            input = sc.nextLine();
                            opcao = Integer.parseInt(input);
                            if (opcao < 1 || opcao > 2) {
                                System.out.println("Opcao invalida!");
                                System.out.print("-> ");
                            }
                        } catch (Exception e) { // se o usuario digitar algo que não seja um numero inteiro
                            System.out.println("Digite um numero!");
                            System.out.print("-> ");
                            sc.nextLine();
                            break;
                        }
                    } while (opcao < 1 || opcao > 2); // enquanto a opcao for invalida

                    if (opcao == 1) {
                        netflix.setType("Movies");
                    } else {
                        netflix.setType("Series");
                    }

                    // Title
                    System.out.println("Digite o titulo: ");
                    System.out.print("-> ");
                    netflix.setTitle(sc.nextLine());

                    // Director
                    System.out.println("Digite a quantidade de diretores: ");
                    System.out.print("-> ");
                    input = sc.nextLine();
                    netflix.setQtdDirectors(Integer.parseInt(input));
                    if (netflix.getQtdDirectors() > 0) {
                        String[] directors = new String[netflix.getQtdDirectors()];
                        for (int i = 0; i < netflix.getQtdDirectors(); i++) {
                            System.out.println("Nome do diretor" + (i + 1) + ": ");
                            System.out.print("-> ");
                            directors[i] = sc.nextLine();
                        }
                        netflix.setDirector(directors);
                    }

                    // Cast
                    System.out.println("Digite a quantidade de atores: ");
                    System.out.print("-> ");
                    input = sc.nextLine();
                    netflix.setQtdCast(Integer.parseInt(input));
                    if (netflix.getQtdCast() > 0) {
                        String[] actors = new String[netflix.getQtdCast()];
                        for (int i = 0; i < netflix.getQtdCast(); i++) {
                            System.out.println("Nome do ator" + (i + 1) + ": ");
                            System.out.print("-> ");
                            actors[i] = sc.nextLine();
                        }
                        netflix.setCast(actors);
                    }

                    // Country
                    System.out.println("Digite a quantidade de paises: ");
                    System.out.print("-> ");
                    input = sc.nextLine();
                    netflix.setQtdCountries(Integer.parseInt(input));
                    if (netflix.getQtdCountries() > 0) {
                        String[] countries = new String[netflix.getQtdCountries()];
                        for (int i = 0; i < netflix.getQtdCountries(); i++) {
                            System.out.println("Nome do pais" + (i + 1) + ": ");
                            System.out.print("-> ");
                            countries[i] = sc.nextLine();
                        }
                        netflix.setCountry(countries);
                    }

                    // Date Added
                    System.out.println("Digite a data de adicao (aaaa-mm-dd): ");
                    System.out.print("-> ");
                    netflix.setDateString(sc.nextLine());
                    netflix.setDate_added(netflix.getDateString());

                    // Release Year
                    System.out.println("Digite o ano de lancamento: ");
                    System.out.print("-> ");
                    input = sc.nextLine();
                    netflix.setRelease_year(Integer.parseInt(input));

                    // Rating
                    System.out.println("Digite a classificacao: ");
                    System.out.print("-> ");
                    netflix.setRating(sc.nextLine());

                    // Duration
                    System.out.println("Digite a duracao: ");
                    System.out.print("-> ");
                    netflix.setDuration(sc.nextLine());

                    netflix.setShow_id(Crud.getLastId() + 1); // incrementa o ultimo id utilizado e armazena no objeto

                    if (Crud.create(netflix)) {
                        System.out.println();
                        System.out.println("Registro criado com sucesso!");
                        System.out.println("ID: " + netflix.getShow_id()); // mostra o id do novo registro
                        System.out.println("____________________________________________"); // formatacao
                        System.out.println();
                        opcao = 0; // para voltar ao menu
                    } else {
                        System.out.println("Erro ao criar registro!");
                    }

                break;
                case 2: // ler registro
                    System.out.println("LER REGISTRO");

                    System.out.println("Digite o id do registro: ");
                    System.out.print("-> ");
                    input = sc.nextLine();
                    int id = Integer.parseInt(input);

                    // Type
                    System.out.println("Como deseja pesquisar? ");
                    System.out.println("1 - Hash");
                    System.out.println("2 - Arvore B");
                    System.out.print("-> ");;

                    do {
                        try {
                            input = sc.nextLine();
                            opcao = Integer.parseInt(input);
                            if (opcao < 1 || opcao > 2) {
                                System.out.println("Opcao invalida!");
                                System.out.print("-> ");
                            }
                        } catch (Exception e) { // se o usuario digitar algo que não seja um numero inteiro
                            System.out.println("Digite um numero!");
                            System.out.print("-> ");
                            sc.nextLine();
                            break;
                        }
                    } while (opcao < 1 || opcao > 2); // enquanto a opcao for invalida

                    if (opcao == 1) {
                        // 
                    } else {
                        // 
                    }

                    netflix = Crud.read(id, opcao);

                    if (netflix != null) {
                        System.out.println();
                        System.out.println("Registro encontrado!");
                        System.out.println("______________________________________"); // formatacao
                        System.out.println();
                        NetflixParser.print(netflix); // imprime o registro
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("Registro nao encontrado!");
                        System.out.println("______________________________________"); // formatacao
                        System.out.println();
                    }

                break;
                case 3: // atualizar registro
                System.out.println("ATUALIZAR REGISTRO");
                System.out.println("Digite o id do registro: ");
                System.out.print("-> ");
                input = sc.nextLine();
                int id2 = Integer.parseInt(input);

                netflix = Crud.read(id2, 1);

                if (netflix == null) {
                    System.out.println();
                    System.out.println("Registro nao encontrado!");
                    System.out.println("____________________________________________"); // formatacao
                    System.out.println();
                } else {
                    System.out.println();
                    System.out.println("Registro encontrado!");
                    System.out.println();
                    NetflixParser.print(netflix);

                    System.out.println(" O que deseja atualizar?");
                    System.out.println(" 0 - Cancelar");
                    System.out.println(" 1 - Tipo");
                    System.out.println(" 2 - Titulo");
                    System.out.println(" 3 - Diretores");
                    System.out.println(" 4 - Elenco");
                    System.out.println(" 5 - Paises");
                    System.out.println(" 6 - Data de adicao");
                    System.out.println(" 7 - Ano de lancamento");
                    // System.out.println("| 8 - Classificacao ");
                    //System.out.println("| 9 - Duracao ");
                    System.out.print("-> ");

                    do {
                        try {
                            input = sc.nextLine();
                            opcao = Integer.parseInt(input);
                            if (opcao < 0 || opcao > 9) {
                                System.out.println("Opcao invalida!");
                                System.out.print("-> ");
                            }
                        } catch (Exception e) { // se o usuario digitar algo que não seja um numero inteiro
                            System.out.println("Digite um numero!");
                            System.out.print("-> ");
                            sc.nextLine();
                            break;
                        }
                    } while (opcao < 0 || opcao > 9); // enquanto a opcao for invalida

                    switch (opcao) { // atualiza o registro
                        case 0:
                            System.out.println();
                            System.out.println("Operacao cancelada!");
                            System.out.println("____________________________________________"); // formatacao
                            System.out.println();
                        break;
                        case 1: // type
                            System.out.println();
                            System.out.println("Digite o novo tipo: ");
                            System.out.println("1 - Filme");
                            System.out.println("2 - Serie");
                            System.out.print("-> ");

                            do {
                                try {
                                    input = sc.nextLine();
                                    opcao = Integer.parseInt(input);
                                    if (opcao < 1 || opcao > 2) {
                                        System.out.println("Opcao invalida!");
                                        System.out.print("-> ");
                                    }
                                } catch (Exception e) { // se o usuario digitar algo que não seja um numero inteiro
                                    System.out.println("Digite um numero!");
                                    System.out.print("-> ");
                                    sc.nextLine();
                                    break;
                                }
                            } while (opcao < 1 || opcao > 2); // enquanto a opcao for invalida

                            if (opcao == 1) {
                                netflix.setType("Movies");
                            } else {
                                netflix.setType("Series");
                            }
                        break;
                        case 2: // title
                            System.out.println();
                            System.out.println("Digite o novo titulo: ");
                            System.out.print("-> ");
                            netflix.setTitle(sc.nextLine());        
                        break;
                        case 3: // directors
                            System.out.println();
                            System.out.println("Digite a nova quantidade de diretores: ");
                            System.out.print("-> ");
                            input = sc.nextLine();
                            netflix.setQtdDirectors(Integer.parseInt(input));
                            String[] directors2 = new String[netflix.getQtdDirectors()];
                            for (int i = 0; i < netflix.getQtdDirectors(); i++) {
                                System.out.println("Novo nome do diretor" + (i + 1) + ": ");
                                System.out.print("-> ");
                                directors2[i] = sc.nextLine();
                            }
                            netflix.setDirector(directors2);
                        break;
                        case 4: // cast
                            System.out.println();
                            System.out.println("Digite a nova quantidade de atores: ");
                            System.out.print("-> ");
                            input = sc.nextLine();
                            netflix.setQtdCast(Integer.parseInt(input));
                            String[] actors2 = new String[netflix.getQtdCast()];
                            for (int i = 0; i < netflix.getQtdCast(); i++) {
                                System.out.println("Novo nome do ator" + (i + 1) + ": ");
                                System.out.print("-> ");
                                actors2[i] = sc.nextLine();
                            }
                            netflix.setCast(actors2);
                        break;
                        case 5: // country
                            System.out.println();
                            System.out.println("Digite a nova quantidade de paises: ");
                            System.out.print("-> ");
                            input = sc.nextLine();
                            netflix.setQtdCountries(Integer.parseInt(input));
                            String[] countries2 = new String[netflix.getQtdCountries()];
                            for (int i = 0; i < netflix.getQtdCountries(); i++) {
                                System.out.println("Novo nome do pais" + (i + 1) + ": ");
                                System.out.print("-> ");
                                countries2[i] = sc.nextLine();
                            }
                            netflix.setCountry(countries2);
                        break;
                        case 6: // date_added
                            System.out.println();
                            System.out.println("Digite a nova data de adicao (aaaa-mm-dd): ");
                            System.out.print("-> ");
                            netflix.setDateString(sc.nextLine());
                            netflix.setDate_added(netflix.getDateString());
                        break;
                        case 7: // release_year
                            System.out.println();
                            System.out.println("Digite o novo ano de lancamento: ");
                            System.out.print("-> ");
                            input = sc.nextLine();
                            netflix.setRelease_year(Integer.parseInt(input));
                        break;
                        case 8: // rating
                            System.out.println();
                            System.out.println("Digite a nova classificacao: ");
                            System.out.print("-> ");
                            netflix.setRating(sc.nextLine());
                        break;
                        case 9: // duration
                            System.out.println();
                            System.out.println("Digite a nova duracao: ");
                            System.out.print("-> ");
                            netflix.setDuration(sc.nextLine());
                        break;
                    }

                    if (opcao != 0) { // se o usuario nao quiser voltar
                        if (Crud.update(netflix)) {
                            System.out.println();
                            System.out.println("Registro atualizado com sucesso!");
                            System.out.println("____________________________________________"); // formatacao
                            System.out.println();
                            System.out.println("-------------REGISTRO ATUALIZADO-------------");
                            NetflixParser.print(netflix);
                        } else {
                            System.out.println();
                            System.out.println("Erro ao atualizar registro!");
                            System.out.println("____________________________________________"); // formatacao
                            System.out.println();
                        }
                    }
                }
                break;
                case 4: // excluir registro
                    System.out.println("EXCLUIR REGISTRO");
                    System.out.println("Digite o id do registro: ");
                    System.out.print("-> ");
                    input = sc.nextLine();
                    int id3 = Integer.parseInt(input);

                    netflix = Crud.read(id3, 1);

                    if (netflix == null) {
                        System.out.println();
                        System.out.println("Registro nao encontrado!");
                        System.out.println("__________________________________________"); // formatacao
                        System.out.println();
                    } else {
                        System.out.println();
                        System.out.println("Registro encontrado!");
                        System.out.println("__________________________________________"); // formatacao
                        System.out.println();
                        NetflixParser.print(netflix); // imprime o registro encontrado

                        System.out.println("Deseja realmente excluir esse registro?");
                        System.out.println("1 - Sim");
                        System.out.println("2 - Nao");
                        System.out.print("-> ");

                        do {
                            try {
                                input = sc.nextLine();
                                opcao = Integer.parseInt(input);
                                if (opcao < 1 || opcao > 2) {
                                    System.out.println("Opcao invalida!");
                                    System.out.print("-> ");
                                }
                            } catch (Exception e) { // se o usuario digitar algo que não seja um numero inteiro
                                System.out.println("Digite um numero!");
                                System.out.print("-> ");
                                sc.nextLine();
                                break;
                            }
                        } while (opcao < 1 || opcao > 2); // enquanto a opcao for invalida

                        if (opcao == 1) {
                            if (Crud.delete(netflix)) {
                                System.out.println();
                                System.out.println("Registro excluido com sucesso!");
                                System.out.println("__________________________________________"); // formatacao
                                System.out.println();
                            } else {
                                System.out.println();
                                System.out.println("Erro ao excluir registro!");
                                System.out.println("__________________________________________"); // formatacao
                                System.out.println();
                            }
                        } else {
                            System.out.println("Operacao cancelada!");
                            System.out.println("__________________________________________"); // formatacao
                            System.out.println();
                        }
                    }
                break;
                
                 /*case 5: // Ordenar registros
                     System.out.println("____________ORDENAR____________");
                     System.out.println("Deseja realmente ordenar os registros?");
                     System.out.println("-> 1 - Sim");
                     System.out.println("-> 2 - Não");
                     System.out.print("-> ");

                     do {
                         try {
                             opcao = sc.nextInt();
                             if(opcao < 1 || opcao > 2) System.out.println("-> Opção inválida!");
                         } catch (Exception e) {
                             System.out.println("-> Digite um número!");
                             sc.next();
                             break;
                         }
                     } while (opcao < 1 || opcao > 2); // Enquanto a opção for inválida continua no loop

                     if(opcao == 1) { // Se a opção for 1, intercala os registros                        
                         if(sort.ordenar(raf)) System.out.println("\n-> Intercalado com sucesso!");
                         else System.out.println("\n-> Erro ao intercalar!");
                        
                     }
                     else System.out.println("-> Cancelado!\n");
                 break;*/
                case 6: // Pesquisar no hash
                    Crud.getHashInfo();

                    System.out.println("HASH ");
                    System.out.println("--       |");
                    System.out.println("0 - Cancelar");
                    System.out.println("1 - Valor de pGlobal");
                    System.out.println("2 - Quantidade de buckets");
                    System.out.println("3 - Endereco de um registro");
                    System.out.print("-> ");

                    do {
                        try {
                            input = sc.nextLine();
                            opcao = Integer.parseInt(input);
                            if (opcao < 0 || opcao > 3) {
                                System.out.println("Opcao invalida!");
                                System.out.print("-> ");
                            }
                        } catch (Exception e) { // se o usuario digitar algo que não seja um numero inteiro
                            System.out.println("Digite um numero!");
                            System.out.print("-> ");
                            sc.nextLine();
                            break;
                        }
                    } while (opcao < 0 || opcao > 3); // enquanto a opcao for invalida

                    switch (opcao) {
                        case 0:
                            System.out.println();
                            System.out.println("Operacao cancelada!");
                            System.out.println("____________________________________________"); // formatacao
                            System.out.println();
                        break;
                        case 1:
                            System.out.println();
                            System.out.println("Valor de pGlobal: " + Crud.getHashPGlobal());
                            System.out.println("__________________________________________"); // formatacao
                            System.out.println();
                        break;
                        case 2:
                            System.out.println();
                            System.out.println("Quantidade de buckets: " + Crud.getHashQtdBuckets());
                            System.out.println("__________________________________________"); // formatacao
                            System.out.println();
                        break;
                        case 3:
                            System.out.println();
                            System.out.println("Digite o id do registro: ");
                            System.out.print("-> ");
                            input = sc.nextLine();
                            int id4 = Integer.parseInt(input);
                            long endRegistro = Crud.findHash(id4);

                            System.out.println("Registro encontrado!");
                            System.out.println();
                            System.out.println("ID: " + id4);
                            System.out.println("Endereco: " + endRegistro);
                            System.out.println("______________________________________"); // formatacao
                            System.out.println();
                        break;
                    }

                break;
                case 7:
                    Crud.getArvoreInfo();

                    System.out.println("ARVORE B");
                    System.out.println("0 - Cancelar");
                    System.out.println("1 - Endereco da raiz");
                    System.out.println("2 - Quantidade de paginas");
                    System.out.println("3 - Endereco de um registro ");
                    System.out.print("-> ");

                    do {
                        try {
                            input = sc.nextLine();
                            opcao = Integer.parseInt(input);
                            if (opcao < 0 || opcao > 3) {
                                System.out.println("Opcao invalida!");
                                System.out.print("-> ");
                            }
                        } catch (Exception e) { // se o usuario digitar algo que não seja um numero inteiro
                            System.out.println("Digite um numero!");
                            System.out.print("-> ");
                            sc.nextLine();
                            break;
                        }
                    } while (opcao < 0 || opcao > 3); // enquanto a opcao for invalida

                    switch (opcao) {
                        case 0:
                            System.out.println();
                            System.out.println("Operacao cancelada!");
                            System.out.println("____________________________________________"); // formatacao
                            System.out.println();
                        break;
                        case 1:
                            System.out.println();
                            System.out.println("Endereco da Raiz: " + Crud.getArvoreRaiz());
                            System.out.println("__________________________________________"); // formatacao
                            System.out.println();
                        break;
                        case 2:
                            System.out.println();
                            System.out.println("Quantidade de paginas: " + Crud.getArvoreQtdPgs());
                            System.out.println("__________________________________________"); // formatacao
                            System.out.println();
                        break;
                        case 3:
                            System.out.println();
                            System.out.println("Digite o id do registro: ");
                            System.out.print("-> ");
                            input = sc.nextLine();
                            int id4 = Integer.parseInt(input);
                            long endRegistro = Crud.findArvore(id4);

                            System.out.println("Registro encontrado!");
                            System.out.println();
                            System.out.println("ID: " + id4);
                            System.out.println("Endereco: " + endRegistro);
                            System.out.println("______________________________________"); // formatacao
                            System.out.println();
                        break;
                    }

                
                     case 8:
                    System.out.println("Escolha o tipo de lista invertida:");
                    System.out.println("1) Pelo tipo (Movies/Series)");
                    System.out.println("2) Pelo ano de lançamento");

                    input = sc.nextLine();
                    int tipoLista = Integer.parseInt(input);
                    //int tipoLista = sc.nextInt();

                    if(tipoLista == 1){
                        System.out.print("\n-> Digite o tipo (Movies/Series): ");
                        String tipo = sc.nextLine();
                        if(lista.listarTipo(raf, tipo)) {
                            System.out.println("\n-> Listado com sucesso!");
                        }else {
                            System.out.println("\n-> Erro ao listar!");
                        }

                    }else if(tipoLista == 2){
                        System.out.print("\n-> Digite o ano de lançamento: ");
                        input = sc.nextLine();
                        int lancamento = Integer.parseInt(input);
                        if(lista.listarLancamento(raf, lancamento)) {
                            System.out.println("\n-> Listado com sucesso!");
                        } else {
                            System.out.println("\n-> Erro ao listar!");
                        }
                    }else{
                        System.out.println("2) Pelo ano de lançamento");
                    }
                break; 
            }
        }
        
        sc.close();

    }

}