import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Entrepot {
    /**
     * Nombre de rangees dans l'entrepot.
     */
    private int m; //nombre de rangees
    /**
     * Nombre d'intervalles dans chaque rangees (longueur en metres de chaque rangee).
     */
    private int n; //nombre d'intervalles

    /**
     * Tresorerie de l'entrepot en double. Non nulle au depart.
     */
    private double tresorerie;

    /**
     * Liste des chefs d'equipe.
     */
    private ArrayList<ChefEquipe> chefsEquipe = new ArrayList<>();

    /**
     * Tableau des m rangees de l'entrepot.
     */
    private Rangee[] tabRangees;

    private HashMap<Integer, Integer> persoIndispo = new HashMap<>();

    /**
     * Constructeur de la classe <code>Entrepot</code> qui initialise la tresorerie
     * @param tresorerie la tresorerie initiale de l'<code>entrepot</code>.
     */
    public Entrepot(int m, int n, double tresorerie) {
        this.m = m;
        this.n = n;
        this.tresorerie = tresorerie;
        Rangee.setN(n);
        tabRangees = new Rangee[m];
        for (int i = 0; i < m; i++) tabRangees[i] = new Rangee();
    }

    public int getM() {
        return m;
    }

    public int getN() {
        return n;
    }

    public double getTresorerie() {
        return tresorerie;
    }

    // retourne le premier chef d'equipe dont l'equipe n'est pas complete
    public ChefEquipe getChefEqNonPleine() throws NullPointerException {
        for (ChefEquipe chef: chefsEquipe) {
            if(chef.getNumOuvriers() != 4) return chef;
        }
        throw new NullPointerException("\u001B[31mAucune equipe n'est disponible\u001B[0m");
    }

    public ArrayList<ChefEquipe> getChefsEquipe() {
        return (ArrayList<ChefEquipe>) chefsEquipe;
    }

    public void setM(int m) {
        this.m = m;
    }

    public void setN(int n) {
        this.n = n;
    }

    public Rangee getRangee(int idRangee) {
        return tabRangees[idRangee];
    }

    /** Affiche la <code>tresorerie</code> de l'<code>entrepot</code> et les <code>lots</code> contenus
     *  dans toutes les <code>rangees</code>.
     * @author Zakaria Ybeggazene
     * @version 1.0
     */
    public void inventaire() {
        System.out.println("---------------------------------------------------------------");
        System.out.println("Tresorerie : "+String.format("%,.2f€",this.tresorerie));
        System.out.println("L'entrepot possede "+m+" rangees de longueur "+n+" dont le contenu est le suivant :");
        for(int i=0; i<m; i++) {
            System.out.println("\nRangee N° "+i+" :");
            for (int j = 0; j < n; j++) {
                if (tabRangees[i].getTabLotId()[j] == -1) System.out.print("|  ");
                else System.out.print("|" + String.format("%02d", tabRangees[i].getTabLotId()[j]));
            }
            System.out.println("|");
            for (Map.Entry<Lot, Integer> entry : tabRangees[i].getLotCaseMap().entrySet()) {
                System.out.println("LotID : " + entry.getKey().getLotId() + "\nType : " + entry.getKey().getNom() +
                        "\nVolume : " + entry.getKey().getVolume() + "\nPoids unitaire : " +
                        entry.getKey().getPoidsUnit() + "\nPrix unitaire : " + String.format("%,.2f€",
                        entry.getKey().getPrixUnit()) + "\nPosition dans rangee : de " + entry.getValue() +" a " +
                        (entry.getValue() + entry.getKey().getVolume() - 1)+"\n--------------------------------------");
            }
        }
        System.out.println("---------------------------------------------------------------");
    }

    /** Appelee a la fin de chaque pas de temps pour payer tout le <code>personnel</code>.
     * @author Zakaria Ybeggazene
     * @version 1.0
     */
    public void payerPersonnel() {
        for (ChefEquipe chef: chefsEquipe) {
            this.tresorerie -= 10;
            Ouvrier[] ouvriers = chef.getEquipe();
            for(int i = 0; i < 4; i++) {
                if(ouvriers[i] == null) break;
                else this.tresorerie -= 5;
            }
        }
    }

    /** Rend le premier membre disponible du <code>personnel</code> pouvant gerer les stocks.
     * @return objet de type <code>Personnel</code> qui est le premier membre du personnel pouvant
     * gerer les stocks, <code>null</code> sinon.
     * @author Zakaria Ybeggazene
     * @version 1.0
     * @see #chefsEquipe
     * @see #recevoirLot(Lot)
     * @see Personnel
     */
    public Personnel persoStockDispo() {
        boolean personnelDispo = false;
        Personnel personnel = null;
        Iterator<ChefEquipe> it = chefsEquipe.iterator();
        while (it.hasNext() && !personnelDispo) {
            ChefEquipe chefEquipe = it.next();
            if(chefEquipe instanceof ChefStock && !persoIndispo.containsKey(chefEquipe.getIdentifiant())) {
                personnelDispo = true;
                personnel = chefEquipe;
            }
            else if(chefEquipe.getNumOuvriers() != 0){
                Ouvrier[] ouvriers = it.next().getEquipe();
                for (int i = 0; i < 4; i++) {
                    if (ouvriers[i] == null) break;
                    else if (!persoIndispo.containsKey(ouvriers[i].getIdentifiant())) {
                        personnelDispo = true;
                        personnel = ouvriers[i];
                    }
                }
            }
        }
        return personnel;
    }

    /** Rend le premier membre disponible du <code>personnel</code> pouvant monter un meuble de type
     * <code>specialite</code>.
     * @return objet de type <code>Personnel</code> qui est le premier membre du personnel pouvant
     * monter un meuble de type <code>specialite</code>, <code>null</code> sinon.
     * @author Zakaria Ybeggazene
     * @version 1.0
     * @see #chefsEquipe
     * @see #monterMeuble(Meuble)
     * @see Personnel
     */
    public Personnel persoBricoDispo(PieceMaison specialite) {
        boolean personnelDispo = false;
        Personnel personnel = null;
        Iterator<ChefEquipe> it = chefsEquipe.iterator();
        while (it.hasNext() && !personnelDispo) {
            ChefEquipe chefEquipe = it.next();
            if(chefEquipe instanceof ChefBrico && !persoIndispo.containsKey(chefEquipe.getIdentifiant())) {
                personnelDispo = true;
                personnel = chefEquipe;
            }
            else if(chefEquipe.getNumOuvriers() != 0){
                Ouvrier[] ouvriers = it.next().getEquipe();
                for (int i = 0; i < 4; i++) {
                    if (ouvriers[i] == null) break;
                    else if (!persoIndispo.containsKey(ouvriers[i].getIdentifiant()) && ouvriers[i].getSpecialite().equals(specialite)) {
                        personnelDispo = true;
                        personnel = ouvriers[i];
                    }
                }
            }
        }
        return personnel;
    }

    /** Essaye de ranger <code>lot<code/> dans l'une des <code>rangee</code> de l'<code>entrepot</code>
     * s'il y a de l'espace contigu et du personnel.
     * @param lot le nouveau <code>lot</code> a receptionner
     * @return <code>true</code> si l'on a pu ranger le <code>lot</code>, <code>false</code> sinon
     * @author Zakaria Ybeggazene
     * @version 1.0
     * @see #chefsEquipe
     * @see #tabRangees
     * @see #persoStockDispo()
     * @see Rangee
     * @see Lot
     * @see Personnel
     * @see Rangee#indiceRanger(Lot)
     * @see Rangee#rangerLot(Lot, int)
     */
    public void recevoirLot(Lot lot) throws IllegalStateException {
        //On verifie d'abord si on a le personnel
        Personnel personnel = persoStockDispo();
        if(personnel == null) {
            throw new IllegalStateException("\u001B[31mLot rejete.\u001B[0m\n" +
                    "Personnel apte a recevoir le lot : \u001B[31mIndisponible\u001B[0m");
        } //Si on n'a pas de personnel, le lot est rejete
        else { //On verifie si on a un espace contigu assez grand pour stocker le lot
            int i = 0, caseDebut = 0;
            boolean espaceDispo = false;
            while(i < m && !espaceDispo) {
                caseDebut = tabRangees[i].indiceRanger(lot);
                if(caseDebut != -1) espaceDispo = true;
                i++;
            }
            int numRangee = i-1;
            if(!espaceDispo) {
                throw new IllegalStateException("\u001B[31mLot rejete.\u001B[0m\n" +
                        "Espace contigu necessaire pour recevoir le lot : \u001B[31mIndisponible\u001B[0m");
            } //Si on n'a pas l'espace contigu necessaire
            else { //on stock le lot en reservant le membre du personnel et en placant le lot a la place trouvee
                persoIndispo.put(personnel.getIdentifiant(), 1);
                tabRangees[numRangee].rangerLot(lot, caseDebut);
            }
        }
    }

    public void deplacerLot(Lot lot) throws IllegalStateException {
        //On verifie d'abord si on a le personnel necessaire au deplacement d'un lot (cad au moins une personne dispo)
        Personnel personnel = persoStockDispo();
        if (personnel == null) {
            throw new IllegalStateException("\u001B[31mImpossible de deplacer le lot.\u001B[0m\n" +
                    "Personnel apte a deplacer le lot : \u001B[31mIndisponible\u001B[0m");
        } else {

        }
    }

    public void monterMeuble(Meuble meuble) throws IllegalStateException{
        //On verifie d'abord si on a le personnel disponible necessaire a la fabrication d'un meuble
        Personnel personnel = persoBricoDispo(meuble.getPieceMaison());
        if(personnel == null) {
            throw new IllegalStateException("\u001B[31mCommande de meuble rejetee.\u001B[0m\n" +
                    "Personnel apte a honnorer la commande : \u001B[31mIndisponible\u001B[0m");
        }
        else {
            HashMap<String, Integer> lotsNecessaires = meuble.getListeLots();

        }
    }

    public void recruterChefEquipe(ChefEquipe chefEquipe) {
        if(chefEquipe != null) chefsEquipe.add(chefEquipe);
    }

    public void licencierChefEquipe(int id, String nomPrenomChefEquipe) throws IllegalArgumentException {
            boolean found = false;
            Iterator<ChefEquipe> it = chefsEquipe.iterator();
            while(!found && it.hasNext()) {
                ChefEquipe chefEquipe = it.next();
                if((id == 0 && (chefEquipe.getNom()+" "+chefEquipe.getPrenom()).equalsIgnoreCase(nomPrenomChefEquipe))
                        || (id != 0 && chefEquipe.getIdentifiant() == id)) {
                    found = true;
                    if(chefEquipe.getNumOuvriers() != 0) {
                        Ouvrier[] ouvriers = chefEquipe.getEquipe();
                        for (int i = 0; i < 4; i++) {
                            if(ouvriers[i] != null) {
                                boolean isAssigned = false;
                                Iterator<ChefEquipe> iterator = chefsEquipe.iterator();
                                while (!isAssigned && iterator.hasNext()) {
                                    ChefEquipe chefReciever = iterator.next();
                                    if (chefReciever.getNumOuvriers() < 4) {
                                        chefReciever.addOuvrier(ouvriers[i]);
                                        isAssigned = true;
                                    }
                                }
                                if(!isAssigned) System.out.println("\u001B[31mL'ouvrier "+ ouvriers[i].getNom() +" "+
                                        ouvriers[i].getPrenom()+" ID : "+ ouvriers[i].getIdentifiant() +
                                        ", n'a pas pu etre affecte a un autre chef d'equipe. Ouvrier licencie\u001B[0m");
                            }
                        }
                    }
                    chefsEquipe.remove(chefEquipe);
                    if(id != 0) break;
                }
            }
            if(!found) {
                if(id == 0) throw new IllegalArgumentException("\u001B[31mLicenciement impossible.\n" +
                    "Aucun chef d'equipe nomme \u001B[0m"+ nomPrenomChefEquipe +
                    " \u001B[31mn'est disponible\u001B[0m");
                else throw new IllegalArgumentException("\u001B[31mLicenciement impossible.\n" +
                        "Aucun chef d'equipe avec l'identifiant \u001B[0m"+ id +
                        " \u001B[31mn'est disponible\u001B[0m");
            }
    }

    public void licencierOuvrier(int idChef, int idOuv, String nomPrenomOuvrier) throws IllegalArgumentException {
            boolean chefFound = false;
            boolean found = false;
            Iterator<ChefEquipe> it = chefsEquipe.iterator();
            while(!found && it.hasNext()) {
                ChefEquipe chefEquipe = it.next();
                if((idChef == 0 || chefEquipe.getIdentifiant() == idChef) && chefEquipe.getNumOuvriers() != 0) {
                    Ouvrier[] ouvriers = chefEquipe.getEquipe();
                    for(int i = 0; i < 4; i++) {
                        if(ouvriers[i] != null) {
                            if(idOuv != 0 && ouvriers[i].getIdentifiant() == idOuv) {
                                found = true;
                                chefEquipe.removeOuvrier(i);
                            } else if(idOuv == 0 && (ouvriers[i].getNom()+" "+ouvriers[i].getPrenom()).equalsIgnoreCase(nomPrenomOuvrier)) {
                                found = true;
                                chefEquipe.removeOuvrier(i);
                            }
                        }
                    }
                }
                if(idChef != 0 && chefEquipe.getIdentifiant() == idChef) {
                    chefFound = true;
                    break;
                }
            }
            if(!found) {
                if(!chefFound) throw new IllegalArgumentException("\u001B[31mLicenciement impossible.\n" +
                    "Aucun chef d'equipe avec l'identifiant \u001B[0m"+ idChef +
                    " \u001B[31mn'est disponible\u001B[0m");
                else if(idOuv != 0) throw new IllegalArgumentException("\u001B[31mLicenciement impossible.\n" +
                        "Aucun ouvrier avec l'identifiant \u001B[0m"+ idOuv +
                        " \u001B[31mn'est disponible\u001B[0m");
                else throw new IllegalArgumentException("\u001B[31mLicenciement impossible.\n" +
                            "Aucun ouvrier nomme \u001B[0m"+ nomPrenomOuvrier +
                            " \u001B[31mn'est disponible\u001B[0m");
            }
    }

    public void afficherPersonnel() {
        System.out.println("---------------------------------------------------------------");
        if(chefsEquipe.isEmpty()) System.out.println("\u001B[31mAucun membre du personnel a afficher\u001B[0m");
        for (ChefEquipe chefEquipe : chefsEquipe) {
            System.out.println(chefEquipe.getIdentifiant()+" "+chefEquipe.getNom()+" "+
                    chefEquipe.getPrenom()+" : "+chefEquipe.getClass().getName()+" | # Ouvriers : "
                    +chefEquipe.getNumOuvriers());
            if(chefEquipe.getNumOuvriers() != 0) {
                Ouvrier[] ouvriers = chefEquipe.getEquipe();
                for (int i = 0; i < 4; i++) {
                    if(ouvriers[i] != null) {
                        System.out.println("|-- "+ouvriers[i].getIdentifiant()+" "+ouvriers[i].getNom()+" "+
                                ouvriers[i].getPrenom()+" : "+ouvriers[i].getSpecialite().getNom());
                    }
                }
            }
        }
        System.out.println("---------------------------------------------------------------");
    }
    public void updatePersonnel() {
        Iterator<Map.Entry<Integer, Integer>> it = persoIndispo.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = it.next();
            if(pair.getValue() == 1) it.remove();
            else pair.setValue(pair.getValue() - 1);
        }
    }
}
