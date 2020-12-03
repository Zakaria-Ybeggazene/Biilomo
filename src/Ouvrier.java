public class Ouvrier extends Personnel {
    //private double salaire;
    private int chefId;
    private PieceMaison specialite;

    // La classe PieceMaison est a creer:
    // PieceMaison specialite;

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

    /*public double getSalaire(){
        return salaire;
    }*/

}
