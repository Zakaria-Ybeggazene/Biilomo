public class Ouvrier extends Personnel {
    private int chefId;
    private PieceMaison specialite;

    /**
     * Constructeur de la classe <code>Ouvrier<code/>.
     * @param nom
     * @param prenom
     * @param chefId
     * @param specialite
     */
    public Ouvrier(String nom, String prenom, int chefId, PieceMaison specialite) {
        super(nom, prenom);
        this.chefId = chefId;
        this.specialite = specialite;
    }

    public int getChefId() {
        return chefId;
    }

    public PieceMaison getSpecialite() {
        return specialite;
    }

}
