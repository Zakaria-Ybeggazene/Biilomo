import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
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
        System.out.println("Bienvenue sur Biilomo !");
        System.out.println("(Ecrivez \"quit()\" a tout moment pour quitter le programme)");
        System.out.println("Veuillez choisir un mode :");
        System.out.println("(1) Mode console\t(2) Mode fichier texte");
        int mode = Keyin.inInt(">>", Arrays.asList(1,2));
        switch (mode) {
            case 1:
                System.out.println("\u001B[34mVous avez choisi le mode console !\u001B[0m");
                initSim();
                break;
            case 2:
                System.out.println("\u001B[34mVous avez choisi le mode fichier texte !\u001B[0m");
                initSim();
                break;
        }
    }

    private static void initSim() {
        System.out.println("Vous allez maintenant specifier les conditions initiales de l'entrepot");
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
                String nom = Keyin.inString();
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