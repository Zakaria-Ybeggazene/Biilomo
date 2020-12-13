public class Lot {
    private static int last_id = 0;

    private int lotId;
    private String nom;
    private int volume;
    private double poids; //d'une unite de volume
    private double prix; //d'une unite de volume

    /**
     * Constructeur de la classe <code>Lot</code>.
     * @param nom nom du lot
     * @param volume volume du lot
     * @param poids poids du lot
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
}
