public class Lot {
    private static int last_id = 0;

    /**
     * Le numero d'identifiant d'un lot.
     */
    private final int lotId;
    /**
     * Le nom d'un lot.
     */
    private final String nom;
    /**
     * Le volume d'un lot (qui correspond a son type).
     */
    private int volume;
    /**
     * Le poids d'une unite de volume d'un lot.
     */
    private final double poids;
    /**
     * Le prix d'une unite de volume d'un lot.
     */
    private final double prix;

    /**
     * Constructeur de la classe <code>Lot</code>.
     * @param nom nom d'un lot
     * @param volume volume d'un lot
     * @param poids poids d'un lot
     * @param prix prix d'une unite de lot
     */
    public Lot(String nom, int volume, double poids, double prix) {
        this.lotId = last_id++;
        this.nom = nom;
        this.volume = volume;
        this.poids = poids;
        this.prix = prix;
    }

    public int getLotId() {
        return lotId;
    }

    public String getNom() {
        return nom;
    }

    public int getVolume() {
        return volume;
    }

    public double getPoidsUnit() {
        return poids;
    }

    public double getPrixUnit() {
        return prix;
    }

    public static int getLast_id() {return last_id;}

    public void setVolume(int volume) {
        this.volume = volume;
    }
}
