import java.util.HashMap;

public class Rangee {
    private static int last_id = 0;
    private static int n;

    private int rangeeId;
    private HashMap<Lot, Integer> lotCaseMap = new HashMap<>();
    private int[] tabLotId;

    public Rangee() {
        this.rangeeId = last_id++;
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
        return (HashMap<Lot, Integer>) lotCaseMap;
    }

    public int[] getTabLotId() {
        return tabLotId;
    }

    /** Trouve l'indice dans le tableau <code>tabLotId</code> ou on peut ranger <code>lot</code>.
     * @param lot le nouveau <code>lot</code> a receptionner
     * @return l'indice dans le tableau s'il y a de l'espace contigu, -1 sinon
     * @author Zakaria Ybeggazene
     * @version 1.0
     */
    public int indiceRanger(Lot lot) {
        int space = 0, j = 0;
        while (space != lot.getVolume() && j < Rangee.n) {
            if(tabLotId[j] == -1) space++;
            else space = 0;
            j++;
        }
        return space == lot.getVolume() ? j - space : -1;
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
        System.out.println("RangeeID : "+ rangeeId);
        for (int i = 0; i < n; i++) {
            if(tabLotId[i] == -1) System.out.print("|  ");
            else System.out.print("|"+String.format("%02d",tabLotId[i]));
        }
        System.out.println("|");
    }

    public boolean lotInitial(Lot lot, int caseDebut) {
        int space = 0, j = 0;
        while (space != lot.getVolume() && caseDebut+j < Rangee.n) {
            if(tabLotId[caseDebut+j] == -1) space++;
            else return false;
            j++;
        }
        if(space == lot.getVolume()) {
            rangerLot(lot, caseDebut);
            return true;
        } else return false;
    }
}
