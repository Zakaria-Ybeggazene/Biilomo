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

    /**
     * Execute la simulation de l'entrepot.
     * @param args - unused
     */
    public static void main(String[] args) {
        launchBiilomo();
    }

    private static void launchBiilomo() {
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
                int pasDeTemps = 0;
                while (choice == 1) {
                    System.out.println("---------\nTemps : "+pasDeTemps);
                    System.out.println("Tresorerie actuelle : "+String.format("%,.2f€", entrepot.getTresorerie()));
                    consigneModeConsole(entrepot);
                    actionsParPas(entrepot);
                    System.out.println("\u001B[34mChoisissez :\u001B[0m\n" +
                            "(1) Passer au prochain pas de temps\t" +
                            "(2) Arreter la simulation");
                    choice = Keyin.inInt(">>", Arrays.asList(1,2));
                    pasDeTemps++;
                }
                break;
            case 2:
                System.out.println("\u001B[34mVous avez choisi le mode fichier texte !\u001B[0m");
                try {
                    Keyin.printPrompt("Entrez le chemin du fichier de simulation :\n" +
                            "(\"src/Simulation.txt\" pour le fichier deja existant dans le projet)\n>>");
                    String chemin = Keyin.inString();
                    File file = new File(chemin);
                    Stream<String> s = Files.lines(file.toPath());
                    s.forEach(l -> {try {
                        parseConsigne(l, entrepot);
                        actionsParPas(entrepot);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    });
                    System.out.println("\u001B[34mFin des consignes du fichier de simulation\nFin de la simulation.\u001B[0m");
                } catch (IOException e) {
                    System.out.println("\u001B[31mUne erreur relative au fichier s'est produite.\u001B[0m");
                }
                break;
        }
    }

    /**
     * Initialise les valeurs de depart de l'entrepot.
     * @return un objet de type <code>Entrepot</code>
     * @see Entrepot
     */
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
                String nom = Keyin.inString().trim();
                Keyin.printPrompt("Entrez le prenom du chef d'equipe :\n>>");
                String prenom = Keyin.inString().trim();
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
                        nom = Keyin.inString().trim();
                        Keyin.printPrompt("Entrez le prenom de l'ouvrier :\n>>");
                        prenom = Keyin.inString().trim();
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
                entrepot.recruterChefEquipe(chefEquipe);
                System.out.println("\u001B[34mLe chef d'equipe "+ chefEquipe.getNom() +" "
                        + chefEquipe.getPrenom() +" a ete cree avec succes\u001B[0m");
                System.out.println("Voulez-vous ajouter un autre chef d'equipe ?\n(1) Oui\t(2) Non");
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
                System.out.println("Voulez-vous ajouter un autre lot de depart ?\n(1) Oui\t(2) Non");
                answer = Keyin.inInt(">>", Arrays.asList(1,2));
            }
        }
        System.out.println("\u001B[34mVous avez initialise l'entrepot avec succès\u001B[0m");
        System.out.print("Cliquez sur Entrer pour commencer la simulation ...");
        Keyin.inString();
        return entrepot;
    }

    /**
     * Demande a l'utilisateur ce qu'il veut faire puis execute la consigne.
     * @param entrepot l'entrepot
     * @see Entrepot
     */
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
                try {
                    entrepot.recevoirLot(new Lot(nom, volume, poids, prix));
                    System.out.println("\u001B[34mLot recu avec succes\u001B[0m");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                }
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
                        " a la construction du meuble");
                int addEntry = 1;
                HashMap<String, Integer> listeLots = new HashMap<>();
                while (addEntry == 1) {
                    Keyin.printPrompt("Entrez le type (nom) du lot (premiere lettre en majuscule) :\n>>");
                    String type = Keyin.inString().trim();
                    int quantite = Keyin.inInt("Entrez le volume necessaire :\n>>", null);
                    listeLots.put(type, quantite);
                    System.out.println("Voulez-vous ajouter un autre lot necessaire a la construction" +
                            " du meuble ?\n(1) Oui\t(2) Non");
                    addEntry = Keyin.inInt(">>", Arrays.asList(1,2));
                }
                Meuble meuble;
                try {
                    meuble = new Meuble(nomMeuble,
                            PieceMaison.getPieceWhereNomIs(pieceMaison.toUpperCase().trim()),
                            dureeConst,
                            listeLots);
                    entrepot.monterMeuble(meuble);
                    System.out.println("\u001B[34mCommande de meuble acceptee\u001B[0m");
                } catch (IllegalArgumentException | IllegalStateException e) {
                    System.out.println(e.getMessage());
                } catch (Exception e) {
                    System.out.println("\u001B[31mUne erreur s'est produite\u001B[0m");
                }
                break;
            case 3:
                System.out.println("\u001B[34mAucune consigne recue...\u001B[0m");
                break;
        }
    }

    /**
     *
     * @param s
     * @param entrepot
     * @throws IOException
     * @throws IllegalArgumentException
     */
    private static void parseConsigne(String s, Entrepot entrepot) throws IOException, IllegalArgumentException {
        try {
            String[] sTab = s.split(" ");
            if(sTab.length < 2) throw new IllegalArgumentException("\u001B[31mFormat du fichier incorrect\u001B[0m");
            int consigneId = Integer.parseInt(sTab[0]);
            System.out.println("---------\nTemps : "+(consigneId - 1));
            System.out.println("Tresorerie actuelle : "+String.format("%,.2f€", entrepot.getTresorerie()));
            switch(sTab[1]) {
                case "rien":
                    System.out.println("\u001B[34mAucune consigne recue...\u001B[0m");
                    break;
                case "lot":
                    if(sTab.length != 6) throw new IllegalArgumentException("\u001B[31mLigne : "+ s +"\nFormat du fichier incorrect\u001B[0m");
                    Integer.parseInt(sTab[5]);
                    Double.parseDouble(sTab[3]);
                    Double.parseDouble(sTab[4]);
                    System.out.println("\u001B[34mCaracteristiques du lot a recevoir :\u001B[0m");
                    System.out.println("Le type (nom) du lot : "+sTab[2]);
                    System.out.println("Le volume du lot : "+sTab[5]);
                    System.out.println("Le poids d'une unite de volume : "+sTab[3]);
                    System.out.println("Le prix (en Euro) d'une unite de volume : "+sTab[4]);
                    try {
                        entrepot.recevoirLot(new Lot(sTab[2],
                                Integer.parseInt(sTab[5]),
                                Double.parseDouble(sTab[3]),
                                Double.parseDouble(sTab[4])));
                        System.out.println("\u001B[34mLot recu avec succes\u001B[0m");
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case "meuble":
					/*Meuble meuble = new Meuble(sTab[2],
						PieceMaison.valueOf(sTab[3]),
						Integer.valueOf(sTab[4]));
					for(int i = 5; i < sTab.length;i+=2)
						meuble.addToComposition(sTab[i],
									Integer.valueOf(sTab[i+1]));
					entrepot.commandeMeuble(meuble);*/
                default:
                    throw new IllegalArgumentException("\u001B[31mLigne : "+ s +"\nFormat du fichier incorrect\u001B[0m");
            }
        } catch (NumberFormatException e) {
            System.out.println("\u001B[31mLigne : "+ s +"\nValeur attendue : Nombre (int ou double)\u001B[0m");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace();
            //throw new IOException("Ligne : "+ s +"\n\u001B[31mFile format not correct\u001B[0m");
        }
    }

    /**
     * Decrit toutes les possibilites de l'utilisateur a chaque pas de temps de la simulation.
     * @param entrepot l'entrepot
     * @see Entrepot
     */
    private static void actionsParPas(Entrepot entrepot) {
        System.out.println("Paiement du personnel en cours...");
        entrepot.payerPersonnel();
        int action;
        boolean licenciement = false;
        boolean recrutement = false;
        do {
            System.out.println("Actions possibles :\n" +
                    "(1) Afficher inventaire\t" +
                    "(2) Deplacer lot\t" +
                    "(3) Supprimer lot\t\n" +
                    "(4) Afficher personnel\t" +
                    "(5) Recruter un nouveau membre du personnel\t" +
                    "(6) Licencier un membre du personnel\t\n" +
                    "(0) Aucune des precedentes");
            action = Keyin.inInt(">>", Arrays.asList(0, 1, 2, 3, 4, 5, 6));
            switch (action) {
                case 1:
                    entrepot.inventaire();
                    break;
                case 2:
                    break;
                case 3:
                    break;
                case 4:
                    entrepot.afficherPersonnel();
                    break;
                case 5:
                    if(!recrutement) {
                        System.out.println("Souhaitez-vous recruter un chef d'equipe ou un ouvrier ?\n(1) Chef d'equipe\t(2) Ouvrier");
                        int nouveauPersonnel = Keyin.inInt(">>", Arrays.asList(1, 2));
                        if (nouveauPersonnel == 1) {
                            Keyin.printPrompt("Entrez le nom du chef d'equipe a recruter :\n>>");
                            String nom = Keyin.inString().trim();
                            Keyin.printPrompt("Entrez le prenom du chef d'equipe a recruter :\n>>");
                            String prenom = Keyin.inString().trim();
                            System.out.println("Type du chef\n(1) Chef Stock\t(2) Chef Brico");
                            int type = Keyin.inInt(">>", Arrays.asList(1, 2));
                            ChefEquipe chefEquipe;
                            if (type == 1) chefEquipe = new ChefStock(nom, prenom);
                            else chefEquipe = new ChefBrico(nom, prenom);

                            entrepot.recruterChefEquipe(chefEquipe);
                            System.out.println("\u001B[34mLe chef d'equipe " + chefEquipe.getNom() + " "
                                    + chefEquipe.getPrenom() + " a ete recrute avec succes\u001B[0m");
                            recrutement = true;
                        } else {
                            Keyin.printPrompt("Entrez le nom de l'ouvrier a recruter :\n>>");
                            String nom = Keyin.inString().trim();
                            Keyin.printPrompt("Entrez le prenom de l'ouvrier a recruter :\n>>");
                            String prenom = Keyin.inString().trim();
                            Keyin.printPrompt("Entrez la specialite de l'ouvrier parmi :" +
                                    "\n('Cuisine', 'Chambre', 'Salle a Manger', 'Salon', 'Salle de bain', 'WC')\n");
                            String specialite = Simulation.Keyin.inString(Arrays.asList("CUISINE", "CHAMBRE", "SALLE A MANGER",
                                    "SALON", "SALLE DE BAIN", "WC"));
                            try {
                                ChefEquipe chefEquipe = entrepot.getChefEqNonPleine();
                                chefEquipe.addOuvrier(new Ouvrier(nom, prenom, chefEquipe.getIdentifiant(),
                                        PieceMaison.getPieceWhereNomIs(specialite.toUpperCase().trim())));
                                System.out.println("\u001B[34mL'ouvrier " + nom + " " + prenom + " a ete ajoute a l'equipe de "
                                        + chefEquipe.getNom() + " " + chefEquipe.getPrenom() + "\u001B[0m");
                                recrutement = true;
                            } catch (IllegalArgumentException | NullPointerException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } else System.out.println("\u001B[31mUn seul recrutement par pas de temps possible\u001B[0m");
                    break;
                case 6:
                    if(!licenciement) {
                        System.out.println("Voulez-vous licencier un chef d'equipe ou un ouvrier ?\n(1) Chef d'equipe\t(2) Ouvrier");
                        int personnelALicencier = Simulation.Keyin.inInt(">>", Arrays.asList(1, 2));
                        try {
                            if (personnelALicencier == 1) {
                                int id = Keyin.inInt("Entrez l'identifiant du chef d'equipe que vous souhaitez licencier :\n" +
                                                "(Entrez 0 si vous ne le connaissez pas)\n>>",
                                        IntStream.rangeClosed(0, Personnel.getLast_id() - 1).boxed().collect(Collectors.toList()));
                                if (id == 0) {
                                    Keyin.printPrompt("Entrez le nom du chef d'equipe que vous souhaitez licencier :\n>>");
                                    String nom = Keyin.inString().trim();
                                    Keyin.printPrompt("Entrez son prenom : \n>>");
                                    String prenom = Keyin.inString().trim();
                                    String nomPrenomChefEquipe = nom + " " + prenom;
                                    entrepot.licencierChefEquipe(id, nomPrenomChefEquipe);
                                    System.out.println("Le chef d'equipe " + nomPrenomChefEquipe +
                                            " a ete licencie");
                                } else {
                                    entrepot.licencierChefEquipe(id, null);
                                    System.out.println("Le chef d'equipe avec l'identifiant " +
                                            id + " a ete licencie");
                                }
                                licenciement = true;
                            } else { // personnelALicencier == 2
                                int idChef = Keyin.inInt("Entrez l'identifiant du chef de l'ouvrier que vous souhaitez licencier :\n" +
                                                "(Entrez 0 si vous ne le connaissez pas)\n>>",
                                        IntStream.rangeClosed(0, Personnel.getLast_id() - 1).boxed().collect(Collectors.toList()));
                                int idOuv = Keyin.inInt("Entrez l'identifiant de l'ouvrier que vous souhaitez licencier :\n" +
                                                "(Entrez 0 si vous ne le connaissez pas)\n>>",
                                        IntStream.rangeClosed(0, Personnel.getLast_id() - 1).boxed().collect(Collectors.toList()));
                                if (idOuv == 0) {
                                    Keyin.printPrompt("Entrez le nom de l'ouvrier que vous souhaitez licencier :\n>>");
                                    String nom = Keyin.inString().trim();
                                    Keyin.printPrompt("Entrez son prenom :\n>>");
                                    String prenom = Keyin.inString().trim();
                                    String nomPrenomOuvrier = nom + " " + prenom;
                                    entrepot.licencierOuvrier(idChef, idOuv, nomPrenomOuvrier);
                                    System.out.println("L'ouvrier " + nomPrenomOuvrier +
                                            " a ete licencie");
                                } else {
                                    entrepot.licencierOuvrier(idChef, idOuv, null);
                                    System.out.println("L'ouvrier avec l'identifiant " +
                                            idOuv + " a ete licencie");
                                }
                                licenciement = true;
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    } else System.out.println("\u001B[31mUn seul licenciement par pas de temps possible\u001B[0m");
                    break;
                default:
            }
        } while (action != 0);
        System.out.println("Mise a jour du personnel en cours...");
        entrepot.updatePersonnel(); //mettre a jour le personnel disponible
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
    }
}