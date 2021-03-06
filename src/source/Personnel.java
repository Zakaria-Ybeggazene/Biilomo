package source;

public abstract class Personnel {
    /**
     * Un identifiant de depart servant a etablir les identifiants de tous les membres du personnel par incrementation.
     */
    private static int next_id = 1;

    /**
     * L'identifiant d'un membre du personnel.
     */
    private final int identifiant;
    /**
     * Le nom d'un membre du personnel.
     */
    private final String nom;
    /**
     * Le prenom d'un membre du personnel.
     */
    private final String prenom;

    /**
     * Constructeur de la classe abstraite <code>Personnel</code>.
     *
     * @param nom    le nom d'un membre du personnel
     * @param prenom le prenom d'un membre du personnel
     */
    public Personnel(String nom, String prenom) {
        this.identifiant = next_id++;
        this.nom = nom;
        this.prenom = prenom;
    }

    /**
     * Retourne le prochain identifiant d'un membre du personnel.
     *
     * @return un objet de type <code>int</code>
     */
    public static int getNext_id() {
        return next_id;
    }

    /**
     * Retourne l'indentifiant d'un membre du personnel de l'entrepot.
     *
     * @return un objet de type <code>int</code>
     */
    public int getIdentifiant() {
        return identifiant;
    }

    /**
     * Retourne le nom d'un membre du personnel.
     *
     * @return un objet de type <code>String</code>
     */
    public String getNom() {
        return nom;
    }

    /**
     * Retourne le prenom d'un membre du personnel.
     *
     * @return un objet de type <code>String</code>
     */
    public String getPrenom() {
        return prenom;
    }
}
