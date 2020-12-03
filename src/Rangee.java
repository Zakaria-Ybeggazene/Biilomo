import java.util.HashMap;

public class Rangee {
    private static int last_id = 0;

    private int rangeeId;
    private HashMap<Lot, Integer> lotCaseMap = new HashMap<>();
    private int[] tabLotId = new int[Entrepot.getN()];

    public Rangee() {
        this.rangeeId = last_id++;
    }

    public int getRangeeId() {
        return rangeeId;
    }
}
