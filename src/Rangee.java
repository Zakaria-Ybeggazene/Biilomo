import java.util.HashMap;

public class Rangee {
    private static int last_id = 0;

    private int rangeeId;
    private HashMap<Lot, Integer> lotCaseMap = new HashMap<>();
    private int[] tabLotId = new int[Entrepot.getN()];

    public Rangee() {
        this.rangeeId = last_id++;
        for (int i = 0; i < Entrepot.getN(); i++) tabLotId[i] = -1;
    }

    public int getRangeeId() {
        return rangeeId;
    }

    public HashMap<Lot, Integer> getLotCaseMap() {
        return (HashMap<Lot, Integer>) lotCaseMap.clone();
    }

    public int[] getTabLotId() {
        return tabLotId.clone();
    }

    /** Trouve l'indice dans le tableau <code>tabLotId</code> ou on peut ranger <code>lot</code>.
     * @param lot le nouveau <code>lot</code> a receptionner
     * @return l'indice dans le tableau s'il y a de l'espace contigu, -1 sinon
     * @author Zakaria Ybeggazene
     * @version 1.0
     */
    public int indiceRanger(Lot lot) {
        int space = 0, j = 0;
        while (space != lot.getVolume() && j < Entrepot.getN()) {
            if(tabLotId[j] == -1) space++;
            else space = 0;
            j++;
        }
        return space == lot.getVolume() ? j - space - 1 : -1;
    }

    /** Range le <code>lot<code/> dans cette rangee a la <code>caseDebut</code>.
     * @param lot le nouveau <code>lot</code> a receptionner
     * @param caseDebut case a partir de laquelle on commence a ranger le <code>lot</code>
     * @author Zakaria Ybeggazene
     * @version 1.0
     */
    public void rangerLot(Lot lot, int caseDebut) {
        for (int i = 0; i < lot.getVolume(); i++) tabLotId[caseDebut+i] = lot.getLotId();
        lotCaseMap.put(lot, caseDebut);
    }
}
