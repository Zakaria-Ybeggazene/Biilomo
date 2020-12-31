package source;

import java.util.HashMap;
import java.util.Map;

public class Rangee {
    private static int next_id = 0;
    private static int n;

    private final int rangeeId;
    private final HashMap<Lot, Integer> lotCaseMap = new HashMap<>();
    private final int[] tabLotId;


    public Rangee() {
        this.rangeeId = next_id++;
        tabLotId = new int[Rangee.n];
        for (int i = 0; i < Rangee.n; i++) tabLotId[i] = -1;
    }

    public static int getN() {
        return n;
    }

    public static void setN(int n) {
        Rangee.n = n;
    }

    public int getRangeeId() {
        return rangeeId;
    }

    public HashMap<Lot, Integer> getLotCaseMap() {
        return lotCaseMap;
    }

    public int[] getTabLotId() {
        return tabLotId;
    }


    /**
     * Trouve le premier indice dans le tableau <code>tabLotId</code> ou on peut ranger <code>lot</code>.
     *
     * @param lot le nouveau <code>lot</code> a receptionner
     * @return l'indice dans le tableau s'il y a de l'espace contigu, -1 sinon
     */
    public int indiceRanger(Lot lot) {
        int space = 0, j = 0;
        while (space != lot.getVolume() && j < Rangee.n) {
            if (tabLotId[j] == -1) space++;
            else space = 0;
            j++;
        }
        return space == lot.getVolume() ? j - space : -1;
    }


    /**
     * private method just for printing, used in methods below.
     */
    private void printRangeeModifiee() {
        System.out.println("RangeeID : " + rangeeId);
        for (int i = 0; i < n; i++) {
            if (tabLotId[i] == -1) System.out.print("|  ");
            else System.out.print("|" + String.format("%02d", tabLotId[i]));
        }
        System.out.println("|");
    }

    /**
     * Range le <code>lot<code/> dans cette rangee a la <code>caseDebut</code>.
     *
     * @param lot       le nouveau <code>lot</code> a receptionner
     * @param caseDebut case a partir de laquelle on commence a ranger le <code>lot</code>
     */
    public void rangerLot(Lot lot, int caseDebut) {
        for (int i = 0; i < lot.getVolume(); i++) tabLotId[caseDebut + i] = lot.getLotId();
        lotCaseMap.put(lot, caseDebut);
        printRangeeModifiee();
    }

    /**
     * Supprime un lot d'une rangee.
     *
     * @param lot       le lot a supprimer
     * @param caseDebut l'indice du lot
     */
    public void retirerLot(Lot lot, int caseDebut) {
        for (int j = 0; j < lot.getVolume(); j++) tabLotId[caseDebut + j] = -1;
        lotCaseMap.remove(lot, caseDebut);
        printRangeeModifiee();
    }


    /**
     * Reduit le volume d'un lot dans une rangee.
     *
     * @param lot            le lot dont le volume est a reduire
     * @param caseDebut      l'indice du tableau <code>tabLotId</code> ou commence le lot
     * @param volumeAReduire un entier qui indique de combien on va reduire le lot
     */
    public void reduireLot(Lot lot, int caseDebut, int volumeAReduire) {
        for (int j = 0; j < volumeAReduire; j++) {
            tabLotId[caseDebut + j] = -1;
        }
        lotCaseMap.remove(lot, caseDebut);
        lot.setVolume(lot.getVolume() - volumeAReduire);
        lotCaseMap.put(lot, caseDebut + volumeAReduire);
        printRangeeModifiee();
    }


    /**
     * Verifie si on a la place suffisante dans la rangee pour stocker le lot initialise.
     *
     * @param lot       le lot a stocker
     * @param caseDebut l'indice du tableau <code>tabLotId</code> a partir duquel on veut ranger le lot dans la rangee
     * @return true si on peut stocker <code>lot</code> a partir de la case d'indice <code>caseDebut</code>, false sinon
     */
    public boolean lotInitial(Lot lot, int caseDebut) {
        int space = 0, j = 0;
        while (space != lot.getVolume() && caseDebut + j < Rangee.n) {
            if (tabLotId[caseDebut + j] == -1) space++;
            else return false;
            j++;
        }
        if (space == lot.getVolume()) {
            rangerLot(lot, caseDebut);
            return true;
        } else return false;
    }


    /**
     * Indique si on peut deplacer un lot ou non.
     *
     * @param lot       le lot a deplacer
     * @param caseDebut la case vers laquelle on veut deplacer le lot (il s'agit d'un indice de tableau)
     * @return un boolean qui indique si oui ou non on peut deplacer ce lot vers cette case
     */
    public boolean peutDeplacer(Lot lot, int caseDebut) {
        int space = 0, j = 0;
        while (space != lot.getVolume() && caseDebut + j < Rangee.n) {
            if (tabLotId[caseDebut + j] == -1 || tabLotId[caseDebut + j] == lot.getLotId()) space++;
            else return false;
            j++;
        }
        return space == lot.getVolume();
    }

    /**
     * Rajoute dans <code>mapEspacesVides</code> les couples ((RangeeId, EmplacementDansRangee), volume) des volumes vides
     * disponibles dans cette rangee.
     *
     * @param mapEspacesVides HashMap dans laquelle rajouter les Map.Entry
     */
    public void getEspacesVides(HashMap<Map.Entry<Integer, Integer>, Integer> mapEspacesVides) {
        int i = 0;
        while (i < n) {
            if (tabLotId[i] == -1) {
                int space = 1;
                boolean isEmpty = true;
                int j;
                for (j = i + 1; j < n && isEmpty; j++) {
                    if (tabLotId[j] == -1) space++;
                    else isEmpty = false;
                }
                mapEspacesVides.put(Map.entry(rangeeId, i), space);
                i = j;
            } else i++;
        }
    }
}
