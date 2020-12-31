package source;

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
    private static int strategieRangement;
    private static int strategiePersonnel;
    private static int nombreCommandesMeubleRecues = 0;
    private static int nombreCommandesMeubleAcceptees = 0;
    private static int nombreConsignesNouveauLot = 0;
    private static int nombreLotsRecus = 0;
    private static int nombreLotsRefuses = 0;
    private static int nombreRecrutements = 0;
    private static int nombreLicenciements = 0;

    /**
     * Execute la simulation de l'entrepot en appelant <code>launchBiilomo()</code>.
     *
     * @param args - unused
     * @see #launchBiilomo()
     */
    public static void main(String[] args) {
        launchBiilomo();
    }

    /**
     * Appelee au debut du programme. Elle initialise l'entrepot, fait choisir le mode a l'utilisateur (mode console
     * ou mode fichier texte) et pour chaque mode elle execute une consigne par pas de temps et plusieurs actions
     * peuvent suivre la consigne.
     *
     * @see #initEntrepot()
     * @see #consigneModeConsole(Entrepot)
     * @see #parseConsigne(String, Entrepot)
     * @see #actionsParPas(Entrepot)
     */
    private static void launchBiilomo() {
        System.out.println("\u001B[36mBienvenue sur Biilomo !\u001B[0m");
        System.out.println("\u001B[33m(Ecrivez \"quit()\" a tout moment pour quitter le programme)\u001B[0m");
        Entrepot entrepot = initEntrepot();
        System.out.println("Veuillez choisir un mode :");
        System.out.println("(1) Mode console\t(2) Mode fichier texte");
        int mode = Keyin.inInt(">>", Arrays.asList(1, 2));
        switch (mode) {
            case 1:
                System.out.println("\u001B[34mVous avez choisi le mode console !\u001B[0m");
                int choice = 1;
                int pasDeTemps = 0;
                while (choice == 1) {
                    System.out.println("---------\nTemps : " + pasDeTemps);
                    System.out.println("Tresorerie actuelle : " + String.format("%,.2f€", entrepot.getTresorerie()));
                    consigneModeConsole(entrepot);
                    actionsParPas(entrepot);
                    System.out.println("\u001B[34mChoisissez :\u001B[0m\n" +
                            "(1) Passer au prochain pas de temps\t" +
                            "(2) Arreter la simulation");
                    choice = Keyin.inInt(">>", Arrays.asList(1, 2));
                    pasDeTemps++;
                }
                afficherBilan(entrepot);
                break;
            case 2:
                System.out.println("\u001B[34mVous avez choisi le mode fichier texte !\u001B[0m");
                while (true) {
                    try {
                        Keyin.printPrompt("Entrez le chemin du fichier de simulation :\n" +
                                "(\"src/Simulation.txt\" pour le fichier deja existant dans le projet)\n>>");
                        String chemin = Keyin.inString();
                        File file = new File(chemin);
                        if (file.isFile()) {
                            Stream<String> s = Files.lines(file.toPath());
                            s.forEach(l -> {
                                parseConsigne(l, entrepot);
                                actionsParPas(entrepot);
                            });
                            System.out.println("\u001B[34mFin des consignes du fichier de simulation\u001B[0m\n");
                            afficherBilan(entrepot);
                            System.out.print("\u001B[34mFinir la simulation en saisissant \"quit()\" " +
                                    "ou saisir autre chose pour choisir un autre fichier et continuer la simulation.\u001B[0m\n>> ");
                            Keyin.inString();
                        }
                    } catch (IOException e) {
                        System.out.println("\u001B[31mUne erreur relative au fichier s'est produite.\u001B[0m");
                    }
                }
        }
    }

    /**
     * Affiche un bilan de la simulation a la fin de cette derniere.
     *
     * @param entrepot l'instance de <code>Entrepot</code>
     */
    private static void afficherBilan(Entrepot entrepot) {
        System.out.println("\u001B[36mBilan :\u001B[0m");
        System.out.println("Tresorerie finale : " + String.format("%,.2f€", entrepot.getTresorerie()));
        System.out.println("Salaires totaux : " + String.format("%,.2f€", entrepot.getSalairesTotaux()));
        System.out.println("Somme des prix des meubles construits : " + String.format("%,.2f€", entrepot.getTotalPrixMeubles()));
        System.out.println("Balance : " + String.format("%,.2f€", (entrepot.getTotalPrixMeubles() - entrepot.getSalairesTotaux())));
        System.out.println("# consignes nouveau lot : " + nombreConsignesNouveauLot);
        System.out.println("# lots recus : " + nombreLotsRecus);
        System.out.println("# lots refuses : " + nombreLotsRefuses);
        System.out.println("# commandes meuble recues : " + nombreCommandesMeubleRecues);
        System.out.println("# commandes meuble acceptees : " + nombreCommandesMeubleAcceptees);
        System.out.println("# recrutements  : " + nombreRecrutements);
        System.out.println("# licenciements : " + nombreLicenciements);
        System.out.println("---------------------------------------------------------------------");
    }

    /**
     * Initialise les valeurs de depart de l'entrepot. Appelee par <code>launchBiilomo()</code>
     *
     * @return une instance de <code>Entrepot</code> initialisee comme indique par l'utilisateur
     * @see Entrepot
     * @see #launchBiilomo()
     */
    private static Entrepot initEntrepot() {
        System.out.println("\u001B[34mCommencez par specifier les conditions initiales de l'entrepot\u001B[0m");
        int m = Keyin.inInt("Nombre de rangees de depart (m) :\n>>", null);
        int n = Keyin.inInt("Nombre d'intervalles de depart (n) :\n>>", null);
        double tresorerie = Keyin.inDouble("Tresorerie de depart (en Euro) :\n>>");
        Entrepot entrepot = new Entrepot(m, n, tresorerie);
        System.out.println("Strategie de rangement des lots :\n" +
                "(1) Ranger dans le premier espace contigu retrouve pouvant recevoir le lot (methode naive)\n" +
                "(2) Ranger dans l'espace contigu de volume le plus proche au volume du lot");
        strategieRangement = Keyin.inInt(">>", Arrays.asList(1, 2));
        System.out.println("Strategie de gestion du personnel :\n" +
                "(1) Gerer le personnel manuellement (laisser tout a l'utilisateur)\n" +
                "(2) Recruter un membre du personnel automatiquement a chaque fois qu'une consigne est refusee " +
                "par manque de personnel\n    (-personnel apte a faire la consigne refusee\n     -ne recrute jamais un chef" +
                " stock puisqu'il fait exactement le meme travail que les ouvrier mais paye plus\n     -recrute des " +
                "ouvriers des specialites manquantes)");
        strategiePersonnel = Keyin.inInt(">>", Arrays.asList(1, 2));
        System.out.println("Voulez-vous initialiser le personnel ?\n(1) Oui\t(2) Non");
        int answer = Keyin.inInt(">>", Arrays.asList(1, 2));
        if (answer == 1) {
            while (answer == 1) {
                System.out.println("\u001B[34mCreer un chef d'equipe :\u001B[0m");
                Keyin.printPrompt("Entrez le nom du chef d'equipe :\n>>");
                String nom = Keyin.inString().trim();
                Keyin.printPrompt("Entrez le prenom du chef d'equipe :\n>>");
                String prenom = Keyin.inString().trim();
                System.out.println("Type du chef\n(1) Chef Stock\t(2) Chef Brico");
                int type = Keyin.inInt(">>", Arrays.asList(1, 2));
                ChefEquipe chefEquipe;
                if (type == 1) chefEquipe = new ChefStock(nom, prenom);
                else chefEquipe = new ChefBrico(nom, prenom);
                System.out.println("Voulez-vous lui associer des ouvriers ?\n(1) Oui\t(2) Non");
                int ouvAns = Keyin.inInt(">>", Arrays.asList(1, 2));
                if (ouvAns == 1) {
                    int numOuv = 0;
                    while (ouvAns == 1 && numOuv <= 4) {
                        System.out.println("Creer un ouvrier dans l'equipe de " + chefEquipe.getNom() +
                                " " + chefEquipe.getPrenom() + " :");
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
                        System.out.println("\u001B[34mL'ouvrier " + nom + " " + prenom + " a ete ajoute a l'equipe de "
                                + chefEquipe.getNom() + " " + chefEquipe.getPrenom() + "\u001B[0m");
                        if (++numOuv != 4) {
                            System.out.println("Voulez-vous ajouter un autre ouvrier ?\n(1) Oui\t(2) Non");
                            ouvAns = Keyin.inInt(">>", Arrays.asList(1, 2));
                        } else {
                            ouvAns = 2;
                            System.out.println("\u001B[34mEquipe complete !\u001B[0m");
                        }
                    }
                }
                entrepot.recruterChefEquipe(chefEquipe);
                System.out.println("\u001B[34mLe chef d'equipe " + chefEquipe.getNom() + " "
                        + chefEquipe.getPrenom() + " a ete cree avec succes\u001B[0m");
                System.out.println("Voulez-vous ajouter un autre chef d'equipe ?\n(1) Oui\t(2) Non");
                answer = Keyin.inInt(">>", Arrays.asList(1, 2));
            }
        }
        System.out.println("Voulez-vous initialiser l'etat des rangees de l'entrepot ?\n(1) Oui\t(2) Non");
        answer = Keyin.inInt(">>", Arrays.asList(1, 2));
        if (answer == 1) {
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
                            IntStream.rangeClosed(0, entrepot.getM() - 1).boxed().collect(Collectors.toList()));
                    int caseDebut = Keyin.inInt("Entrez le numero (a partir de 0) de la case dans " +
                                    "la rangee a partir de laquelle le lot sera stocke :\n>>",
                            IntStream.rangeClosed(0, entrepot.getN() - 1).boxed().collect(Collectors.toList()));
                    initPossible = entrepot.getRangee(idRangee).lotInitial(lot, caseDebut);
                    if (initPossible) System.out.println("\u001B[34mLe lot a ete initialise avec succes\u001B[0m");
                    else System.out.println("\u001B[31mLe lot ne peut pas etre initialise a " +
                            "l'emplacement choisi\u001B[0m");
                }
                System.out.println("Voulez-vous ajouter un autre lot de depart ?\n(1) Oui\t(2) Non");
                answer = Keyin.inInt(">>", Arrays.asList(1, 2));
            }
        }
        System.out.println("\u001B[34mVous avez initialise l'entrepot avec succès\u001B[0m");
        System.out.print("Cliquez sur Entrer pour commencer la simulation ...");
        Keyin.inString();
        return entrepot;
    }

    /**
     * Demande a l'utilisateur ce qu'il veut faire dans la console et execute la consigne (mode console).
     *
     * @param entrepot l'instance de <code>Entrepot</code>
     * @see Entrepot
     * @see #launchBiilomo()
     */
    private static void consigneModeConsole(Entrepot entrepot) {
        System.out.println("Consigne recue :\n(1) Nouveau Lot\t(2) Commande Meuble\t(3) Rien");
        int cons = Keyin.inInt(">>", Arrays.asList(1, 2, 3));
        switch (cons) {
            case 1:
                nombreConsignesNouveauLot++;
                System.out.println("\u001B[34mCaracteristiques du lot a recevoir :\u001B[0m");
                Keyin.printPrompt("Entrez le type (nom) du lot (premiere lettre en majuscule) :\n>>");
                String nom = Keyin.inString();
                int volume = Keyin.inInt("Entrez le volume du lot :\n>>", null);
                double poids = Keyin.inDouble("Entrez le poids d'une unite de volume :\n>>");
                double prix = Keyin.inDouble("Entrez le prix (en Euro) d'une unite de volume :\n>>");
                try {
                    if (strategieRangement == 1) entrepot.recevoirLotNaive(new Lot(nom, volume, poids, prix));
                    else entrepot.recevoirLot(new Lot(nom, volume, poids, prix));
                    System.out.println("\u001B[34mLot recu avec succes\u001B[0m");
                    nombreLotsRecus++;
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    nombreLotsRefuses++;
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
                    addEntry = Keyin.inInt(">>", Arrays.asList(1, 2));
                }
                Meuble meuble;
                try {
                    meuble = new Meuble(nomMeuble,
                            PieceMaison.getPieceWhereNomIs(pieceMaison.toUpperCase().trim()),
                            dureeConst,
                            listeLots);
                    nombreCommandesMeubleRecues++;
                    entrepot.monterMeuble(meuble);
                    System.out.println("\u001B[34mCommande de meuble acceptee\u001B[0m");
                    nombreCommandesMeubleAcceptees++;
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
     * Interprete une ligne du fichier de simulation comme une consigne et l'execute (mode fichier texte).
     *
     * @param line     une ligne du fichier de simulation
     * @param entrepot l'instance de <code>Entrepot</code>
     * @see Entrepot
     * @see #launchBiilomo()
     */
    private static void parseConsigne(String line, Entrepot entrepot) {
        try {
            String[] sTab = line.split(" ");
            if (sTab.length < 2) throw new IllegalArgumentException("\u001B[31mFormat du fichier incorrect\u001B[0m");
            int consigneId = Integer.parseInt(sTab[0]);
            System.out.println("---------\nTemps : " + (consigneId - 1));
            System.out.println("Tresorerie actuelle : " + String.format("%,.2f€", entrepot.getTresorerie()));
            switch (sTab[1]) {
                case "rien":
                    System.out.println("\u001B[34mAucune consigne recue...\u001B[0m");
                    break;
                case "lot":
                    nombreConsignesNouveauLot++;
                    if (sTab.length != 6)
                        throw new IllegalArgumentException("\u001B[31mLigne : " + line + "\nFormat du fichier incorrect\u001B[0m");
                    Integer.parseInt(sTab[5]);
                    Double.parseDouble(sTab[3]);
                    Double.parseDouble(sTab[4]);
                    System.out.println("\u001B[34mCaracteristiques du lot a recevoir :\u001B[0m");
                    System.out.println("Le type (nom) du lot : " + sTab[2]);
                    System.out.println("Le volume du lot : " + sTab[5]);
                    System.out.println("Le poids d'une unite de volume : " + sTab[3]);
                    System.out.println("Le prix (en Euro) d'une unite de volume : " + sTab[4]);
                    try {
                        if (strategieRangement == 1) entrepot.recevoirLotNaive(new Lot(sTab[2],
                                Integer.parseInt(sTab[5]),
                                Double.parseDouble(sTab[3]),
                                Double.parseDouble(sTab[4])));
                        else entrepot.recevoirLot(new Lot(sTab[2],
                                Integer.parseInt(sTab[5]),
                                Double.parseDouble(sTab[3]),
                                Double.parseDouble(sTab[4])));
                        System.out.println("\u001B[34mLot recu avec succes\u001B[0m");
                        nombreLotsRecus++;
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                        nombreLotsRefuses++;
                    }
                    break;
                case "meuble":
                    if (sTab.length < 7 || sTab.length % 2 == 0) throw new IllegalArgumentException("\u001B[31mLigne : "
                            + line + "\nFormat du fichier incorrect\u001B[0m");
                    Integer.parseInt(sTab[4]);
                    for (int i = 6; i < sTab.length; i += 2) {
                        Integer.parseInt(sTab[i]);
                    }
                    System.out.println("\u001B[34mCommande de meuble recue :\u001B[0m");
                    System.out.println("Nom (type) : " + sTab[2]);
                    System.out.println("Piece de la maison a laquelle il est associe : " + sTab[3]);
                    System.out.println("Duree de construction (en pas de temps) : " + sTab[4]);
                    System.out.println("Lots necessaires a sa construction :");
                    HashMap<String, Integer> listeLots = new HashMap<>();
                    for (int i = 5; i < sTab.length; i += 2) {
                        listeLots.put(sTab[i], Integer.parseInt(sTab[i + 1]));
                        System.out.println("{Type : \"" + sTab[i] + "\" ; Qt : \"" + sTab[i + 1] + "\"}");
                    }
                    nombreCommandesMeubleRecues++;
                    try {
                        entrepot.monterMeuble(new Meuble(sTab[2],
                                PieceMaison.getPieceWhereNomIs(sTab[3]),
                                Integer.parseInt(sTab[4]),
                                listeLots));
                        System.out.println("\u001B[34mCommande de meuble acceptee\u001B[0m");
                        nombreCommandesMeubleAcceptees++;
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                default:
                    throw new IllegalArgumentException("\u001B[31mLigne : " + line + "\nFormat du fichier incorrect\u001B[0m");
            }
        } catch (NumberFormatException e) {
            System.out.println("\u001B[31mLigne : " + line + "\nValeur attendue : Nombre (int ou double)\u001B[0m");
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            //throw new IOException("Ligne : "+ line +"\n\u001B[31mFile format not correct\u001B[0m");
        }
    }

    /**
     * Decrit toutes les possibilites de l'utilisateur a chaque pas de temps de la simulation (dans les deux modes).
     *
     * @param entrepot l'instance de <code>Entrepot</code>
     * @see Entrepot
     * @see #launchBiilomo()
     */
    private static void actionsParPas(Entrepot entrepot) {
        int action;
        boolean licenciement = false;
        boolean recrutement = false;
        if (strategiePersonnel == 2) {
            if (entrepot.isPersoBricoManquant()) {
                ChefEquipe chefEquipe = new ChefBrico("NomChef" + Personnel.getNext_id(), "PrenomChef" + Personnel.getNext_id());
                entrepot.recruterChefEquipe(chefEquipe);
                System.out.println("\u001B[36mBienvenue sur Biilomo !\u001B[0m");
                System.out.println("\u001B[36mStrategie : \u001B[0m\u001B[34mLe chef d'equipe " + chefEquipe.getNom() + " "
                        + chefEquipe.getPrenom() + " a ete recrute avec succes\u001B[0m");
                recrutement = true;
                nombreRecrutements++;
                entrepot.togglePersoBricoManquant();
            }
            if (entrepot.isPersoStockManquant() && !recrutement) {
                try {
                    ChefEquipe chefEquipe = entrepot.getChefEqNonPleine();
                    chefEquipe.addOuvrier(new Ouvrier("NomOuvrier" + Personnel.getNext_id(),
                            "PrenomOuvrier" + Personnel.getNext_id(), chefEquipe.getIdentifiant(),
                            entrepot.getSpecialiteManquante()));
                    System.out.println("\u001B[36mStrategie : \u001B[0m\u001B[34mL'ouvrier d'identifiant " +
                            (Personnel.getNext_id() - 1) + " a ete ajoute a l'equipe de "
                            + chefEquipe.getNom() + " " + chefEquipe.getPrenom() + "\u001B[0m");
                    recrutement = true;
                    nombreRecrutements++;
                    entrepot.togglePersoStockManquant();
                } catch (IllegalArgumentException | IllegalStateException e) {
                    ChefEquipe chefEquipe = new ChefBrico("NomChef" + Personnel.getNext_id(), "PrenomChef" + Personnel.getNext_id());
                    entrepot.recruterChefEquipe(chefEquipe);
                    System.out.println("\u001B[36mStrategie : \u001B[0m\u001B[34mLe chef d'equipe " +
                            chefEquipe.getNom() + " " + chefEquipe.getPrenom() + " a ete recrute avec succes\u001B[0m");
                    recrutement = true;
                    nombreRecrutements++;
                }
            }
        }
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
                    System.out.println("Entrez l'identifiant du lot a deplacer :");
                    int identifiantLot = Keyin.inInt(">>", IntStream.rangeClosed(0, Lot.getNext_id()).boxed().collect(Collectors.toList()));
                    System.out.println("Entrez le numero de la rangee dans laquelle vous souhaitez deplacer le lot :");
                    int numRangee = Keyin.inInt(">>", IntStream.rangeClosed(0, entrepot.getM() - 1).boxed().collect(Collectors.toList()));
                    System.out.println("Entrez le numero de l'intervalle dans lequel vous souhaitez ranger le lot :");
                    int numIntervalle = Keyin.inInt(">>", IntStream.rangeClosed(0, entrepot.getN() - 1).boxed().collect(Collectors.toList()));
                    try {
                        entrepot.deplacerLot(identifiantLot, numRangee, numIntervalle);
                        System.out.println("\u001B[34mLe lot d'identifiant " + identifiantLot +
                                " a ete deplace avec succes\u001B[0m");
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 3:
                    System.out.println("Entrez l'identifiant du lot a supprimer :");
                    int idASupp = Keyin.inInt(">>", IntStream.rangeClosed(0, Lot.getNext_id()).boxed().collect(Collectors.toList()));
                    try {
                        entrepot.supprimerLot(idASupp);
                        System.out.println("\u001B[34mLe lot d'identifiant " + idASupp +
                                " a ete supprime avec succes\u001B[0m");
                    } catch (IllegalStateException e) {
                        System.out.println(e.getMessage());
                    }
                    break;
                case 4:
                    entrepot.afficherPersonnel();
                    break;
                case 5:
                    if (!recrutement) {
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
                            nombreRecrutements++;
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
                                nombreRecrutements++;
                            } catch (IllegalArgumentException | IllegalStateException e) {
                                System.out.println(e.getMessage());
                            }
                        }
                    } else System.out.println("\u001B[31mUn seul recrutement par pas de temps possible\u001B[0m");
                    break;
                case 6:
                    if (!licenciement) {
                        System.out.println("Voulez-vous licencier un chef d'equipe ou un ouvrier ?\n(1) Chef d'equipe\t(2) Ouvrier");
                        int personnelALicencier = Simulation.Keyin.inInt(">>", Arrays.asList(1, 2));
                        try {
                            if (personnelALicencier == 1) {
                                int id = Keyin.inInt("Entrez l'identifiant du chef d'equipe que vous souhaitez licencier :\n" +
                                                "(Entrez 0 si vous ne le connaissez pas)\n>>",
                                        IntStream.rangeClosed(0, Personnel.getNext_id()).boxed().collect(Collectors.toList()));
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
                                nombreLicenciements++;
                            } else { // personnelALicencier == 2
                                int idChef = Keyin.inInt("Entrez l'identifiant du chef de l'ouvrier que vous souhaitez licencier :\n" +
                                                "(Entrez 0 si vous ne le connaissez pas)\n>>",
                                        IntStream.rangeClosed(0, Personnel.getNext_id()).boxed().collect(Collectors.toList()));
                                int idOuv = Keyin.inInt("Entrez l'identifiant de l'ouvrier que vous souhaitez licencier :\n" +
                                                "(Entrez 0 si vous ne le connaissez pas)\n>>",
                                        IntStream.rangeClosed(0, Personnel.getNext_id()).boxed().collect(Collectors.toList()));
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
                                nombreLicenciements++;
                            }
                        } catch (IllegalArgumentException e) {
                            System.out.println(e.getMessage());
                        }
                    } else System.out.println("\u001B[31mUn seul licenciement par pas de temps possible\u001B[0m");
                    break;
                default:
            }
        } while (action != 0);
        System.out.println("Paiement du personnel en cours...");
        entrepot.payerPersonnel();
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
                    if (possibleValues != null) {
                        if (!possibleValues.contains(i))
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
                    if (d <= 0) throw new IllegalArgumentException("\u001B[31mSaisie erronee." +
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