import java.util.AbstractMap.SimpleEntry;
import java.util.HashMap;

public class Meuble {
    private static int last_id = 0;

    private int meubleId;
    private String nom; //represente le type du meuble ex : Table
    private PieceMaison pieceMaison;
    private int dureeConstruction;
    private HashMap<String, Integer> listeLots;

    /**
     * Constructeur de la classe <code>Meuble</code>.
     * @param nom
     * @param pieceMaison
     * @param dureeConstruction
     * @param listeLots
     */
    public Meuble(String nom, PieceMaison pieceMaison, int dureeConstruction,
                  HashMap<String, Integer> listeLots) {
        this.meubleId = last_id++;
        this.nom = nom;
        this.pieceMaison = pieceMaison;
        this.dureeConstruction = dureeConstruction;
        this.listeLots = listeLots;
    }

    public int getMeubleId() {
        return meubleId;
    }

    public String getNom() {
        return nom;
    }

    public PieceMaison getPieceMaison() {
        return pieceMaison;
    }

    public int getDureeConstruction() {
        return dureeConstruction;
    }

    public HashMap<String, Integer> getListeLots() {
        return listeLots;
    }
}
