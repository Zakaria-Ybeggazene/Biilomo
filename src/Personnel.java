public abstract class Personnel {
    private static int last_id = 1;

    private int identifiant;
    private String nom;
    private String prenom;

    /**
     * Constructeur de la classe abstraite <code>Personnel</code>.
     * @param nom le nom d'un membre du personnel
     * @param prenom le prenom d'un membre du personnel
     */
    public Personnel (String nom, String prenom){
        this.identifiant = last_id++;
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Retourne l'indentifiant d'un membre du personnel de l'entrepot
     * @return un objet de type <code>int</code>
     */
    public int getIdentifiant() {
        return identifiant;
    }

    /**
     * Retourne le nom d'un membre du personnel
     * @return un objet de type <code>String</code>
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le prenom d'un membre du personnel
     * @return un objet de type <code>String</code>
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * Retourne l'identifiant d'un membre du personnel
     * @return
     */
    public static int getLast_id() {
        return last_id;
    }
}
