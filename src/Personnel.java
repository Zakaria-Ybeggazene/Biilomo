public abstract class Personnel {
    private static int last_id = 1;

    private int identifiant;
    private String nom;
    private String prenom;

    public Personnel (String nom, String prenom){
        this.identifiant = last_id++;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getIdentifiant() {
        return identifiant;
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

}
