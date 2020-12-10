import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Simulation {

    public static void main(String[] args) {
        launchMenu();
        //Entrepot e = new Entrepot(5000);
        //launchFile(e,"Simulation.txt");
    }

    private static void parse(String s, Entrepot e) throws IOException {
        try {
            String[] sTab = s.split(" ");
            switch(sTab[1]) {
                case "rien":
                    break;
                case "lot":
					/*e.recevoirLot(new Lot(Integer.valueOf(sTab[5]),
									  Double.valueOf(sTab[3]),
									  Double.valueOf(sTab[4]),
									  sTab[2]));*/
                    break;
                case "meuble":
					/*Meuble meuble = new Meuble(sTab[2],
						PieceMaison.valueOf(sTab[3]),
						Integer.valueOf(sTab[4]));
					for(int i = 5; i < sTab.length;i+=2)
						meuble.addToComposition(sTab[i],
									Integer.valueOf(sTab[i+1]));
					e.commandeMeuble(meuble);*/
            }
        }catch(Exception ex) {
            throw new IOException("File format not correct");
        }
    }

    private static void launchMenu() {
        System.out.println("\u001B[36mBienvenue sur Biilomo !\u001B[0m");
        System.out.println("\u001B[33m(Ecrivez \"quit()\" a tout moment pour quitter le programme)\u001B[0m");
        Entrepot entrepot = initEntrepot();
        System.out.println("Veuillez choisir un mode :");
        System.out.println("(1) Mode console\t(2) Mode fichier texte");
        int mode = Keyin.inInt(">>", Arrays.asList(1,2));
        switch (mode) {
            case 1:
                System.out.println("\u001B[34mVous avez choisi le mode console !\u001B[0m");
                int choice = 1;
                int i = 0;
                while (choice == 1) {
                    System.out.println("---------\nTemps : "+i);
                    System.out.println("Tresorerie actuelle : "+String.format("%,.2f€", entrepot.getTresorerie()));
                    consigneModeConsole(entrepot);
                    System.out.println("Paiement du personnel en cours...");
                    entrepot.payerPersonnel();
                    int action = 0;
                    do {
                        System.out.println("Actions possibles :\n" +
                                "(1) Afficher inventaire\t" +
                                "(2) Deplacer lot\t" +
                                "(3) Supprimer lot\t\n" +
                                "(4) Recruter un nouveau membre du personnel\t" +
                                "(5) Licencier un membre du personnel\t\n" +
                                "(0) Aucune des precedentes");
                                action = Keyin.inInt(">>", Arrays.asList(0, 1, 2, 3, 4, 5));
                        switch (action) {
                            case 1:
                                entrepot.inventaire();
                                break;
                            case 2:
                                break;
                            case 3:
                                break;
                            case 4:
                                break;
                            case 5:
                                break;
                            default:
                        }
                    } while (action != 0);
                    System.out.println("\u001B[34mChoisissez :\u001B[0m\n" +
                            "(1) Passer au prochain pas de temps\t" +
                            "(2) Arreter la simulation");
                    choice = Keyin.inInt(">>", Arrays.asList(1,2));
                }
                break;
            case 2:
                System.out.println("\u001B[34mVous avez choisi le mode fichier texte !\u001B[0m");
                break;
        }
    }

    private static Entrepot initEntrepot() {
        System.out.println("\u001B[34mCommencez par specifier les conditions initiales de l'entrepot\u001B[0m");
        int m = Keyin.inInt("Nombre de rangees de depart (m) :\n>>", null);
        int n = Keyin.inInt("Nombre d'intervalles de depart (n) :\n>>", null);
        double tresorerie = Keyin.inDouble("Tresorerie de depart (en Euro) :\n>>");
        Entrepot entrepot = new Entrepot(m, n, tresorerie);
        System.out.println("Voulez-vous initialiser le personnel ?\n(1) Oui\t(2) Non");
        int answer = Keyin.inInt(">>", Arrays.asList(1,2));
        if(answer == 1) {
            while (answer == 1) {
                System.out.println("\u001B[34mCreer un chef d'equipe :\u001B[0m");
                Keyin.printPrompt("Entrez le nom du chef d'equipe :\n>>");
                String nom = Keyin.inString();
                Keyin.printPrompt("Entrez le prenom du chef d'equipe :\n>>");
                String prenom = Keyin.inString();
                System.out.println("Type du chef\n(1) Chef Stock\t(2) Chef Brico");
                int type = Keyin.inInt(">>", Arrays.asList(1,2));
                ChefEquipe chefEquipe;
                if(type == 1) chefEquipe = new ChefStock(nom, prenom);
                else chefEquipe = new ChefBrico(nom, prenom);
                System.out.println("Voulez-vous lui associer des ouvriers ?\n(1) Oui\t(2) Non");
                int ouvAns = Keyin.inInt(">>", Arrays.asList(1,2));
                if(ouvAns == 1) {
                    int numOuv = 0;
                    while (ouvAns == 1 && numOuv <= 4) {
                        System.out.println("Creer un ouvrier dans l'equipe de "+chefEquipe.getNom()+
                                " "+chefEquipe.getPrenom()+" :");
                        Keyin.printPrompt("Entrez le nom de l'ouvrier :\n>>");
                        nom = Keyin.inString();
                        Keyin.printPrompt("Entrez le prenom de l'ouvrier :\n>>");
                        prenom = Keyin.inString();
                        Keyin.printPrompt("Entrez la specialite de l'ouvrier parmi :" +
                                "\n('Cuisine', 'Chambre', 'Salle a Manger', 'Salon', 'Salle de bain', 'WC')\n");
                        String specialite = Keyin.inString(Arrays.asList("CUISINE", "CHAMBRE", "SALLE A MANGER",
                                "SALON", "SALLE DE BAIN", "WC"));
                        try {
                            chefEquipe.addOuvrier(new Ouvrier(nom, prenom, chefEquipe.getIdentifiant(),
                                    PieceMaison.getPieceWhereNomIs(specialite.toUpperCase().trim())));
                        } catch (IllegalArgumentException ex) {
                            System.out.println(ex.getMessage());
                        }
                        System.out.println("\u001B[34mL'ouvrier "+nom+" "+prenom+" a ete ajoute a l'equipe de "
                        + chefEquipe.getNom() +" "+ chefEquipe.getPrenom() +"\u001B[0m");
                        if(++numOuv != 4) {
                            System.out.println("Voulez-vous ajouter un autre ouvrier ?\n(1) Oui\t(2) Non");
                            ouvAns = Keyin.inInt(">>", Arrays.asList(1, 2));
                        } else {
                            ouvAns = 2;
                            System.out.println("\u001B[34mEquipe complete !\u001B[0m");
                        }
                    }
                }
                entrepot.recruterPersonnel(chefEquipe);
                System.out.println("\u001B[34mLe chef d'equipe "+ chefEquipe.getNom() +" "
                        + chefEquipe.getPrenom() +" a ete cree avec succes\u001B[0m");
                System.out.println("Vous-vous ajouter un autre chef d'equipe ?\n(1) Oui\t(2) Non");
                answer = Keyin.inInt(">>", Arrays.asList(1,2));
            }
        }
        System.out.println("Voulez-vous initialiser l'etat des rangees de l'entrepot ?\n(1) Oui\t(2) Non");
        answer = Keyin.inInt(">>", Arrays.asList(1,2));
        if(answer == 1) {
            while (answer == 1) {
                System.out.println("\u001B[34mCreer un lot :\u001B[0m");
                Keyin.printPrompt("Entrez le type (nom) du lot (premiere lettre en majuscule) :\n>>");
                String nom = Keyin.inString().trim();
                int volume = Keyin.inInt("Entrez le volume du lot :\n>>", null);
                double poids = Keyin.inDouble("Entrez le poids d'une unite de volume :\n>>");
                double prix = Keyin.inDouble("Entrez le prix (en Euro) d'une unite de volume :\n>>");
                Lot lot = new Lot(nom, volume, poids, prix);
                boolean initPossible = false;
                while (!initPossible) {
                    int idRangee = Keyin.inInt("Entrez le numero (a partir de 0) de la rangee dans laquelle " +
                                    "vous voulez que le lot soit initialisé :\n>>",
                            IntStream.rangeClosed(0, entrepot.getM()-1).boxed().collect(Collectors.toList()));
                    int caseDebut = Keyin.inInt("Entrez le numero (a partir de 0) de la case dans " +
                                    "la rangee a partir de laquelle le lot sera stocke :\n>>",
                            IntStream.rangeClosed(0, entrepot.getN()-1).boxed().collect(Collectors.toList()));
                    initPossible = entrepot.getRangee(idRangee).lotInitial(lot, caseDebut);
                    if(initPossible) System.out.println("\u001B[34mLe lot a ete initialise avec succes\u001B[0m");
                    else System.out.println("\u001B[31mLe lot ne peut pas etre initialise a " +
                            "l'emplacement choisi\u001B[0m");
                }
                System.out.println("Vous-vous ajouter un autre lot de depart ?\n(1) Oui\t(2) Non");
                answer = Keyin.inInt(">>", Arrays.asList(1,2));
            }
        }
        System.out.println("\u001B[34mVous avez initialise l'entrepot avec succès\u001B[0m");
        System.out.print("Cliquez sur Entrer pour commencer la simulation ...");
        Keyin.inString();
        return entrepot;
    }

    private static void consigneModeConsole(Entrepot entrepot) {
        System.out.println("Consigne recue :\n(1) Nouveau Lot\t(2) Commande Meuble\t(3) Rien");
        int cons = Keyin.inInt(">>", Arrays.asList(1,2,3));
        switch (cons) {
            case 1:
                System.out.println("\u001B[34mCaracteristiques du lot a recevoir :\u001B[0m");
                Keyin.printPrompt("Entrez le type (nom) du lot (premiere lettre en majuscule) :\n>>");
                String nom = Keyin.inString();
                int volume = Keyin.inInt("Entrez le volume du lot :\n>>", null);
                double poids = Keyin.inDouble("Entrez le poids d'une unite de volume :\n>>");
                double prix = Keyin.inDouble("Entrez le prix (en Euro) d'une unite de volume :\n>>");
                boolean recu = entrepot.recevoirLot(new Lot(nom, volume, poids, prix));
                System.out.println(recu);
                //Do smth here or do it inside recevoirLot()
                break;
            case 2:
                System.out.println("\u001B[34mCommande de meuble recue :\u001B[0m");
                Keyin.printPrompt("Entrez le nom du meuble commande :\n>>");
                String nomMeuble = Keyin.inString();
                Keyin.printPrompt("Entrez la piece de la maison a laquelle est associe le meuble parmi :" +
                        "\n('Cuisine', 'Chambre', 'Salle a Manger', 'Salon', 'Salle de bain', 'WC')\n");
                String pieceMaison = Keyin.inString(Arrays.asList("CUISINE", "CHAMBRE", "SALLE A MANGER",
                        "SALON", "SALLE DE BAIN", "WC"));
                int dureeConst = Keyin.inInt("Entrez la duree de construction du meuble :\n>>", null);
                System.out.println("Entrez la liste des lots et leurs volumes respectifs necessaires" +
                        "a la construction du meuble");
                int addEntry = 1;
                HashMap<String, Integer> listeLots = new HashMap<>();
                while (addEntry == 1) {
                    Keyin.printPrompt("Entrez le type (nom) du lot (premiere lettre en majuscule) :\n>>");
                    String type = Keyin.inString().trim();
                    int quantite = Keyin.inInt("Entrez le volume necessaire :\n>>", null);
                    listeLots.put(type, quantite);
                    System.out.println("Vous-vous ajouter un autre lot de depart ?\n(1) Oui\t(2) Non");
                    addEntry = Keyin.inInt(">>", Arrays.asList(1,2));
                }
                Meuble meuble = null;
                try {
                    meuble = new Meuble(nomMeuble,
                            PieceMaison.getPieceWhereNomIs(pieceMaison.toUpperCase().trim()),
                            dureeConst,
                            listeLots);
                } catch (IllegalArgumentException ex) {
                    System.out.println(ex.getMessage());
                }
                System.out.println("\u001B[34mCommande de meuble bien creee\u001B[0m");
                boolean peutMonter = entrepot.monterMeuble(meuble);
                System.out.println(peutMonter);
                //do smth after monterMeuble() is done
                break;
            case 3:
                System.out.println("\u001B[34mPas de consigne cette fois...\u001B[0m");
                break;
        }
    }

    private static void launchFile(Entrepot e, String string) {
        try {
            File f= new File(string);
            Stream<String> s = Files.lines(f.toPath());
            s.forEach( l -> {try {
                parse(l,e);
            }
            catch (IOException e1) {e1.printStackTrace();}});
        }
        catch(IOException ex) {
            ex.getStackTrace();
        }
    }

    static class Keyin {

        //*******************************
        //   support methods
        //*******************************
        //Method to display the user's prompt string
        static void printPrompt(String prompt) {
            System.out.print(prompt + " ");
            System.out.flush();
        }

        //Method to make sure no data is available in the
        //input stream
        static void inputFlush() {
            int dummy;
            int bAvail;

            try {
                while ((System.in.available()) != 0)
                    dummy = System.in.read();
            } catch (java.io.IOException e) {
                System.out.println("\u001B[31mSaisie erronee\u001B[0m");
            }
        }

        static String inString() {
            int aChar;
            StringBuilder s = new StringBuilder();
            boolean finished = false;

            while (!finished) {
                try {
                    aChar = System.in.read();
                    if (aChar < 0 || (char) aChar == '\n')
                        finished = true;
                    else if ((char) aChar != '\r')
                        s.append((char) aChar); // Enter into string
                } catch (java.io.IOException e) {
                    System.out.println("\u001B[31mSaisie erronee\u001B[0m");
                    finished = true;
                }
            }
            if (s.toString().equals("quit()")) System.exit(0);
            return s.toString();
        }

        static String inString(List<String> possibleValues) {
            while (true) {
                System.out.print(">> ");
                String s = inString().trim();
                try {
                    if (!possibleValues.contains(s.toUpperCase())) throw
                new IllegalArgumentException("\u001B[31mSaisie erronee. Valeur inattendue\u001B[0m");
                    return s;
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        static int inInt(String prompt, List<Integer> possibleValues) {
            while (true) {
                inputFlush();
                printPrompt(prompt);
                try {
                    int i = Integer.parseInt(inString().trim());
                    if(possibleValues != null) {
                        if(!possibleValues.contains(i))
                            throw new IllegalArgumentException("\u001B[31mSaisie erronee." +
                                    " Valeur inattendue\u001B[0m");
                        if (!possibleValues.contains(0) && i == 0)
                            throw new IllegalArgumentException("\u001B[31mSaisie erronee." +
                                " Valeur nulle\u001B[0m");
                    } else if (i <= 0)
                        throw new IllegalArgumentException("\u001B[31mSaisie erronee." +
                                " Valeur negative ou nulle\u001B[0m");
                    return i;
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mSaisie erronee. Pas un Integer\u001B[0m");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        static double inDouble(String prompt) {
            while (true) {
                inputFlush();
                printPrompt(prompt);
                try {
                    double d = Double.parseDouble(inString().trim());
                    if(d <= 0) throw new IllegalArgumentException("\u001B[31mSaisie erronee." +
                            " Valeur negative ou nulle\u001B[0m");
                    else return d;
                } catch (NumberFormatException e) {
                    System.out.println("\u001B[31mSaisie erronee. Pas un double\u001B[0m");
                } catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }

        /*static void printString(String s) {
            for (int i = 0;
                 i < s.length();
                 i++) {
                //Pause
                try {
                    Thread.sleep(100);
                    //Print a message
                    System.out.print(s.charAt(i));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
            }
            System.out.println();
        }*/
    }
}