import java.util.HashMap;

public class Meuble {
    private static int next_id = 0;

    /**
     * Le numero d'identifiant du meuble.
     */
    private final int meubleId;
    /**
     * Le nom du meuble, qui represente egalement son type. Exemple: "Table"
     */
    private final String nom;
    /**
     * La piece de la maison associee a ce meuble.
     * @see PieceMaison
     */
    private final PieceMaison pieceMaison;
    /**
     * La duree de construction du meuble.
     */
    private final int dureeConstruction;
    /**
     * Une <code>HashMap</code> des listes de lots necessaires a la construction d'un meuble
     * avec pour couple (cle, valeur) = (nom, lotID)
     * @see Lot
     */
    private final HashMap<String, Integer> listeLots;

    /**
     * Constructeur de la classe <code>Meuble</code>.
     * @param nom le nom d'un meuble
     * @param pieceMaison le nom d'une piece de la maison
     * @param dureeConstruction la duree de construction d'un meuble
     * @param listeLots la liste des lots necessaires a la construction d'un meuble
     * @see PieceMaison
     */
    public Meuble(String nom, PieceMaison pieceMaison, int dureeConstruction,
                  HashMap<String, Integer> listeLots) {
        this.meubleId = next_id++;
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
