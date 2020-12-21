import java.util.*;

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
     * Constructeur de la classe <code>Entrepot</code> qui initialise la tresorerie.
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

    /**
     * Retourne le nombre de rangees de l'entrepot.
     * @return un objet de type <code>int</code>
     */
    public int getM() {
        return m;
    }

    /**
     * Retourne le nombre d'intervalles de chaque rangée de l'entrepot.
     * @return un objet de type <code>int</code>
     */
    public int getN() {
        return n;
    }

    /**
     * Retourne la tresorie de l'entrepot.
     * @return un objet de type <code>int</code>
     */
    public double getTresorerie() {
        return tresorerie;
    }

    /**
     * Retourne le premier chef equipe dont l'equipe n'est pas complete.
     * @return un objet de type <code>ChefEquipe</code>.
     * @throws IllegalStateException Si aucun chef d'equipe ne possede une equipe non pleine
     * @see ChefEquipe
     */
    public ChefEquipe getChefEqNonPleine() throws IllegalStateException {
        for (ChefEquipe chef: chefsEquipe) {
            if(chef.getNumOuvriers() != 4) return chef;
        }
        throw new IllegalStateException("\u001B[31mAucune equipe n'est disponible\u001B[0m");
    }

    /**
     * Retourne la liste des chefs d'equipe
     * @return un objet de type <code>ArrayList</code>
     */
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
        System.out.println("L'entrepot possede "+m+" rangee(s) de longueur "+n+" dont le contenu est le suivant :");
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
                Ouvrier[] ouvriers = chefEquipe.getEquipe();
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
                Ouvrier[] ouvriers = chefEquipe.getEquipe();
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

    /**
     * Essaye de deplacer le lot d'un endroit a un autre de l'entrepot afin de creer de l'espace contigu.
     * @param id identifiant du <code>lot</code> a deplacer
     * @param numRangee indice dans <code>tabRangees</code> de la rangee vers laquelle on veut deplacer le <code>lot</code>
     * @param emplacementRangee indice dans la rangee de l'emplacement vers lequel on veut deplacer le <code>lot</code>
     * @throws IllegalStateException Si le personnel est indisponible, impossible de deplacer vers l'emplacement specifie ou
     * l'identifiant <code>id</code> ne correspond a aucun lot.
     * @see Lot
     * @see Rangee
     * @see Simulation
     */
    public void deplacerLot(int id, int numRangee, int emplacementRangee) throws IllegalStateException {
        //On verifie d'abord si on a le personnel
        Personnel personnel = persoStockDispo();
        if (personnel == null) {
            throw new IllegalStateException("\u001B[31mImpossible de deplacer le lot.\u001B[0m\n" +
                    "Personnel apte a deplacer le lot : \u001B[31mIndisponible\u001B[0m");
        }
        else {
            boolean lotFound = false;
            for (int i=0; i<m; i++){
                HashMap<Lot, Integer> lotCaseMap = tabRangees[i].getLotCaseMap();
                Iterator<Map.Entry<Lot, Integer>> iterator = lotCaseMap.entrySet().iterator();
                while (iterator.hasNext() && !lotFound) {
                    Map.Entry<Lot, Integer> entry = iterator.next();
                    if (entry.getKey().getLotId() == id) {
                        lotFound = true;
                        if(tabRangees[numRangee].peutDeplacer(entry.getKey(), emplacementRangee)) {
                            persoIndispo.put(personnel.getIdentifiant(), 1);
                            tabRangees[i].retirerLot(entry.getKey(), entry.getValue());
                            tabRangees[numRangee].rangerLot(entry.getKey(), emplacementRangee);
                        } else throw new IllegalStateException("\u001B[31mImpossible de deplacer ce lot vers " +
                                "la rangee "+ numRangee +" a l'emplacement "+ emplacementRangee +".\u001B[0m");
                    }
                }
                if(lotFound) break;
            }
            if (!lotFound) throw new IllegalStateException("\u001B[31mImpossible de deplacer ce lot." +
                    " Son identifiant ne correspond a aucun lot de l'entrepot\u001B[0m");
        }
    }


    /**
     * Supprime un lot de l'entrepot, soit pour construire un meuble soit pour faire de la place.
     * @param id l'identifiant du <code>lot</code> a supprimer
     * @throws IllegalStateException Si personnel indisponible ou l'identifiant <code>id</code> ne correspond a aucun lot.
     * @see Lot
     * @see Rangee
     * @see Simulation
     */
    public void supprimerLot(int id) throws IllegalStateException {
        //On verifie d'abord si on a le personnel necessaire
        Personnel personnel = persoStockDispo();
        if (personnel == null) {
            throw new IllegalStateException("\u001B[31mImpossible de supprimer le lot.\u001B[0m\n" +
                    "Personnel apte a supprimer le lot : \u001B[31mIndisponible\u001B[0m");
        }
        else {
            boolean lotFound = false;
            for (int i=0; i<m; i++){
                HashMap<Lot, Integer> lotCaseMap = tabRangees[i].getLotCaseMap();
                Iterator<Map.Entry<Lot, Integer>> iterator = lotCaseMap.entrySet().iterator();
                while (iterator.hasNext() && !lotFound) {
                    Map.Entry<Lot, Integer> entry = iterator.next();
                    if (entry.getKey().getLotId() == id) {
                        lotFound = true;
                        persoIndispo.put(personnel.getIdentifiant(), 1);
                        tabRangees[i].retirerLot(entry.getKey(), entry.getValue());
                    }
                }
                if(lotFound) break;
            }
            if (!lotFound) throw new IllegalStateException("\u001B[31mImpossible de supprimer ce lot." +
                    " Son identifiant ne correspond a aucun lot de l'entrepot\u001B[0m");
        }
    }

    /**
     * Reduit le volume d'un meuble dans l'entrepot. Cette methode est utilisee dans <code>monterMeuble()</code>.
     * @param id l'identifiant du meuble a reduire
     * @param volumeAReduire le nombre d'unites de volume a reduire du <code>lot</code> avec l'identifiant <code>id</code> a partir du debut
     * @throws IllegalStateException Si le personnel est indisponible ou l'identifiant <code>id</code> ne correspond a aucun lot.
     * @see #monterMeuble(Meuble)
     * @see Lot
     * @see Rangee
     * @see Simulation
     */
    public void reduireLot(int id, int volumeAReduire) throws IllegalStateException{
        //On verifie d'abord si on a le personnel necessaire
        Personnel personnel = persoStockDispo();
        if (personnel == null) {
            throw new IllegalStateException("\u001B[31mImpossible de reduire le lot.\u001B[0m\n" +
                    "Personnel apte a reduire le lot : \u001B[31mIndisponible\u001B[0m");
        }
        else {
            boolean lotFound = false;
            for (int i=0; i<m; i++){
                HashMap<Lot, Integer> lotCaseMap = tabRangees[i].getLotCaseMap();
                Iterator<Map.Entry<Lot, Integer>> iterator = lotCaseMap.entrySet().iterator();
                while (iterator.hasNext() && !lotFound) {
                    Map.Entry<Lot, Integer> entry = iterator.next();
                    if (entry.getKey().getLotId() == id) {
                        lotFound = true;
                        persoIndispo.put(personnel.getIdentifiant(), 1);
                        tabRangees[i].reduireLot(entry.getKey(), entry.getValue(), volumeAReduire);
                    }
                }
                if(lotFound) break;
            }
            if (!lotFound) throw new IllegalStateException("\u001B[31mImpossible de réduire ce lot." +
                    "Son identifiant ne correspond a aucun lot de l'entrepot\u001B[0m");
        }
    }

    /**
     * Verifie si on a le personnel et les lots necessaires a la fabrication d'un meuble.
     * Si c'est le cas, alors le meuble est construit et les lots utilises sont supprimés de l'entrepot.
     * @param meuble le meuble a monter
     * @throws IllegalStateException Si les lots necessaires a la construction du <code>meuble</code> ne sont pas satisfaits
     * ou si le personnel qui peut monter le meuble et supprimer/reduire les lots retrouves est indisponible
     * @see Meuble
     * @see Rangee
     * @see Lot
     * @see Simulation
     * @see #supprimerLot(int)
     * @see #reduireLot(int, int)
     */
    public void monterMeuble(Meuble meuble) throws IllegalStateException {
        //On verifie d'abord si on a le personnel
        Personnel personnel = persoBricoDispo(meuble.getPieceMaison());
        if(personnel == null) {
            throw new IllegalStateException("\u001B[31mCommande de meuble rejetee.\u001B[0m\n" +
                    "Personnel apte a honnorer la commande : \u001B[31mIndisponible\u001B[0m");
        }
        else {
            // On verifie a present si on a le volume de lots necessaire
            double prixMeuble = 0;
            HashMap<String, Integer> lotsNecessaires = meuble.getListeLots();
            boolean bonVolume;
            HashMap<Lot, Integer> idLotVolume = new HashMap<>();
            for (Map.Entry<String, Integer> entry : lotsNecessaires.entrySet()) {
                bonVolume = false;
                int volumeNecessaire = 0;
                for (int j=0; j<m; j++) {
                    HashMap<Lot, Integer> lotCaseMap = tabRangees[j].getLotCaseMap();
                    Iterator<Map.Entry<Lot, Integer>> iterator = lotCaseMap.entrySet().iterator();
                    while (iterator.hasNext() && volumeNecessaire < entry.getValue()) {
                        Map.Entry<Lot, Integer> kv = iterator.next();
                        if (kv.getKey().getNom().equals(entry.getKey())) {
                            volumeNecessaire += kv.getKey().getVolume();
                            idLotVolume.put(kv.getKey(), entry.getValue());
                            prixMeuble += kv.getKey().getPrixUnit() * entry.getValue();
                        }
                    }
                    if (volumeNecessaire >= entry.getValue()){
                        bonVolume = true;
                        break;
                    }
                }
                if(!bonVolume) throw new IllegalStateException("\u001B[31mCommande de meuble rejetee.\u001B[0m\n" +
                        "Volume necessaire pour le lot "+ entry.getKey() + " : \u001B[31mIndisponible\u001B[0m");
            }
            /*On assemble tous les lots trouves (en supprimant ou reduisant chaque lot)
              pour ce faire il faut du personnel stock, si on n'a pas assez, la commande de meuble est rejetee*/
            ArrayList<Integer> persoStockId = new ArrayList<>();
            for (int i = 0; i < idLotVolume.size()-1; i++) {
                Personnel perso = persoStockDispo();
                if(perso == null) throw new IllegalStateException("\u001B[31mCommande de meuble rejetee.\u001B[0m\n" +
                        "Personnel apte a rassembler les lots necessaires : \u001B[31mIndisponible\u001B[0m");
                else {
                    persoIndispo.put(perso.getIdentifiant(), 1);
                    persoStockId.add(perso.getIdentifiant());
                }
            }
            persoStockId.forEach(integer -> persoIndispo.remove(integer));
            idLotVolume.forEach((lot, volumeNecessaire) -> {
                if(lot.getVolume() == volumeNecessaire) supprimerLot(lot.getLotId());
                else reduireLot(lot.getLotId(), volumeNecessaire);
            });
            //On reserve personnel durant toute la duree de construction
            persoIndispo.put(personnel.getIdentifiant(), meuble.getDureeConstruction());
            tresorerie += prixMeuble;
        }
    }

    /**
     * Recrute un nouveau chef d'equipe en l'ajoutant a la liste des chefs d'equipe de l'<code>entrepot</code>.
     * @param chefEquipe la liste des chefs d'equipe
     */
    public void recruterChefEquipe(ChefEquipe chefEquipe) {
        if(chefEquipe != null) chefsEquipe.add(chefEquipe);
    }

    /**
     * Recherche un chef d'equipe par son identifiant ou par son nom et prenom.
     * Si ce chef existe, alors ses ouvriers sont redistribues a d'autres chefs dont l'equipe n'est pas pleine.
     * Les ouvriers ne pouvant etre redistribues sont licencies.
     * A l'issue de cette methode, le chef d'equipe est licencie et donc supprime de la liste chefsEquipe.
     * @param id L'identifiant d'un chef d'equipe
     * @param nomPrenomChefEquipe Le nom et le prenom d'un chef d'equipe
     * @throws IllegalArgumentException si aucun chef d'equipe n'a l'identifiant <code>id</code> ou le nom et prenom <code>nomPrenomChefEquipe</code>
     * @see ChefEquipe
     * @see Simulation
     */
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

    /**
     * Recherche un ouvrier en cherchant d'abord le chef a qui il est affilie
     * puis par soit son identifiant, soit par ses nom et prenom puis le licencie.
     * @param idChef l'identifiant du chef d'equipe associe a l'ouvrier recherche
     * @param idOuv l'identifiant de l'ouvrier recherche
     * @param nomPrenomOuvrier le nom et le prenom de l'ouvrier recherche
     * @throws IllegalArgumentException Si aucun chef d'equipe ne possede l'identifiant <code>idChef</code> ou aucun ouvrier ne
     * possede l'identifiant <code>idOuv</code> ou le nom et prenom <code>nomPrenomOuvrier</code>
     * @see ChefEquipe
     * @see ChefEquipe#removeOuvrier(int)
     * @see Simulation
     */
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


    /**
     * Affiche l'ensemble du personnel
     */
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

    /**
     * Appelee a la fin de chaque pas de temps pour mettre a jour la HashMap <code>personIndispo</code> qui suit les membre deja requisitionnes
     * @see Simulation
     */
    public void updatePersonnel() {
        Iterator<Map.Entry<Integer, Integer>> it = persoIndispo.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Integer> pair = it.next();
            if(pair.getValue() == 1) it.remove();
            else pair.setValue(pair.getValue() - 1);
        }
    }
}
