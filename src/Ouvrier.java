public class Ouvrier extends Personnel {
    /**
     * L'identifiant du chef associe a l'ouvrier
     * @see ChefEquipe
     */
    private int chefId;
    /**
     * La specialite associee a l'ouvrier, qui est une piece de la maison
     * @see PieceMaison
     */
    private PieceMaison specialite;

    /**
     * Constructeur de la classe <code>Ouvrier<code/>.
     * @param nom le nom d'un ouvrier
     * @param prenom le prenom d'un ouvrier
     * @param chefId l'identifiant du chef associe a l'ouvrier
     * @param specialite la specialite d'un ouvrier
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
